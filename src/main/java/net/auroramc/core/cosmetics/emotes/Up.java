/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.emotes;

import net.auroramc.core.api.cosmetics.ChatEmote;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

public class Up extends ChatEmote {
    public Up() {
        super(1104, "Up", "up", "⬆", ChatColor.BLUE, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.UNCOMMON);
    }
}
