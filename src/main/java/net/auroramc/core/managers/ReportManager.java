package net.auroramc.core.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ChatLogs;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportManager {

    private final static Object lock = new Object();
    private static final List<PlayerReport> submittedReports;
    private static final List<BukkitRunnable> reportTasks;

    static {
        submittedReports = new ArrayList<>();
        reportTasks = new ArrayList<>();
    }

    public static void newReport(int suspect, String suspectName, AuroraMCPlayer reporter, long timestamp, PlayerReport.ReportType type, PlayerReport.ChatType chatType, PlayerReport.ReportReason reason, PlayerReport.QueueType queue) {
        synchronized (lock) {
            List<PlayerReport> report = submittedReports.stream().filter(playerReport -> (playerReport.getType() == type && playerReport.getSuspect() == suspect && playerReport.getChatType() == chatType)).collect(Collectors.toList());

            if (report.size() > 0) {
                //There is a cached recent report, for this user, check if the user has already reported this person.
                List<PlayerReport> reportedPlayer = report.stream().filter(playerReport -> playerReport.getReporters().contains(reporter.getId())).collect(Collectors.toList());
                if (reportedPlayer.size() != 0) {
                    reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You have already reported that user!"));
                    return;
                }

                //There isn't a cached report by this user for this suspect, see if they submitted one from a different server.
                PlayerReport preport = AuroraMCAPI.getDbManager().getRecentReport(suspect, type);
                if (preport.getReporters().contains(reporter.getId())) {
                    reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You have already reported that user!"));
                    return;
                }

                //They have not sent one for this user, append the reporter.
                if (preport.getType() == PlayerReport.ReportType.CHAT) {
                    if (preport.getChatType() == PlayerReport.ChatType.PUBLIC) {
                        ChatLogs.appendMessages(preport.getChatReportUUID());
                    } else if (preport.getChatType() == PlayerReport.ChatType.PARTY) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("ChatReportAppend");
                        out.writeInt(preport.getId());
                        reporter.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    } else {
                        int id = AuroraMCAPI.getDbManager().newReport(suspect, reporter.getId(), timestamp, type, chatType, reason, null, queue);
                        if (id == -1) {
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "An error has occurred while trying submit this report. Please try again later."));
                            return;
                        }
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("ChatReportSent");
                        out.writeInt(id);
                        reporter.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
                        PlayerReport playerReport = new PlayerReport(id, suspect, suspectName, new ArrayList<>(Collections.singletonList(reporter.getId())), timestamp, type, chatType, reason, -1, null, PlayerReport.ReportOutcome.PENDING, null, queue, null);
                        submittedReports.add(playerReport);
                        BukkitRunnable runnable = new BukkitRunnable(){
                            @Override
                            public void run() {
                                synchronized (lock) {
                                    submittedReports.remove(playerReport);
                                    reportTasks.remove(this);
                                }
                            }
                        };
                        runnable.runTaskLaterAsynchronously(AuroraMCAPI.getCore(), 1200);
                        reportTasks.add(runnable);
                        return;
                    }
                }
                preport.addReporter(reporter.getId());
                reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
            } else {
                //There isn't a cached report for this suspect, see if there is one sent from another server/in the database from the last minute.
                PlayerReport preport = AuroraMCAPI.getDbManager().getRecentReport(suspect, type);
                if (preport != null) {
                    if (preport.getReporters().contains(reporter.getId())) {
                        reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You have already reported that user!"));
                        return;
                    }

                    //Has not already submitted a report but one exists for this suspect and specified type.
                    //They have not sent one for this user, append the reporter.

                    if (preport.getType() == PlayerReport.ReportType.CHAT) {
                        if (preport.getChatType() == PlayerReport.ChatType.PUBLIC) {
                            ChatLogs.appendMessages(preport.getChatReportUUID());
                        } else if (preport.getChatType() == PlayerReport.ChatType.PARTY) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("ChatReportAppend");
                            out.writeInt(preport.getId());
                            reporter.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        } else {
                            int id = AuroraMCAPI.getDbManager().newReport(suspect, reporter.getId(), timestamp, type, chatType, reason, null, queue);
                            if (id == -1) {
                                reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "An error has occurred while trying submit this report. Please try again later."));
                                return;
                            }
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("ChatReportSent");
                            out.writeInt(id);
                            reporter.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
                            PlayerReport playerReport = new PlayerReport(id, suspect, suspectName, new ArrayList<>(Collections.singletonList(reporter.getId())), timestamp, type, chatType, reason, -1, null, PlayerReport.ReportOutcome.PENDING, null, queue, null);
                            submittedReports.add(playerReport);
                            BukkitRunnable runnable = new BukkitRunnable(){
                                @Override
                                public void run() {
                                    synchronized (lock) {
                                        submittedReports.remove(playerReport);
                                        reportTasks.remove(this);
                                    }
                                }
                            };
                            runnable.runTaskLaterAsynchronously(AuroraMCAPI.getCore(), 1200);
                            reportTasks.add(runnable);
                            return;
                        }
                    }

                    preport.addReporter(reporter.getId());
                    reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
                    return;
                }

                //There is no currently pending report for this user, create one.
                UUID chatReportUUID = null;

                if (type == PlayerReport.ReportType.CHAT) {
                    if (chatType == PlayerReport.ChatType.PUBLIC) {
                        if (!ChatLogs.hasChatted(suspect)) {
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "That player has not chatted in this server! Please make sure you are submitting from the server the message was said in!"));
                            return;
                        }
                        chatReportUUID = ChatLogs.reportDump();
                        if (chatReportUUID == null) {
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "An error has occurred while trying to upload a chat log. Please try again later."));
                            return;
                        }
                    } else if (chatType == PlayerReport.ChatType.PARTY) {
                        if (reporter.getPartyUUID() == null) {
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You are not in a party, so cannot report party chat!"));
                            return;
                        }
                    }

                }

                int id = AuroraMCAPI.getDbManager().newReport(suspect, reporter.getId(), timestamp, type, chatType, reason, chatReportUUID, queue);
                if (id == -1) {
                    reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "An error has occurred while trying submit this report. Please try again later."));
                    return;
                }

                if (type == PlayerReport.ReportType.CHAT && chatType != PlayerReport.ChatType.PUBLIC) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("ChatReportSent");
                    out.writeInt(id);
                    reporter.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                }

                reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
                PlayerReport playerReport = new PlayerReport(id, suspect, suspectName, new ArrayList<>(Collections.singletonList(reporter.getId())), timestamp, type, chatType, reason, -1, null, PlayerReport.ReportOutcome.PENDING, null, queue, chatReportUUID);
                submittedReports.add(playerReport);
                BukkitRunnable runnable = new BukkitRunnable(){
                    @Override
                    public void run() {
                        synchronized (lock) {
                            submittedReports.remove(playerReport);
                            reportTasks.remove(this);
                        }
                    }
                };
                runnable.runTaskLaterAsynchronously(AuroraMCAPI.getCore(), 1200);
                reportTasks.add(runnable);
            }
        }
    }

}
