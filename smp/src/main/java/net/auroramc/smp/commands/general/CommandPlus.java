/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general;

import net.auroramc.api.permissions.Permission;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.gui.plus.PlusColour;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandPlus extends ServerCommand {


    public CommandPlus() {
        super("plus", Arrays.asList("pluscolor", "pluscolour"), Collections.singletonList(Permission.PLUS), true, "You must have an active Plus subscription in order to use this command!");
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        PlusColour plusColour = new PlusColour(player);
        plusColour.open(player);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
