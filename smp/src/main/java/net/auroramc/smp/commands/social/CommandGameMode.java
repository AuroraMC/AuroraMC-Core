/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.social;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGameMode extends ServerCommand {


    public CommandGameMode() {
        super("gamemode", Collections.singletonList("gm"), Collections.singletonList(Permission.SOCIAL), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(TextFormatter.pluginMessage("GameMode", "Creative Mode: §cDisabled"));
        } else {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(TextFormatter.pluginMessage("GameMode", "Creative Mode: §aEnabled"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
