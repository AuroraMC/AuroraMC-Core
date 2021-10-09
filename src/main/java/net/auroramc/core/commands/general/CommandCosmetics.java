/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.cosmetics.Cosmetics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCosmetics extends Command {

    public CommandCosmetics() {
        super("cosmetics", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        Cosmetics cosmetics = new Cosmetics(player);
        cosmetics.open(player);
        AuroraMCAPI.openGUI(player, cosmetics);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
