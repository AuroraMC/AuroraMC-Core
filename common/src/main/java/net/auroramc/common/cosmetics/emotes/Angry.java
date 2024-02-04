/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.List;

public class Angry extends ChatEmote {
    public Angry() {
        super(1117, "Angry", "angry", "(⩺_⩹)", ChatColor.RED, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.LEGENDARY);
    }
}
