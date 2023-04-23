/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;

public class Shrug extends ChatEmote {
    public Shrug() {
        super(1100, "Shrug", "shrug", "¯\\_(ツ)_/¯", ChatColor.AQUA, false, UnlockMode.ALL, Collections.emptyList(), Collections.emptyList(), Rarity.COMMON);
    }
}
