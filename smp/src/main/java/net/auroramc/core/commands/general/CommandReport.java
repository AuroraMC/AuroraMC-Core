/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.report.ChatType;
import net.auroramc.core.gui.report.Report;
import net.auroramc.core.managers.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandReport extends ServerCommand {
    public CommandReport() {
        super("report", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            String name = args.remove(0);
            if (name.equalsIgnoreCase(player.getName()) || name.equalsIgnoreCase(player.getName())) {
                player.sendMessage(TextFormatter.pluginMessage("Reports", "You cannot report yourself, silly!"));
                return;
            }

            AuroraMCServerPlayer target = ServerAPI.getDisguisedPlayer(name);
            if (target == null) {
                target = ServerAPI.getPlayer(name);
            }
            if (args.size() == 0) {
                if (target != null) {
                    Report report = new Report(player, target.getId(), target.getName());
                    report.open(player);
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int id;
                            if (AuroraMCAPI.getDbManager().isAlreadyDisguise(name)) {
                                id = AuroraMCAPI.getDbManager().getAuroraMCID(AuroraMCAPI.getDbManager().getUUIDFromDisguise(name));
                            } else {
                                id = AuroraMCAPI.getDbManager().getAuroraMCID(name);
                            }
                            if (id == -1) {
                                player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("No results found for **%s**. They have either never joined the network or recently changed their name!", name)));
                            } else {
                                Report report = new Report(player, id, name);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        report.open(player);
                                    }
                                }.runTask(ServerAPI.getCore());
                            }

                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                }
            } else {
                //They supplied a reason.
                if (target != null) {
                    String reasonString = String.join(" ", args).toLowerCase();

                    PlayerReport.ReportReason reason = PlayerReport.ReportReason.getByAlias(reasonString);
                    if (reason != null) {
                        final AuroraMCServerPlayer finalTarget = target;
                        if (reason.getType() == PlayerReport.ReportType.CHAT) {
                            ChatType chatType = new ChatType(player, target.getId(), target.getName(), reason);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    chatType.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        } else {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    PlayerReport.QueueType queueType = PlayerReport.QueueType.NORMAL;
                                    if (reason.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME || player.hasPermission("moderation")) {
                                        queueType = PlayerReport.QueueType.LEADERSHIP;
                                    }
                                    ReportManager.newReport(finalTarget.getId(), finalTarget.getName(), player, System.currentTimeMillis(), reason.getType(), null, reason, queueType);
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Reports", "There is no report of that type. Please try again."));
                    }

                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int id;
                            if (AuroraMCAPI.getDbManager().isAlreadyDisguise(name)) {
                                id = AuroraMCAPI.getDbManager().getAuroraMCID(AuroraMCAPI.getDbManager().getUUIDFromDisguise(name));
                            } else {
                                id = AuroraMCAPI.getDbManager().getAuroraMCID(name);
                            }
                            if (id == -1) {
                                player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("No results found for **%s**. They have either never joined the network or recently changed their name!", args.get(0))));
                            } else {
                                String reasonString = String.join(" ", args).toLowerCase();

                                PlayerReport.ReportReason reason = PlayerReport.ReportReason.getByAlias(reasonString);
                                if (reason != null) {
                                    if (reason.getType() == PlayerReport.ReportType.CHAT) {
                                        ChatType chatType = new ChatType(player, id, name, reason);
                                        new BukkitRunnable(){
                                            @Override
                                            public void run() {
                                                chatType.open(player);
                                            }
                                        }.runTask(ServerAPI.getCore());
                                    } else {
                                        PlayerReport.QueueType queueType = PlayerReport.QueueType.NORMAL;
                                        if (reason.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME || player.hasPermission("moderation")) {
                                            queueType = PlayerReport.QueueType.LEADERSHIP;
                                        }
                                        ReportManager.newReport(id, name, player, System.currentTimeMillis(), reason.getType(), null, reason, queueType);
                                    }
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Reports", "There is no report of that type. Please try again."));
                                }
                            }
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                }
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Reports", "Invalid syntax. Correct syntax: **/report <user> [reason]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (player1.getName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    completions.add(player1.getName());
                }
            }
        } else if (numberArguments == 2) {
            for (PlayerReport.ReportReason reason : PlayerReport.ReportReason.values()) {
                for (String alias : reason.getAliases()) {
                    if (alias.toLowerCase().startsWith(lastToken.toLowerCase())) {
                        completions.add(alias);
                    }
                }
            }
        }
        return completions;
    }
}
