/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSpectator extends ServerCommand {


    public CommandSpectator() {
        super("spectator", Collections.singletonList("spec"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(TextFormatter.pluginMessage("GameMode", "Spectator Mode: §cDisabled"));
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(TextFormatter.pluginMessage("GameMode", "Spectator Mode: §aEnabled"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
