/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.plus.LevelColour;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLevel extends Command {


    public CommandLevel() {
        super("level", Arrays.asList("levelcolor", "levelcolour"), Collections.singletonList(Permission.PLUS), true, "You must have an active Plus subscription in order to use this command!");
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        LevelColour levelColour = new LevelColour(player);
        levelColour.open(player);
        AuroraMCAPI.openGUI(player, levelColour);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
