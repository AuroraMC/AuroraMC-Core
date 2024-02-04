/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.List;

public class Running extends ChatEmote {
    public Running() {
        super(1116, "Running", "running", "┌( `ー´)┘", ChatColor.RED, false, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.EPIC);
    }
}
