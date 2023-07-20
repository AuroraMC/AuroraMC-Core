/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.social;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTeleport extends ServerCommand {
    public CommandTeleport() {
        super("Teleport", Arrays.asList("tp", "teleportplayer"), List.of(Permission.SOCIAL), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            if (args.get(0).equalsIgnoreCase("here")) {
                if (args.size() == 2) {
                    if (args.get(1).equalsIgnoreCase("all")) {
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", "You have teleported everyone to yourself."));
                        for (Player from : Bukkit.getOnlinePlayers()) {
                            if (from.equals(player)) {
                                continue;
                            }
                            from.spigot().sendMessage(TextFormatter.pluginMessage("Map Manager", String.format("You have been teleported to player **%s**.", player.getName())));
                            from.teleport(player.getLocation());
                        }
                    } else {
                        Player target = Bukkit.getPlayer(args.get(1));
                        AuroraMCServerPlayer aTarget = ServerAPI.getPlayer(target);
                        if (target != null) {
                            aTarget.teleport(player.getLocation());
                            player.sendMessage(TextFormatter.pluginMessage("Map Manager", String.format("You have teleported **%s** to your location.", target.getName())));
                            aTarget.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("**%s** has teleported you to their location.", player.getName())));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("No matches found for user [**%s**]", args.get(0))));
                        }
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Teleport", "Invalid syntax. Correct syntax: **/tp here <user | all**"));
                }
            } else {
                if (args.size() == 1) {
                    if (args.get(0).equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", "You cannot teleport to yourself."));
                        return;
                    }
                    Player target = Bukkit.getPlayer(args.get(0));
                    AuroraMCServerPlayer aTarget = ServerAPI.getPlayer(target);
                    if (target != null) {
                        player.teleport(aTarget.getLocation());
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You have been teleported to player **%s**.", target.getName())));
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("No matches found for user [**%s**]", args.get(0))));
                    }
                } else if (args.size() == 2) {
                    if (args.get(0).equalsIgnoreCase(args.get(1))) {
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", "You cannot teleport a player to themselves."));
                        return;
                    }
                    Player from = Bukkit.getPlayer(args.get(0));
                    AuroraMCServerPlayer afrom = ServerAPI.getPlayer(from);

                    if (from != null) {

                        Player to = Bukkit.getPlayer(args.get(1));
                        AuroraMCServerPlayer ato = ServerAPI.getPlayer(to);

                        if (to != null) {
                            afrom.sendMessage(TextFormatter.pluginMessage("Map Manager", String.format("You have been teleported to player **%s**.", to.getName())));
                            player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You have teleported **%s** to **%s**.", args.get(0), args.get(1))));
                            afrom.teleport(ato.getLocation());
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("No matches found for user [**%s**]", args.get(1))));
                        }
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("No matches found for user [**%s**]", args.get(0))));
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Teleport", "Invalid syntax. Correct syntax: **/tp <user> <user>**"));
                }
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teleport", "Invalid syntax. Correct syntax: **/tp <user> <user>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
