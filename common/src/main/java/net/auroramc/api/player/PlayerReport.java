/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.punishments.Rule;
import net.auroramc.api.utils.PunishUtils;
import net.auroramc.api.utils.TextFormatter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.auroramc.api.player.PlayerReport.ReportType.*;

public class PlayerReport {

    public enum ReportType {CHAT, MISC, HACKING, INAPPROPRIATE_NAME}
    public enum ChatType {PUBLIC, PRIVATE, PARTY}
    public enum ReportOutcome {ACCEPTED, DENIED, PENDING, EXPIRED}
    public enum QueueType {LEADERSHIP, NORMAL}

    private final int id;
    private final int suspect;
    private final String suspectName;
    private final List<Integer> reporters;
    private final long timestamp;
    private final ReportType type;
    private final ChatType chatType;
    private final ReportReason reason;
    private int handler;
    private String handlerName;
    private ReportOutcome outcome;
    private UUID chatReportUUID;
    private ReportReason reasonAccepted;
    private QueueType queue;

    public PlayerReport(int id, int suspect, String suspectName, List<Integer> reporters, long timestamp, ReportType type, ChatType chatType, ReportReason reason, int handler, String handlerName, ReportOutcome outcome, ReportReason reasonAccepted, QueueType queue, UUID chatReportUUID) {
        this.id = id;
        this.suspect = suspect;
        this.suspectName = suspectName;
        this.reporters = reporters;
        this.timestamp = timestamp;
        this.type = type;
        this.chatType = chatType;
        this.reason = reason;
        this.handler = handler;
        this.handlerName = handlerName;
        this.outcome = outcome;
        this.reasonAccepted = reasonAccepted;
        this.queue = queue;
        this.chatReportUUID = chatReportUUID;
    }

    public ReportType getType() {
        return type;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public int getHandler() {
        return handler;
    }

    public List<Integer> getReporters() {
        return reporters;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public QueueType getQueue() {
        return queue;
    }

    public ReportOutcome getOutcome() {
        return outcome;
    }

    public ReportReason getReason() {
        return reason;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public ReportReason getReasonAccepted() {
        return reasonAccepted;
    }

    public UUID getChatReportUUID() {
        return chatReportUUID;
    }

    public int getId() {
        return id;
    }

    public int getSuspect() {
        return suspect;
    }

    public String getSuspectName() {
        return suspectName;
    }

    public void attachChatLog(UUID uuid) {
        this.chatReportUUID = uuid;
        ScheduleFactory.scheduleAsync(() -> {
            AuroraMCAPI.getDbManager().attachChatLog(id, uuid);
        });
    }

    public void addReporter(int reporter) {
        reporters.add(reporter);
        ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().addReporter(id, reporter));
    }

    public void handle(AuroraMCPlayer player, ReportOutcome outcome, ReportReason acceptedReason, boolean alt) {
        this.outcome = outcome;
        this.reasonAccepted = acceptedReason;

        Rule rule;
        if (outcome == ReportOutcome.ACCEPTED) {
            if (acceptedReason != null) {
                rule = AuroraMCAPI.getRules().getRule(((alt)? acceptedReason.getAltRule() : acceptedReason.getDefaultRule()));
                PunishUtils.punishUser(suspect, suspectName, player, rule, acceptedReason.getName() + " [Report #" + id + "]");
            }
        } else {
            if (alt) {
                this.outcome = ReportOutcome.ACCEPTED;
                outcome = ReportOutcome.ACCEPTED;
            }
        }

        AuroraMCAPI.getDbManager().handleReport(id, outcome, reasonAccepted);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ReportHandled");
        out.writeInt(id);
        player.sendPluginMessage(out.toByteArray());

        player.sendMessage(TextFormatter.pluginMessage("Reports", "The report has been handled."));
    }

    public void forwardToLeadership(AuroraMCPlayer player) {
        this.handler = 0;
        this.handlerName = null;
        this.queue = QueueType.LEADERSHIP;
        ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().forwardReportToLeadership(id));
        player.sendMessage(TextFormatter.pluginMessage("Reports", "The report has been forwarded to the Leadership Team."));
    }

    public void abort(AuroraMCPlayer player) {
        this.handler = 0;
        this.handlerName = null;
        ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().abortReport(id));
        player.sendMessage(TextFormatter.pluginMessage("Reports", "The report has been aborted."));
    }

