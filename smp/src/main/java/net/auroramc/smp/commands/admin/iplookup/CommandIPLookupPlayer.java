/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.admin.iplookup;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.gui.admin.iplookup.IPLookupPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandIPLookupPlayer extends ServerCommand {

    public CommandIPLookupPlayer() {
        super("player", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("No matches found for user **%s**.", args.get(0))));
                        return;
                    }
                    player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("Performing IP Lookup for user **%s**...", args.get(0))));
                    PlayerProfile playerProfile = AuroraMCAPI.getDbManager().ipLookup(uuid);
                    if (playerProfile == null) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("IP Lookup failed for user **%s**.", args.get(0))));
                        return;
                    }
                    IPLookupPlayer ipLookupPlayer = new IPLookupPlayer(playerProfile);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ipLookupPlayer.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup player [player name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }


}
