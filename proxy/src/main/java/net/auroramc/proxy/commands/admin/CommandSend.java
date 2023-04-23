/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandSend extends ProxyCommand {


    public CommandSend() {
        super("send", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            if (ProxyServer.getInstance().getPlayer(args.get(0)) != null) {
                AuroraMCProxyPlayer playerTarget = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(args.get(0)));
                if(!playerTarget.getServer().getName().equals(args.get(1))){
                    ServerInfo target = ProxyAPI.getAmcServers().get(args.get(1));
                    if (target == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for server [**%s**]", args.get(1))));
                    }else {
                        playerTarget.connect(ProxyAPI.getAmcServers().get(args.get(1)));
                        playerTarget.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s** by **%s**.", playerTarget.getServer().getName(), target.getName(), player.getName())));
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You have sent **%s** to **%s**.", playerTarget.getName(), target.getName())));
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Server Manager", "They are already connected to that server!"));
                }
            } else {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                   if (uuid != null) {
                       if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                           String server = AuroraMCAPI.getDbManager().getCurrentServer(uuid);
                           if (!server.equalsIgnoreCase(args.get(1))) {
                               ServerInfo target = ProxyAPI.getAmcServers().get(args.get(1));
                               if (target == null) {
                                   player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for server [**%s**]", args.get(1))));
                               } else {
                                   player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You have sent **%s** to **%s**.", args.get(0), target.getName())));
                                   ProtocolMessage message = new ProtocolMessage(Protocol.SEND, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), args.get(0), player.getName(), target.getName());
                                   CommunicationUtils.sendMessage(message);
                               }
                           } else {
                               player.sendMessage(TextFormatter.pluginMessage("Server Manager", "They are already connected to that server!"));
                           }
                       } else {
                           player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for player [**%s**]", args.get(0))));
                       }
                   } else {
                       player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for player [**%s**]", args.get(0))));
                   }
                });
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "Invalid syntax. Correct syntax: **/send [player] [server]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
