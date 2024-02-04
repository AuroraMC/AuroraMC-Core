/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.List;

public class Up extends ChatEmote {
    public Up() {
        super(1104, "Up", "up", "⬆", ChatColor.BLUE, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.UNCOMMON);
    }
}
