/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.iplookup;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.punishments.ipprofiles.IPProfile;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.admin.iplookup.IPLookupProfile;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIPLookupProfile extends ServerCommand {

    public CommandIPLookupProfile() {
        super("profile", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id;
                    try {
                        id = Integer.parseInt(args.get(0));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup profile [profile ID]**"));
                        return;
                    }
                    player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("Performing IP Lookup for profile **%s**...", args.get(0))));
                    IPProfile playerProfile = AuroraMCAPI.getDbManager().ipLookup(id);
                    if (playerProfile == null) {
                        player.sendMessage(TextFormatter.pluginMessage("IP Lookup", String.format("IP Lookup failed for profile **%s**.", args.get(0))));
                        return;
                    }
                    IPLookupProfile ipLookupPlayer = new IPLookupProfile(playerProfile);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ipLookupPlayer.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup profile [profile ID]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }


}