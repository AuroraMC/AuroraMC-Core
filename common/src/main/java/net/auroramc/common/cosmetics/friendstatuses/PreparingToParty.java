/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class PreparingToParty extends FriendStatus {

    public PreparingToParty() {
        super(124, "&5&k0&r &4P&cr&6e&ep&aa&br&3i&9n&dg &4t&co &6p&ea&ar&bt&3y &5&k0&r", "&5&k0&r &4&lP&c&lr&6&le&e&lp&a&la&b&lr&3&li&9&ln&d&lg &4&lt&c&lo &6&lp&e&la&a&lr&b&lt&3&ly &5&k0&r", UnlockMode.STORE_PURCHASE, 0, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at;&cstore.auroramc.net to unlock this friend status!", "&5&k0&r &4P&cr&6e&ep&aa&br&3i&9n&dg &4t&co &6p&ea&ar&bt&3y &5&k0&r", '4', true, Rarity.LEGENDARY);
    }
}
