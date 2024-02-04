/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.permissions.Permission;

import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.preferences.Preferences;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPrefs extends ServerCommand {
    public CommandPrefs() {
        super("prefs", Collections.singletonList("preferences"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        Preferences prefs = new Preferences(player);
        prefs.open(player);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
