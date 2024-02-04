/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.api.utils;


import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;

public enum PanelAccountType {

    OWNER(Rank.OWNER, null),
    SR_DEV(Rank.SENIOR_DEVELOPER, null),
    DEV(Rank.DEVELOPER, null),
    ADMIN(Rank.ADMIN, null),
    RC(null, SubRank.RULES_COMMITTEE),
    STAFF(null, SubRank.STAFF_MANAGEMENT),
    APPEALS(null, SubRank.APPEALS),
    QA(null, SubRank.QUALITY_ASSURANCE_MANAGEMENT);

    private final Rank rank;
    private final SubRank subrank;

    PanelAccountType(Rank rank, SubRank subrank) {
        this.rank = rank;
        this.subrank = subrank;
    }

    public Rank getRank() {
        return rank;
    }

    public SubRank getSubrank() {
        return subrank;
    }
}
