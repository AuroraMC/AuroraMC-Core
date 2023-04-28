/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin;


import net.auroramc.api.AuroraMCAPI;
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

public class CommandMotd extends ProxyCommand {

    public CommandMotd() {
        super("motd", Collections.singletonList("news"), Collections.singletonList(Permission.ALL), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (ProxyAPI.isMaintenance()) {
                player.sendMessage(TextFormatter.pluginMessage("MOTD", "You cannot change the MOTD while the network is in maintenance."));
                return;
            }
            String motd = String.join(" ", args);
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MOTD, "Mission Control", "", AuroraMCAPI.getInfo().getName(), motd + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            });

            player.sendMessage(TextFormatter.pluginMessage("MOTD", String.format("You set the MOTD to: %s", motd)));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("MOTD", "Invalid syntax. Correct syntax: **/motd [Message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
