/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
import net.auroramc.proxy.api.utils.MaintenanceMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandMode extends ProxyCommand {


    public CommandMode() {
        super("mode", Collections.emptyList(), Collections.singletonList(Permission.ALL), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (ProxyAPI.isMaintenance()) {
                MaintenanceMode mode;
                try {
                    mode = MaintenanceMode.valueOf(args.get(0).toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(TextFormatter.pluginMessage("Maintenance", "That is an invalid maintenance mode."));
                    return;
                }

                player.sendMessage(TextFormatter.pluginMessage("Maintenance", String.format("You have set the maintenance mode to: **%s**", mode.getTitle())));
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAINTENANCE_MODE, "Mission Control", "update", AuroraMCAPI.getInfo().getName(), mode.name() + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "You cannot change the maintenance mode when the network is not in maintenance!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Maintenance", "Invalid syntax. Correct syntax: **/mm mode [mode]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> modes = new ArrayList<>();
        if (numberArguments == 1) {
            for (MaintenanceMode mode : MaintenanceMode.values()) {
                if (mode.name().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    modes.add(mode.name());
                }
            }
        }

        return modes;
    }
}
