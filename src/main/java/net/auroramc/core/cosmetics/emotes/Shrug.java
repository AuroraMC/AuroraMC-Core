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

public class Shrug extends ChatEmote {
    public Shrug() {
        super(1100, "Shrug", "shrug", "¯\\_(ツ)_/¯", ChatColor.AQUA, false, UnlockMode.ALL, Collections.emptyList(), Collections.emptyList(), Rarity.COMMON);
    }
}
