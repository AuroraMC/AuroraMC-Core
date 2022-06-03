/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Permission;

import java.util.Collections;

public class Programming extends FriendStatus {

    public Programming() {
        super(112, "&2&k0&r &adeveloper.setProgramming(true) &2&k0&r", "&2&k0&r &a&ldeveloper.setProgramming(true); &2&k0&r", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.DEBUG_INFO), Collections.emptyList(), "", "&2&k0&r &a&ldeveloper.setProgramming(true); &2&k0&r", 'a', false, Rarity.MYTHICAL);
    }
}
