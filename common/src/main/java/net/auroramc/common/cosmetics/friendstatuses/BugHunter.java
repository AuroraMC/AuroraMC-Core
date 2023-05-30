/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class BugHunter extends FriendStatus {

    public BugHunter() {
        super(123, "&e&k00&r &2Bug Hunter &e&k00&r", "&e&l&k00&r &2&lBug Hunter &e&l&k00&r", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "", "&e&l&k00&r &2&lBug Hunter &e&l&k00&r", '2', false, Rarity.MYTHICAL);
    }
}
