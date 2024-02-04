/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.api;

import net.auroramc.api.command.Command;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;

import java.util.List;

public abstract class ProxyCommand extends Command<AuroraMCProxyPlayer> {


    public ProxyCommand(String mainCommand, List<String> alises, List<Permission> permission, boolean showPermissionMessage, String customPermissionMessage) {
        super(mainCommand, alises, permission, showPermissionMessage, customPermissionMessage);
    }

}