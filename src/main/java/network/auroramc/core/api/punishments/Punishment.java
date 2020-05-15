package network.auroramc.core.api.punishments;

import org.bukkit.scheduler.BukkitTask;

public class Punishment {

    private final String punishmentCode;
    private final int punished;
    private final int ruleID;
    private final String extraNotes;
    private final int punisher;
    private final long issued;
    private final long expire;
    private final int status;
    private final String evidence;
    private final int suffix;
    private final String removalReason;
    private final String remover;
    private BukkitTask task;
    private String punisherName;
    private long removalTimestamp;
    private String punishedName;


    public Punishment(String punishmentCode, int punished, int ruleID, String extraNotes, int punisher, long issued, long expire, int status, String evidence, int suffix, String removalReason, String remover, long removalTimestamp, String punisherName) {
        this.punishmentCode = punishmentCode;
        this.punished = punished;
        this.ruleID = ruleID;
        this.extraNotes = extraNotes;
        this.punisher = punisher;
        this.issued = issued;
        this.expire = expire;
        this.status = status;
        this.evidence = evidence;
        this.suffix = suffix;
        this.removalReason = removalReason;
        this.remover = remover;
        this.punisherName = punisherName;
        this.removalTimestamp = removalTimestamp;
    }

    public Punishment(String punishmentCode, int punished, int ruleID, String extraNotes, int punisher, long issued, long expire, int status, String evidence, int suffix, String removalReason, String remover, long removalTimestamp, String punisherName, String punishedName) {
        this.punishmentCode = punishmentCode;
        this.punished = punished;
        this.ruleID = ruleID;
        this.extraNotes = extraNotes;
        this.punisher = punisher;
        this.issued = issued;
        this.expire = expire;
        this.status = status;
        this.evidence = evidence;
        this.suffix = suffix;
        this.removalReason = removalReason;
        this.remover = remover;
        this.punisherName = punisherName;
        this.removalTimestamp = removalTimestamp;
        this.punishedName = punishedName;
    }

    public int getRuleID() {
        return ruleID;
    }

    public int getPunished() {
        return punished;
    }

    public int getPunisher() {
        return punisher;
    }

    public String getRemover() {
        return remover;
    }

    public int getStatus() {
        return status;
    }

    public int getSuffix() {
        return suffix;
    }

    public long getExpire() {
        return expire;
    }

    public long getIssued() {
        return issued;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public String getPunishmentCode() {
        return punishmentCode;
    }

    public String getRemovalReason() {
        return removalReason;
    }

    public BukkitTask getTask() {
        return task;
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    public String getPunisherName() {
        return punisherName;
    }

    public long getRemovalTimestamp() {
        return removalTimestamp;
    }

    public String getPunishedName() {
        return punishedName;
    }
}
