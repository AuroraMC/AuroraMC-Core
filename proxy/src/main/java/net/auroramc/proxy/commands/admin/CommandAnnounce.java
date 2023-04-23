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
import net.auroramc.proxy.api.utils.targetselector.SelectorParseFailException;
import net.auroramc.proxy.api.utils.targetselector.TargetSelectorParser;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandAnnounce extends ProxyCommand {


    public CommandAnnounce() {
        super("announce", Arrays.asList("alert", "broadcast", "bc"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            String selectors = args.remove(0);
            String message = String.join(" ", args);
            try {
                TargetSelectorParser.parse(selectors);
            } catch (SelectorParseFailException e) {
                player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("Failed to parse target selectors for reason: **%s**", e.getMessage())));
                return;
            }

            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.ANNOUNCE, "Mission Control", selectors, AuroraMCAPI.getInfo().getName(), message + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(protocolMessage);
                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "Announcement sent!"));
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "Invalid syntax. Correct syntax: **/announce  [targets] [message]**\n" +
                    "Information on how to use target selectors can be found here: https://auroramc.net/network-announcements/"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
