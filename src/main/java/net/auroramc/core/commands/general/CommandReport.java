package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.gui.report.ChatType;
import net.auroramc.core.gui.report.Report;
import net.auroramc.core.managers.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandReport extends Command {
    public CommandReport() {
        super("report", new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("player"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            String name = args.remove(0);
            AuroraMCPlayer target = AuroraMCAPI.getPlayer(name);
            if (args.size() == 0) {
                if (target != null) {
                    Report report = new Report(player, target.getId(), target.getPlayer().getName());
                    report.open(player);
                    AuroraMCAPI.openGUI(player, report);
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int id = AuroraMCAPI.getDbManager().getAuroraMCID(name);
                            if (id == -1) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("No results found for **%s**. They have either never joined the network or recently changed their name!", name)));
                            } else {
                                String plname  = AuroraMCAPI.getDbManager().getNameFromID(id);
                                Report report = new Report(player, id, plname);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        report.open(player);
                                        AuroraMCAPI.openGUI(player, report);
                                    }
                                }.runTask(AuroraMCAPI.getCore());
                            }
                        }
                    }.runTaskAsynchronously(AuroraMCAPI.getCore());
                }
            } else {
                //They supplied a reason.
                if (target != null) {
                    String reasonString = String.join(" ", args).toLowerCase();

                    PlayerReport.ReportReason reason = PlayerReport.ReportReason.getByAlias(reasonString);
                    if (reason != null) {
                        if (reason.getType() == PlayerReport.ReportType.CHAT) {
                            ChatType chatType = new ChatType(player, target.getId(), target.getPlayer().getName(), reason);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    chatType.open(player);
                                    AuroraMCAPI.openGUI(player, chatType);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        } else {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    PlayerReport.QueueType queueType = PlayerReport.QueueType.NORMAL;
                                    if (reason.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME || player.hasPermission("moderation")) {
                                        queueType = PlayerReport.QueueType.LEADERSHIP;
                                    }
                                    ReportManager.newReport(target.getId(), target.getPlayer().getName(), player, System.currentTimeMillis(), reason.getType(), null, reason, queueType);
                                }
                            }.runTaskAsynchronously(AuroraMCAPI.getCore());
                        }
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There is no report of that type. Please try again."));
                    }

                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int id = AuroraMCAPI.getDbManager().getAuroraMCID(name);
                            if (id == -1) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("No results found for **%s**. They have either never joined the network or recently changed their name!", args.get(0))));
                            } else {
                                String plname  = AuroraMCAPI.getDbManager().getNameFromID(id);
                                String reasonString = String.join(" ", args).toLowerCase();

                                PlayerReport.ReportReason reason = PlayerReport.ReportReason.getByAlias(reasonString);
                                if (reason != null) {
                                    if (reason.getType() == PlayerReport.ReportType.CHAT) {
                                        ChatType chatType = new ChatType(player, id, plname, reason);
                                        new BukkitRunnable(){
                                            @Override
                                            public void run() {
                                                chatType.open(player);
                                                AuroraMCAPI.openGUI(player, chatType);
                                            }
                                        }.runTask(AuroraMCAPI.getCore());
                                    } else {
                                        PlayerReport.QueueType queueType = PlayerReport.QueueType.NORMAL;
                                        if (reason.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME || player.hasPermission("moderation")) {
                                            queueType = PlayerReport.QueueType.LEADERSHIP;
                                        }
                                        ReportManager.newReport(id, plname, player, System.currentTimeMillis(), reason.getType(), null, reason, queueType);
                                    }
                                } else {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There is no report of that type. Please try again."));
                                }
                            }
                        }
                    }.runTaskAsynchronously(AuroraMCAPI.getCore());
                }
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Invalid syntax. Correct syntax: **/report <user> [reason]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        //TODO: tab complete.
        return new ArrayList<>();
    }
}
