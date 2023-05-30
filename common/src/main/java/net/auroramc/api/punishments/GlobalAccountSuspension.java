/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.punishments;

import java.util.List;

public class GlobalAccountSuspension {

    private final String code;
    private final String rootAccount;
    private final String reason;
    private final List<String> bannedProfiles;
    private final int issuer;
    private final long timestamp;

    public GlobalAccountSuspension(String code, String rootAccount, String reason, List<String> bannedProfiles, int issuer, long timestamp) {
        this.code = code;
        this.rootAccount = rootAccount;
        this.reason = reason;
        this.bannedProfiles = bannedProfiles;
        this.issuer = issuer;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<String> getBannedProfiles() {
        return bannedProfiles;
    }

    public String getCode() {
        return code;
    }

    public int getIssuer() {
        return issuer;
    }

    public String getReason() {
        return reason;
    }

    public String getRootAccount() {
        return rootAccount;
    }
}
