/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.punishments.ipprofiles;

import java.util.List;

public class ProfileComparison {

    private final String firstUser;
    private final String secondUser;
    private final int amountOfCommonAlts;
    private final List<String> commonAlts;
    private final int amountOfCommonProfiles;
    private final List<String> commonProfiles;
    private final int bans;
    private final int mutes;
    private final int firstProfile;
    private final int secondProfile;

    public ProfileComparison(String firstUser, String secondUser, int amountOfCommonAlts, List<String> commonAlts, int amountOfCommonProfiles, List<String> commonProfiles, int bans, int mutes, int firstProfile, int secondProfile) {
        this.firstProfile = firstProfile;
        this.secondProfile = secondProfile;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.amountOfCommonAlts = amountOfCommonAlts;
        this.amountOfCommonProfiles = amountOfCommonProfiles;
        this.bans = bans;
        this.mutes = mutes;
        this.commonAlts = commonAlts;
        this.commonProfiles = commonProfiles;
    }

    public int getBans() {
        return bans;
    }

    public int getMutes() {
        return mutes;
    }

    public int getAmountOfCommonAlts() {
        return amountOfCommonAlts;
    }

    public int getAmountOfCommonProfiles() {
        return amountOfCommonProfiles;
    }

    public int getFirstProfile() {
        return firstProfile;
    }

    public int getSecondProfile() {
        return secondProfile;
    }

    public List<String> getCommonAlts() {
        return commonAlts;
    }

    public List<String> getCommonProfiles() {
        return commonProfiles;
    }

    public String getFirstUser() {
        return firstUser;
    }

    public String getSecondUser() {
        return secondUser;
    }
}
