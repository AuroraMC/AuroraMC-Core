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

public class Star extends ChatEmote {
    public Star() {
        super(1110, "Star", "star", "✦", ChatColor.YELLOW, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.EPIC);
    }
}
