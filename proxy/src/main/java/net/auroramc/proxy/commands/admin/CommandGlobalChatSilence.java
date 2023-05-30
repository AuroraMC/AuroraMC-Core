/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

public class CommandGlobalChatSilence extends ProxyCommand {

    public CommandGlobalChatSilence() {
        super("gchatsilence", Collections.singletonList("gsilence"), Collections.singletonList(Permission.ALL), false, null);
    }

    //TODO: add protocol message
    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            short amount;
            try {
                amount = Short.parseShort(args.get(0));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Silence", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            if (amount < 1 || amount > 300) {
                player.sendMessage(TextFormatter.pluginMessage("Silence", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            player.sendMessage(TextFormatter.pluginMessage("Silence", "Global chat silence has been set enabled for **" + amount + " seconds**."));
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_CHAT_SILENCE, "Mission Control", "enable", AuroraMCAPI.getInfo().getName(), amount + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            });
        } else {
            if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                player.sendMessage(TextFormatter.pluginMessage("Silence", "Global chat silence has been disabled."));
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_CHAT_SILENCE, "Mission Control", "disable", AuroraMCAPI.getInfo().getName(), "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                    CommunicationUtils.sendMessage(message);
                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Silence", "Global chat silence has been set enabled for **Permanent**."));
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_CHAT_SILENCE, "Mission Control", "enable", AuroraMCAPI.getInfo().getName(), "-1\n" + AuroraMCAPI.getInfo().getNetwork().name());
                    CommunicationUtils.sendMessage(message);
                });
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

}
