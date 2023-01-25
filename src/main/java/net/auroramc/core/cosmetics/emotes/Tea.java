/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.emotes;

import net.auroramc.core.api.cosmetics.ChatEmote;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

public class Tea extends ChatEmote {
    public Tea() {
        super(1111, "Tea", "tea", "☕", ChatColor.AQUA, false, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.RARE);
    }
}
