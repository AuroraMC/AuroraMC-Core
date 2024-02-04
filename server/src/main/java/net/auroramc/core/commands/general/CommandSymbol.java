/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.permissions.Permission;

import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.plus.SymbolColour;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSymbol extends ServerCommand {


    public CommandSymbol() {
        super("symbol", Arrays.asList("symbolcolor", "symbolcolour"), Collections.singletonList(Permission.PLUS), true, "You must have an active Plus subscription in order to use this command!");
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        SymbolColour symbolColour = new SymbolColour(player);
        symbolColour.open(player);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
