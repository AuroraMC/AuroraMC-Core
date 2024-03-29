/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.maintenance;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandMotd extends ProxyCommand {


    public CommandMotd() {
        super("motd", Collections.emptyList(), Collections.singletonList(Permission.ALL), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (ProxyAPI.isMaintenance()) {
                String motd = String.join(" ", args);
                motd = TextFormatter.convert(motd);
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", String.format("You have set the maintenance MOTD to: **%s**", motd)));
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MOTD, "Mission Control", "maintenance", AuroraMCAPI.getInfo().getName(), motd + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "You cannot change the MOTD when the network is not in maintenance!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Maintenance", "Invalid syntax. Correct syntax: **/mm motd [Message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
