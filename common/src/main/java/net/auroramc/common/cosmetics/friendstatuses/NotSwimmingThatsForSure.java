/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;
import java.util.List;

public class NotSwimmingThatsForSure extends FriendStatus {
    public NotSwimmingThatsForSure() {
        super(125, "&5&oNot swimming... that's for sure.", "&5&oNot swimming... that's for sure.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", "&5&oNot swimming... that's for sure.", '5', true, Rarity.EPIC);
    }
}
