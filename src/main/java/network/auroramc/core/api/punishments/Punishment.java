package network.auroramc.core.api.punishments;

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
    private final int remover;


    public Punishment(String punishmentCode, int punished, int ruleID, String extraNotes, int punisher, long issued, long expire, int status, String evidence, int suffix, String removalReason, int remover) {
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

    public int getRemover() {
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
}
