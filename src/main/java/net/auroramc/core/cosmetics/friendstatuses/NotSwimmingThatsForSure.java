/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

public class NotSwimmingThatsForSure extends FriendStatus {
    public NotSwimmingThatsForSure() {
        super(125, "&5&oNot swimming... that's for sure.", "&5&oNot swimming... that's for sure.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", "&5&oNot swimming... that's for sure.", '5', true, Rarity.EPIC);
    }
}
