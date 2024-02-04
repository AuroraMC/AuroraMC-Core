/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.debug;

import net.auroramc.api.permissions.Permission;

import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandKillMessageTest extends ServerCommand {

    public CommandKillMessageTest() {
        super("KillMessageTest", Collections.emptyList(), Arrays.asList(Permission.ADMIN, Permission.ALL), false, null);
    }


    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        //
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
