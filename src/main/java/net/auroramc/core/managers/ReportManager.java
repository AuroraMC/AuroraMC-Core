package net.auroramc.core.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ChatLogs;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.punishments.Punishment;

import java.util.List;
import java.util.UUID;

public class ReportManager {

    private final static Object lock = new Object();

    public static void newReport(int suspect, String suspectName, AuroraMCPlayer reporter, long timestamp, PlayerReport.ReportType type, PlayerReport.ChatType chatType, PlayerReport.ReportReason reason, PlayerReport.QueueType queue) {
        synchronized (lock) {
            switch (type) {
                case HACKING:
                case INAPPROPRIATE_NAME: {
                    PlayerReport preport = AuroraMCAPI.getDbManager().getActiveReport(suspect, type);
                    if (preport == null) {
                        //new report
                        submitNewReport(suspect, suspectName, reporter, timestamp, type, chatType, reason, queue);
                    } else {
                        if (preport.getReporters().contains(reporter.getId())) {
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You have already reported that user!"));
                        } else {
                            preport.addReporter(reporter.getId());
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
                        }
                        return;
                    }
                    break;
                }
                case MISC:
                case CHAT: {
                    PlayerReport preport = AuroraMCAPI.getDbManager().getRecentReport(suspect, type, reason);
                    if (preport != null) {
                        if (preport.getReporters().contains(reporter.getId())) {
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You have already reported that user!"));
                        } else {
                            preport.addReporter(reporter.getId());
                            reporter.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Thank you for submitting a report! A member of our staff team will look into it as soon as possible!"));
                            if (preport.getType() == PlayerReport.ReportType.CHAT) {
                                if (preport.getChatType() == PlayerReport.ChatType.PUBLIC) {
                                    ChatLogs.appendMessages(preport.getChatReportUUID());
                                } else if (preport.getChatType() == PlayerReport.ChatType.PARTY) {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    out.writeUTF("ChatReportAppend");
                                    out.writeInt(preport.getId());
                                    reporter.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                }
                            }
                        }
                    } else {
                        submitNewReport(suspect, suspectName, reporter, timestamp, type, chatType, reason, queue);
                    }
                    break;
                }
            }
        }
    }

    private static void submitNewReport(int suspect, String suspectName, AuroraMCPlayer reporter, long timestamp, PlayerReport.ReportType type, PlayerReport.ChatType chatType, PlayerReport.ReportReason reason, PlayerReport.QueueType queue) {
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
    }


    public static boolean canAutoHandle(AuroraMCPlayer player, PlayerReport report) {
        if (report != null) {
            switch (report.getType()) {
                case HACKING:
                case MISC: {
                    if (!AuroraMCAPI.getDbManager().hasActiveSession(report.getSuspect())) {
                        report.handle(player, PlayerReport.ReportOutcome.DENIED, null, false);
                        return true;
                    }
                }
                case CHAT:
                    List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(report.getSuspect());
                    for (Punishment punishment : punishments) {
                        if ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1)) {
                            if (punishment.getRuleID() == report.getReason().getDefaultRule()) {
                                report.handle(player, PlayerReport.ReportOutcome.DENIED, null, true);
                                return true;
                            }
                        }
                    }
                    break;
                case INAPPROPRIATE_NAME:
                    if (AuroraMCAPI.getDbManager().isUsernameBanned(report.getSuspectName().toLowerCase())) {
                        report.handle(player, PlayerReport.ReportOutcome.DENIED, null, true);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}
