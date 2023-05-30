/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
