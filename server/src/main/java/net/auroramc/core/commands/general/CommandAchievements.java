/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.stats.achievements.Achievements;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandAchievements extends ServerCommand {
    public CommandAchievements() {
        super("achievements", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getStats() != null) {
            Achievements achievements = new Achievements(player, player.getStats(), player.getName());
            achievements.open(player);
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Statistics", "Your statistics are still loading, please wait to use this command!"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
