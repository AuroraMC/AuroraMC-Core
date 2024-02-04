/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.events.player.PlayerVanishEvent;
import net.auroramc.api.permissions.Permission;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandVanish extends ServerCommand {


    public CommandVanish() {
        super("vanish", Collections.singletonList("v"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        PlayerVanishEvent event = new PlayerVanishEvent(player, !player.isVanished());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            player.sendMessage(TextFormatter.pluginMessage("Vanish", "You cannot vanish at this time."));
            return;
        }
        if (!event.isVanish()) {
            player.unvanish();
            player.sendMessage(TextFormatter.pluginMessage("Vanish", "You are no longer vanished."));
        } else {
            player.vanish();
            player.sendMessage(TextFormatter.pluginMessage("Vanish", "You are now vanished."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
