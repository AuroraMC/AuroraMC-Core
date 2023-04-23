/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.iplookup;

import net.auroramc.api.AuroraMCAPI;

import net.auroramc.api.punishments.ipprofiles.ProfileComparison;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.admin.iplookup.IPLookupCompare;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandIPLookupCompare extends ServerCommand {

    public CommandIPLookupCompare() {
        super("compare", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("No matches found for user **%s**.", args.get(0))));
                        return;
                    }
                    UUID uuid2 = AuroraMCAPI.getDbManager().getUUID(args.get(1));
                    if (uuid2 == null) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("No matches found for user **%s**.", args.get(1))));
                        return;
                    }
                    player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("Performing IP Lookup comparison for users **%s** and **%s**...", args.get(0), args.get(1))));
                    ProfileComparison playerProfile = AuroraMCAPI.getDbManager().ipLookup(uuid, uuid2);
                    if (playerProfile == null) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("IP Lookup failed for users **%s** and **%s**.", args.get(0), args.get(1))));
                        return;
                    }
                    if (playerProfile.getAmountOfCommonProfiles() == 0 && playerProfile.getAmountOfCommonAlts() == 0) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("IP Lookup found nothing in common for users **%s** and **%s**.", args.get(0), args.get(1))));
                        return;
                    }
                    IPLookupCompare ipLookupPlayer = new IPLookupCompare(playerProfile);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ipLookupPlayer.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup compare [player 1] [player 2]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }


}
