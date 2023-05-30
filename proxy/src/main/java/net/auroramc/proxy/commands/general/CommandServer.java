/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandServer extends ProxyCommand {

    public CommandServer() {
        super("server", Collections.singletonList("sv"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if(!player.getServer().getName().equals(args.get(0))){
                ServerInfo target = ProxyAPI.getAmcServers().get(args.get(0));
                if (target == null) {
                    player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for [**%s**]", args.get(0))));
                }else {
                    switch (((String) target.getServerType().get("type")).toLowerCase()) {
                        case "build":
                            if (!player.hasPermission("build") && !player.hasPermission("admin") && !player.hasPermission("events")) {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(7))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(7), 1, true);
                                }
                                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                                return;
                            }
                            player.connect(target);
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                            break;
                        case "staff":
                            if (!player.hasPermission("moderation")) {
                                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(7))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(7), 1, true);
                                }
                                return;
                            }
                            player.connect(target);
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                            break;
                        case "pathfinder": {
                            if (!player.hasPermission("moderation")) {
                                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You cannot manually go to a Pathfinder Server as Pathfinder Servers are assigned."));
                                return;
                            }
                            player.connect(target);
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                            break;
                        }
                        case "smp": {
                            if (!player.hasPermission("moderation")) {
                                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You cannot manually go to an SMP server. You must use the Lobby to connect to SMP."));
                                return;
                            }
                            player.connect(target);
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                            break;
                        }
                        default:
                            player.connect(target);
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                    }
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You are already connected to that server!"));
            }

        } else {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are currently connected to: **%s**", player.getServer().getName())));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
