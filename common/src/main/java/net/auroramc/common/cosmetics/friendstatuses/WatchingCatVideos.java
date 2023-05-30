/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class WatchingCatVideos extends FriendStatus {
    public WatchingCatVideos() {
        super(122, "&dWatching Cat Videos", "&dWatching Cat Videos", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at;&cstore.auroramc.net to unlock this friend status!", "&d&lWatching Cat Videos", 'd', true, Rarity.EPIC);
    }
}
