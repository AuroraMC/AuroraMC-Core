/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.punishments;

public class Ban {

    private final String code, extraNotes;
    private final int ruleID, status;
    private final long issued, expire;

    public Ban(String code, int ruleID, long issued, long expire, int status, String extraNotes) {
        this.code = code;
        this.ruleID = ruleID;
        this.issued = issued;
        this.expire = expire;
        this.status = status;
        this.extraNotes = extraNotes;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public long getIssued() {
        return issued;
    }

    public long getExpire() {
        return expire;
    }

    public int getStatus() {
        return status;
    }

    public int getRuleID() {
        return ruleID;
    }

    public String getCode() {
        return code;
    }
}
