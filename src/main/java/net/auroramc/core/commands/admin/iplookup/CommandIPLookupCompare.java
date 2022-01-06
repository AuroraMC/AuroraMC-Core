/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.iplookup;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.core.api.punishments.ipprofiles.ProfileComparison;
import net.auroramc.core.gui.admin.iplookup.IPLookupCompare;
import net.auroramc.core.gui.admin.iplookup.IPLookupPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandIPLookupCompare extends Command {

    public CommandIPLookupCompare() {
        super("compare", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (uuid == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("No matches found for user **%s**.", args.get(0))));
                        return;
                    }
                    UUID uuid2 = AuroraMCAPI.getDbManager().getUUID(args.get(1));
                    if (uuid2 == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("No matches found for user **%s**.", args.get(1))));
                        return;
                    }
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Performing IP Lookup comparison for users **%s** and **%s**...", args.get(0), args.get(1))));
                    ProfileComparison playerProfile = AuroraMCAPI.getDbManager().ipLookup(uuid, uuid2);
                    if (playerProfile == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("IP Lookup failed for users **%s** and **%s**.", args.get(0), args.get(1))));
                        return;
                    }
                    if (playerProfile.getAmountOfCommonProfiles() == 0 && playerProfile.getAmountOfCommonAlts() == 0) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("IP Lookup found nothing in common for users **%s** and **%s**.", args.get(0), args.get(1))));
                        return;
                    }
                    IPLookupCompare ipLookupPlayer = new IPLookupCompare(playerProfile);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ipLookupPlayer.open(player);
                            AuroraMCAPI.openGUI(player, ipLookupPlayer);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup compare [player 1] [player 2]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }


}
