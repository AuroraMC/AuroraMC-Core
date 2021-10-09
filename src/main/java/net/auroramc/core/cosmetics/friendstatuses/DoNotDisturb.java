/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class DoNotDisturb extends FriendStatus {

    public DoNotDisturb() {
        super(102, "Do Not Disturb", "Do Not Disturb", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Do Not Disturb", 'c', true);
    }
}