    public enum ReportReason {
        SPAMMING(1, 21, "Spamming", CHAT, Arrays.asList("spamming", "spam", "chat spam", "flooding", "chat flooding")),
        RUDENESS(2, null, "Rudeness", CHAT, Arrays.asList("rudeness", "gr", "general rudeness", "rude", "mean", "unkind")),
        TROLLING(3, null, "Trolling", CHAT, Arrays.asList("trolling", "troll")),
        FILTER_EVASION(5, 8, "Filter Evasion", CHAT, Arrays.asList("filter evasion", "filter", "filter bypass", "swearing", "fe")),
        HARASSMENT(7, null, "Harassment", CHAT, Arrays.asList("harassment", "harass")),
        REVEALING_PERSONAL_INFORMATION(9, null, "Revealing Personal Information", CHAT, Arrays.asList("revealing personal information", "rpi", "doxxing", "leaking", "dox")),
        MALICIOUS_THREATS(10, null, "Malicious Threats", CHAT, Arrays.asList("malicious threats", "mt", "threats", "threatening")),
        DISCRIMINATION(13, null, "Discrimination", CHAT, Arrays.asList("discrimination", "racist", "racism", "discrim")),
        ADVERTISEMENT(14, null, "Advertisement", CHAT, Arrays.asList("advertisement", "advertising")),
        BOT_SPAM(21, null, "Bot Spam", CHAT, Collections.singletonList("bot spam")),
        MAP_EXPLOITING(15, null, "Map Exploiting", MISC, Collections.singletonList("map exploiting")),
        BUG_EXPLOITING(20, null, "Bug Exploiting", MISC, Arrays.asList("bug exploiting", "exploiting")),
        COMPROMISED_ACCOUNT(21, null, "Compromised Account", MISC, Arrays.asList("compromised account", "compromised", "comp")),
        INAPPROPRIATE_SKIN(22, null, "Inappropriate Skin", MISC, Arrays.asList("inappropriate skin", "skin", "bad skin")),
        INAPPROPRIATE_CAPE(22, null, "Inappropriate Cape", MISC, Arrays.asList("inappropriate cape", "cape", "bad cape")),
        STAT_BOOSTING(17, null, "Stat Boosting", MISC, Arrays.asList("stat boosting", "stat", "stats")),
        KILL_AURA(18, null, "Kill Aura", HACKING, Arrays.asList("kill aura", "killaura", "ka", "killbot", "tpaura", "aura")),
        BUNNY_HOP(18, null, "Bunny Hop", HACKING, Arrays.asList("bunny hop", "bhop", "bh")),
        SPEED(18, null, "Speed", HACKING, Collections.singletonList("speed")),
        SCAFFOLD(18, null, "Scaffold", HACKING, Collections.singletonList("scaffold")),
        ANTI_KNOCKBACK(18, null, "Anti Knockback", HACKING, Arrays.asList("antiknockback", "anti knockback", "antikb", "nokb", "modded knockback", "modded kb", "modified knockback")),
        REACH(18, null, "Reach", HACKING, Collections.singletonList("reach")),
        DOLPHIN(18, null, "Dolphin", HACKING, Collections.singletonList("dolphin")),
        FLY(18, null, "Fly", HACKING, Arrays.asList("fly", "flying", "flight", "glide")),
        WATER_WALKING(18, null, "Water Walking", HACKING, Arrays.asList("water walking", "jesus")),
        INAPPROPRIATE_NAME(-1, null, "Inappropriate Name", ReportType.INAPPROPRIATE_NAME, Arrays.asList("inappropriate name", "name", "bad name"));

        private final int defaultRule;
        private final Integer altRule;
        private final String name;
        private final ReportType type;
        private final List<String> aliases;
        ReportReason(int defaultRule, Integer altRule, String name, ReportType type, List<String> aliases) {
            this.defaultRule = defaultRule;
            this.name = name;
            this.type = type;
            this.aliases = aliases;
            this.altRule = altRule;
        }

        public int getDefaultRule() {
            return defaultRule;
        }

        public String getName() {
            return name;
        }

        public ReportType getType() {
            return type;
        }

        public List<String> getAliases() {
            return aliases;
        }

        public Integer getAltRule() {
            return altRule;
        }

        public static ReportReason getByAlias(String alias) {
            alias = alias.toLowerCase();
            for (ReportReason reason : ReportReason.values()) {
                if (reason.getAliases().contains(alias)) {
                    return reason;
                }
            }
            return null;
        }

        public static ReportReason getByName(String name) {
            for (ReportReason reason : ReportReason.values()) {
                if (reason.getName().equalsIgnoreCase(name)) {
                    return reason;
                }
            }
            return null;
        }
    }
}
