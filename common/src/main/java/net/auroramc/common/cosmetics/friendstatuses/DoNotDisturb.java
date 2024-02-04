/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class DoNotDisturb extends FriendStatus {

    public DoNotDisturb() {
        super(102, "Do Not Disturb", "Do Not Disturb", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Do Not Disturb", 'c', true, Rarity.COMMON);
    }
}
