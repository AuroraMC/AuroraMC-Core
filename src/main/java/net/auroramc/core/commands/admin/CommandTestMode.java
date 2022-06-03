/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTestMode extends Command {


    public CommandTestMode() {
        super("toggletestmode", Collections.emptyList(), Arrays.asList(Permission.ADMIN, Permission.DEBUG_ACTION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (AuroraMCAPI.isTestServer()) {
            AuroraMCAPI.setTestServer(false);
            for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Server Manager", "&a&lThis server is no longer in test mode. Player profiles will now be saved correctly."));
            }
        } else {
            AuroraMCAPI.setTestServer(true);
            for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Server Manager", "&c&lThis server is now in test mode. While test mode is enabled, stats and other core features will not be saved."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
