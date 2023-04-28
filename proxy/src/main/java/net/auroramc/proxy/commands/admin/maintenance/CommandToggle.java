/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
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

public class CommandToggle extends ProxyCommand {


    public CommandToggle() {
        super("toggle", Collections.emptyList(), Collections.singletonList(Permission.ALL), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (!ProxyAPI.isMaintenance()) {
                MaintenanceMode mode;
                try {
                    mode = MaintenanceMode.valueOf(args.get(0).toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(TextFormatter.pluginMessage("Maintenance", "That is an invalid maintenance mode."));
                    return;
                }

                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "You have toggled maintenance mode on."));
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAINTENANCE_MODE, "Mission Control", "enable", AuroraMCAPI.getInfo().getName(), mode.name() + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "You have toggled maintenance mode off."));
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAINTENANCE_MODE, "Mission Control", "disable", AuroraMCAPI.getInfo().getName(), "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            }
        } else {
            if (ProxyAPI.isMaintenance()) {
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "You have toggled maintenance mode off."));
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAINTENANCE_MODE, "Mission Control", "disable", AuroraMCAPI.getInfo().getName(), "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "You must specify a mode when toggling maintenance on!"));
            }
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
