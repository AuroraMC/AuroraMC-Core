/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.admin.SetRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommandSetRank extends ServerCommand {


    public CommandSetRank() {
        super("setrank", Arrays.asList("sr", "updaterank"), Arrays.asList(Permission.ADMIN,Permission.BUILD_TEAM_MANAGEMENT,Permission.STAFF_MANAGEMENT,Permission.SUPPORT), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            String name = args.get(0);
            if (name.matches("[a-zA-Z0-9_]{3,16}")) {
                if (name.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(TextFormatter.pluginMessage("SetRank", "You cannot set your own rank."));
                    return;
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(name);
                        if (id < 1) {
                            player.sendMessage(TextFormatter.pluginMessage("SetRank", String.format("User [**%s**] has never joined the network, so cannot receive a rank.", name)));
                            return;
                        }

                        Rank rank = AuroraMCAPI.getDbManager().getRank(id);
                        if (rank == null) {
                            player.sendMessage(TextFormatter.pluginMessage("SetRank", "There has been an error trying to retrieve their rank from the Database. Please try again."));
                            return;
                        }

                        UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);

                        if (player.getRank().hasPermission("admin")) {
                            if (rank.getId() >= player.getRank().getId()) {
                                player.sendMessage(TextFormatter.pluginMessage("SetRank", "You cannot set the rank of a user who has a higher or the same rank as you."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.FULL);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        } else if (player.hasPermission("btm")) {
                            if (rank.getId() >= player.getRank().getId()) {
                                player.sendMessage(TextFormatter.pluginMessage("SetRank", "You cannot set the rank of a user who has a higher or the same rank as you."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.BTM);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        } else if (player.hasPermission("staffmanagement")) {
                            if (rank.getId() >= player.getRank().getId()) {
                                player.sendMessage(TextFormatter.pluginMessage("SetRank", "You cannot set the rank of a user who has a higher or the same rank as you."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.STM);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        } else if (player.hasPermission("support")) {
                            if (rank.getId() > 4) {
                                player.sendMessage(TextFormatter.pluginMessage("SetRank", "You cannot set the rank of a user who is a staff member."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.SUPPORT);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        } else {
                            if (rank.hasPermission("admin") || rank.getId() == player.getRank().getId()) {
                                player.sendMessage(TextFormatter.pluginMessage("SetRank", "You cannot set the rank of a user who is leadership staff."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.FULL);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        }
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            } else {
                player.sendMessage(TextFormatter.pluginMessage("SetRank", "Invalid syntax. Correct syntax: **/setrank [username]**"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("SetRank", "Invalid syntax. Correct syntax: **/setrank [username]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        ArrayList<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (player1.getName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    completions.add(player1.getName());
                }
            }
        }
        return completions;
    }
}
