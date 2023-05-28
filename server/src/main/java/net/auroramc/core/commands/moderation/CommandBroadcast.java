/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandBroadcast extends ServerCommand {

    public CommandBroadcast() {
        super("broadcast", Arrays.asList("bc", "shout"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
            if (args.size() > 0) {
                String message = String.join(" ", args);
                BaseComponent component = TextFormatter.formatBroadcast(player, message);
                for (AuroraMCServerPlayer target : ServerAPI.getPlayers()) {
                    target.sendMessage(component);
                    target.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 2);
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Broadcast", "Invalid syntax. Correct syntax: **/broadcast [message]**"));
            }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
