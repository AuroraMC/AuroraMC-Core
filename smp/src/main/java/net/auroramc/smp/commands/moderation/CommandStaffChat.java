/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandStaffChat extends ServerCommand {


    public CommandStaffChat() {
        super("staffchat", Arrays.asList("sc", "s", "staff", "doyourjobmod", "helpmemoderatoryouresupposedtohelppeoplewhichiswhyyoureamoderator"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
            if (args.size() > 0) {
                String message = String.join(" ", args);
                player.sendMessage(TextFormatter.formatStaffMessage(player, message));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCServerPlayer p = ServerAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.sendMessage(TextFormatter.formatStaffMessage(player, message));
                                p.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                            }
                        }

                    }
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Staff Chat", "Invalid syntax. Correct syntax: **/s [message]**"));
            }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
