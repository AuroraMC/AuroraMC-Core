/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.managers;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.bigbrother.Watchdog;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.events.player.PlayerCommandEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements Listener {

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player == null) {
            e.getPlayer().spigot().sendMessage(TextFormatter.pluginMessage("Command Manager", "Your player profile is still loading, please wait to use commands!"));
            return;
        }
        if (!player.isLoaded()) {
            e.getPlayer().spigot().sendMessage(TextFormatter.pluginMessage("Command Manager", "Your player profile is still loading, please wait to use commands!"));
            return;
        }
        PlayerCommandEvent event = new PlayerCommandEvent(player, e.getMessage());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        e.setCancelled(true);
        ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getMessage().split(" ")));
        String commandLabel = args.remove(0).substring(1);
        onCommand(commandLabel, args, player);
    }

    private static void onCommand(String commandLabel, List<String> args, AuroraMCServerPlayer player) {
        ServerCommand command = (ServerCommand) AuroraMCAPI.getCommand(commandLabel.toLowerCase());
        if (command != null) {
            for (Permission permission : command.getPermission()) {
                if (player.hasPermission(permission.getId())) {
                    try {
                        command.execute(player, commandLabel, args);
                    } catch (Exception e) {
                        Watchdog.logException(e, player, "/" + commandLabel + " " + String.join(" ", args));
                        player.sendMessage(TextFormatter.pluginMessage("Command Manager", "An error occurred when executing this command. Please report this to the admins!"));
                    }
                    return;
                }
            }

            if (command.showPermissionMessage()) {
                if (command.getCustomPermissionMessage() != null) {
                    player.sendMessage(TextFormatter.pluginMessage("Command Manager", command.getCustomPermissionMessage()));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Command Manager", "You do not have permission to use that command!"));
                }
            } else {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(27))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(27), 1, true);
                }
                player.sendMessage(TextFormatter.pluginMessage("Command Manager", "That command is unrecognised."));
            }
        } else {
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(27))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(27), 1, true);
            }
            player.sendMessage(TextFormatter.pluginMessage("Command Manager", "That command is unrecognised."));
        }
    }

}
