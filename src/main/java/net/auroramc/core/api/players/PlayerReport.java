package net.auroramc.core.api.players;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.punishments.Rule;
import net.auroramc.core.api.utils.PunishUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static net.auroramc.core.api.players.PlayerReport.ReportType.*;

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
    private final UUID chatReportUUID;
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

    public void addReporter(int reporter) {
        reporters.add(reporter);
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().addReporter(id, reporter);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public void handle(AuroraMCPlayer player, ReportOutcome outcome, ReportReason acceptedReason, boolean alt) {
        this.outcome = outcome;
        this.reasonAccepted = acceptedReason;

        Rule rule;
        if (outcome == ReportOutcome.ACCEPTED) {
            if (acceptedReason != null) {
                rule = AuroraMCAPI.getRules().getRule(((alt)? acceptedReason.getAltRule() : acceptedReason.getDefaultRule()));
                PunishUtils.punishUser(suspect, suspectName, player, rule, acceptedReason.getName() + " [Report #" + id + "]");
            } else {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("KickPlayer");
                out.writeUTF(suspectName);
                out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Username Manager", "This username is blacklisted.\n" +
                        "\n" +
                        "This username has been deemed inappropriate and is therefore\n" +
                        "blacklisted from use on our network!\n\n" +
                        "In order to join, simply change your name!"));
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AuroraMCAPI.getDbManager().addUsernameBan(suspectName);
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            }
        }


        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().handleReport(id, outcome, reasonAccepted);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("ReportHandled");
                out.writeInt(id);
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());

        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "The report has been handled."));
    }

    public void forwardToLeadership(AuroraMCPlayer player) {
        this.handler = 0;
        this.handlerName = null;
        this.queue = QueueType.LEADERSHIP;
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().forwardReportToLeadership(id);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "The report has been forwarded to the Leadership Team."));
    }

    public void abort(AuroraMCPlayer player) {
        this.handler = 0;
        this.handlerName = null;
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().abortReport(id);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());

        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "The report has been aborted."));
    }

    public enum ReportReason {
        SPAMMING(1, 13, "Spamming", CHAT, Arrays.asList("spamming", "spam", "chat spam", "flooding", "chat flooding")),
        RUDENESS(2, null, "Rudeness", CHAT, Arrays.asList("rudeness", "gr", "general rudeness", "rude", "mean", "unkind")),
        TROLLING(18, null, "Trolling", CHAT, Arrays.asList("trolling", "troll")),
        FILTER_EVASION(3, 27, "Filter Evasion", CHAT, Arrays.asList("filter evasion", "filter", "filter bypass", "swearing", "fe")),
        HARASSMENT(4, null, "Harassment", CHAT, Arrays.asList("harassment", "harass")),
        REVEALING_PERSONAL_INFORMATION(6, null, "Revealing Personal Information", CHAT, Arrays.asList("revealing personal information", "rpi", "doxxing", "leaking", "dox")),
        MALICIOUS_THREATS(9, null, "Malicious Threats", CHAT, Arrays.asList("malicious threats", "mt", "threats", "threatening")),
        DISCRIMINATION(5, null, "Discrimination", CHAT, Arrays.asList("discrimination", "racist", "racism", "discrim")),
        ADVERTISEMENT(7, null, "Advertisement", CHAT, Arrays.asList("advertisement", "advertising")),
        BOT_SPAM(13, null, "Bot Spam", CHAT, Collections.singletonList("bot spam")),
        MAP_EXPLOITING(10, null, "Map Exploiting", MISC, Collections.singletonList("map exploiting")),
        BUG_EXPLOITING(11, null, "Bug Exploiting", MISC, Arrays.asList("bug exploiting", "exploiting")),
        COMPROMISED_ACCOUNT(13, null, "Compromised Account", MISC, Arrays.asList("compromised account", "compromised", "comp")),
        INAPPROPRIATE_SKIN(17, null, "Inappropriate Skin", MISC, Arrays.asList("inappropriate skin", "skin", "bad skin")),
        INAPPROPRIATE_CAPE(17, null, "Inappropriate Cape", MISC, Arrays.asList("inappropriate cape", "cape", "bad cape")),
        STAT_BOOSTING(14, null, "Stat Boosting", MISC, Arrays.asList("stat boosting", "stat", "stats")),
        KILL_AURA(16, null, "Kill Aura", HACKING, Arrays.asList("kill aura", "killaura", "ka", "killbot", "tpaura", "aura")),
        BUNNY_HOP(16, null, "Bunny Hop", HACKING, Arrays.asList("bunny hop", "bhop", "bh")),
        SPEED(16, null, "Speed", HACKING, Collections.singletonList("speed")),
        SCAFFOLD(16, null, "Scaffold", HACKING, Collections.singletonList("scaffold")),
        ANTI_KNOCKBACK(16, null, "Anti Knockback", HACKING, Arrays.asList("antiknockback", "anti knockback", "antikb", "nokb", "modded knockback", "modded kb", "modified knockback")),
        REACH(16, null, "Reach", HACKING, Collections.singletonList("reach")),
        DOLPHIN(16, null, "Dolphin", HACKING, Collections.singletonList("dolphin")),
        FLY(16, null, "Fly", HACKING, Arrays.asList("fly", "flying", "flight", "glide")),
        WATER_WALKING(16, null, "Water Walking", HACKING, Arrays.asList("water walking", "jesus")),
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
