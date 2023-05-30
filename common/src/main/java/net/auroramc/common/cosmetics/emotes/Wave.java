/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.List;

public class Wave extends ChatEmote {
    public Wave() {
        super(1112, "Wave", "wave", "ヾ(＾∇＾)", ChatColor.GREEN, false, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.RARE);
    }
}
