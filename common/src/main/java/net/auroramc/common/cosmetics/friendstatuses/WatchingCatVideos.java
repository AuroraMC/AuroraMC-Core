/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class WatchingCatVideos extends FriendStatus {
    public WatchingCatVideos() {
        super(122, "&dWatching Cat Videos", "&dWatching Cat Videos", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at;&cstore.auroramc.net to unlock this friend status!", "&d&lWatching Cat Videos", 'd', true, Rarity.EPIC);
    }
}
