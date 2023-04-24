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

public class CommandGlobalChatSlow extends ProxyCommand {


    public CommandGlobalChatSlow() {
        super("gchatslow", Collections.singletonList("gslow"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    //TODO: add protocol message
    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            short amount;
            try {
                amount = Short.parseShort(args.get(0));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            if (amount < 1 || amount > 300) {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_CHAT_SLOW, "Mission Control", "", AuroraMCAPI.getInfo().getName(), "" + amount + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            });
        } else {
            if (AuroraMCAPI.getChatSlow() != -1) {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "ChatSL"));
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_CHAT_SLOW, "Mission Control", "", AuroraMCAPI.getInfo().getName(), "-1" + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                    CommunicationUtils.sendMessage(message);
                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "There is currently no global chat slow active. To set a chat slow, use **/gchatslow [seconds]**."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
