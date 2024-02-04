/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTestMode extends ServerCommand {


    public CommandTestMode() {
        super("toggletestmode", Collections.emptyList(), Arrays.asList(Permission.ADMIN, Permission.DEBUG_ACTION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (AuroraMCAPI.isTestServer()) {
            AuroraMCAPI.setTestServer(false);
            for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                player1.sendMessage(TextFormatter.pluginMessage("Server Manager", "&a&lThis server is no longer in test mode. Player profiles will now be saved correctly."));
            }
        } else {
            AuroraMCAPI.setTestServer(true);
            for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                player1.sendMessage(TextFormatter.pluginMessage("Server Manager", "&c&lThis server is now in test mode. While test mode is enabled, stats and other core features will not be saved."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
