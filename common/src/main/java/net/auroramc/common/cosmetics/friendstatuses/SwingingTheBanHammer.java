/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;

public class SwingingTheBanHammer extends FriendStatus {

    public SwingingTheBanHammer() {
        super(111, "&6&k0&r &9Swinging the Ban Hammer &6&k0&r", "&6&k0&r &9&lSwinging the Ban Hammer &6&k0&r", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MODERATOR), "", "&6&k0&r &9&lSwinging the Ban Hammer &6&k0&r", '9', false, Rarity.MYTHICAL);
    }
}
