/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Sound;
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
                    target.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
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
