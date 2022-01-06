/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.managers;

import net.auroramc.core.AuroraMC;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.events.CommandEngineOverwriteEvent;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
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
        AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
        if (player == null) {
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "Your player profile is still loading, please wait to use commands!"));
            return;
        }
        if (!player.isLoaded()) {
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "Your player profile is still loading, please wait to use commands!"));
            return;
        }
        CommandEngineOverwriteEvent event = new CommandEngineOverwriteEvent(e.getMessage(), AuroraMCAPI.getPlayer(e.getPlayer()));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        e.setCancelled(true);
        ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getMessage().split(" ")));
        String commandLabel = args.remove(0).substring(1);
        onCommand(commandLabel, args, player);
    }

    private static void onCommand(String commandLabel, List<String> args, AuroraMCPlayer player) {
        Command command = AuroraMCAPI.getCommand(commandLabel.toLowerCase());
        if (command != null) {
            for (Permission permission : command.getPermission()) {
                if (player.hasPermission(permission.getId())) {
                    try {
                        command.execute(player, commandLabel, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "An error occurred when executing this command. Please report this to the admins!"));
                    }
                    return;
                }
            }

            if (command.showPermissionMessage()) {
                if (command.getCustomPermissionMessage() != null) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", command.getCustomPermissionMessage()));
                } else {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "You do not have permission to use that command!"));
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "That command is unrecognised."));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "That command is unrecognised."));
        }
    }

}
