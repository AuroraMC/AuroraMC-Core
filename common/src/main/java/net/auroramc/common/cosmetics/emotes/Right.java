/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.List;

public class Right extends ChatEmote {
    public Right() {
        super(1105, "Right", "right", "➡", ChatColor.BLUE, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.UNCOMMON);
    }
}
