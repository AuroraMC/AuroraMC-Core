/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.punishments.ipprofiles;

import java.util.List;

public class PlayerProfile {

    private final String playerName;
    private final int numberOfProfiles;
    private final List<String> profiles;
    private final int latestProfile;
    private final String lastUsedBy;
    private final int sharedAccounts;
    private final List<String> mostRecentAlts;
    private final int bans;
    private final int mutes;
    private final boolean globalAccountSuspended;
    private final String globalAccountSuspensionReason;

    public PlayerProfile(String playerName, int numberOfProfiles, List<String> profiles, int latestProfile, String lastUsedBy, int sharedAccounts, List<String> mostRecentAlts, int bans, int mutes, boolean globalAccountSuspended, String globalAccountSuspensionReason) {
        this.playerName = playerName;
        this.numberOfProfiles = numberOfProfiles;
        this.profiles = profiles;
        this.lastUsedBy = lastUsedBy;
        this.latestProfile = latestProfile;
        this.sharedAccounts = sharedAccounts;
        this.mostRecentAlts = mostRecentAlts;
        this.bans = bans;
        this.mutes = mutes;
        this.globalAccountSuspended = globalAccountSuspended;
        this.globalAccountSuspensionReason = globalAccountSuspensionReason;
    }

    public int getMutes() {
        return mutes;
    }

    public int getBans() {
        return bans;
    }

    public String getGlobalAccountSuspensionReason() {
        return globalAccountSuspensionReason;
    }

    public boolean isGlobalAccountSuspended() {
        return globalAccountSuspended;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLatestProfile() {
        return latestProfile;
    }

    public int getSharedAccounts() {
        return sharedAccounts;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public String getLastUsedBy() {
        return lastUsedBy;
    }

    public int getNumberOfProfiles() {
        return numberOfProfiles;
    }

    public List<String> getMostRecentAlts() {
        return mostRecentAlts;
    }
}
