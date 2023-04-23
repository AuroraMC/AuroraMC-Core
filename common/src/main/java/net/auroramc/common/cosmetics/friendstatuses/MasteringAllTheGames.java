/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;

public class MasteringAllTheGames extends FriendStatus {

    public MasteringAllTheGames() {
        super(106, "Mastering all the games", "Mastering all the games", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MASTER), "Purchase Master at store.auroramc.net to unlock this status.", "Mastering all the games", 'd', true, Rarity.RARE);
    }
}
