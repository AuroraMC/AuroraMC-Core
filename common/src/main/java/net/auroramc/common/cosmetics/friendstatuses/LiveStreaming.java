/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Permission;

import java.util.Collections;

public class LiveStreaming extends FriendStatus {

    public LiveStreaming() {
        super(108, "Live Streaming", "Live Streaming", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.SOCIAL), Collections.emptyList(), "", "Live Streaming", '5', false, Rarity.MYTHICAL);
    }
}
