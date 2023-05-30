/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
