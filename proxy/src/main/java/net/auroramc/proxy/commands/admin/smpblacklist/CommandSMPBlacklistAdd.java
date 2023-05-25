/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.smpblacklist;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandSMPBlacklistAdd extends ProxyCommand {


    public CommandSMPBlacklistAdd() {
        super("add", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                AuroraMCAPI.getDbManager().addSMPBlacklist(args.get(0));
                player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", "User blacklisted."));
                ProxiedPlayer pl = ProxyAPI.getCore().getProxy().getPlayer(args.get(0));
                if (pl != null) {
                    AuroraMCProxyPlayer proxyPlayer = ProxyAPI.getPlayer(pl);
                    if (proxyPlayer.getServer().getName().startsWith("SMP")) {
                        proxyPlayer.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "You have been blacklisted from the NuttersSMP. You are being sent to a Lobby.\n\nIf you believe this to be a mistake, please contact @Heliology#3092 on Discord."));
                        List<ServerInfo> lobbies = ProxyAPI.getLobbyServers();
                        ServerInfo leastPopulated = null;
                        assert player != null;
                        for (ServerInfo lobby : lobbies) {
                            if ((leastPopulated == null || leastPopulated.getCurrentPlayers() > lobby.getCurrentPlayers()) && !player.getServer().getName().equals(lobby.getName())) {
                                leastPopulated = lobby;
                            }
                        }
                        assert leastPopulated != null;
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), leastPopulated.getName())));
                        player.connect(leastPopulated);
                    }
                } else {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "smpblacklist", AuroraMCAPI.getInfo().getName(), uuid.toString());
                        CommunicationUtils.sendMessage(message);
                    }
                }
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Invalid syntax. Correct syntax: **/blacklist add [name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
