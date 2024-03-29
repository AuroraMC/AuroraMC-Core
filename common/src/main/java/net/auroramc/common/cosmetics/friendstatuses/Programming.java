/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Permission;

import java.util.Collections;

public class Programming extends FriendStatus {

    public Programming() {
        super(112, "&2&k0&r &adeveloper.setProgramming(true) &2&k0&r", "&2&k0&r &a&ldeveloper.setProgramming(true); &2&k0&r", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.DEBUG_INFO), Collections.emptyList(), "", "&2&k0&r &a&ldeveloper.setProgramming(true); &2&k0&r", 'a', false, Rarity.MYTHICAL);
    }
}
