/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.punishments.ipprofiles;

import java.util.List;

public class IPProfile {

    private final int id;
    private final int numberOfAccounts;
    private final List<String> accounts;
    private final String lastUsedBy;
    private final long lastUsedAt;
    private final int bans;
    private final int mutes;
    private final boolean globalAccountSuspended;
    private final String globalAccountSuspensionReason;

    public IPProfile(int id, int numberOfAccounts, List<String> accounts, String lastUsedBy, long lastUsedAt, int bans, int mutes, boolean globalAccountSuspended, String globalAccountSuspensionReason) {
        this.id = id;
        this.numberOfAccounts = numberOfAccounts;
        this.accounts = accounts;
        this.lastUsedBy = lastUsedBy;
        this.lastUsedAt = lastUsedAt;
        this.bans = bans;
        this.mutes = mutes;
        this.globalAccountSuspended = globalAccountSuspended;
        this.globalAccountSuspensionReason = globalAccountSuspensionReason;
    }

    public int getId() {
        return id;
    }

    public boolean isGlobalAccountSuspended() {
        return globalAccountSuspended;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public long getLastUsedAt() {
        return lastUsedAt;
    }

    public String getGlobalAccountSuspensionReason() {
        return globalAccountSuspensionReason;
    }

    public String getLastUsedBy() {
        return lastUsedBy;
    }

    public int getBans() {
        return bans;
    }

    public int getMutes() {
        return mutes;
    }

    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }
}

