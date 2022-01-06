/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.iplookup;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.core.gui.admin.iplookup.IPLookupPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandIPLookupPlayer extends Command {

    public CommandIPLookupPlayer() {
        super("player", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (uuid == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("No matches found for user **%s**.", args.get(0))));
                        return;
                    }
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Performing IP Lookup for user **%s**...", args.get(0))));
                    PlayerProfile playerProfile = AuroraMCAPI.getDbManager().ipLookup(uuid);
                    if (playerProfile == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("IP Lookup failed for user **%s**.", args.get(0))));
                        return;
                    }
                    IPLookupPlayer ipLookupPlayer = new IPLookupPlayer(playerProfile);
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
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup player [player name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }


}
