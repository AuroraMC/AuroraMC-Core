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

public class Yes extends ChatEmote {
    public Yes() {
        super(1102, "Yes", "yes", "✔", ChatColor.GREEN, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.UNCOMMON);
    }
}
