/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api;

import net.auroramc.api.command.Command;
import net.auroramc.api.permissions.Permission;
import net.auroramc.core.api.player.AuroraMCServerPlayer;

import java.util.List;

public abstract class ServerCommand extends Command<AuroraMCServerPlayer> {


    public ServerCommand(String mainCommand, List<String> alises, List<Permission> permission, boolean showPermissionMessage, String customPermissionMessage) {
        super(mainCommand, alises, permission, showPermissionMessage, customPermissionMessage);
    }

}