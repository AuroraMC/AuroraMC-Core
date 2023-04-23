/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.emotes;

import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.md_5.bungee.api.ChatColor;

import java.util.Collections;
import java.util.List;

public class HeartEyes extends ChatEmote {
    public HeartEyes() {
        super(1115, "Heart Eyes", "hearteyes", "(｡♥‿♥｡)", ChatColor.LIGHT_PURPLE, false, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.EPIC);
    }
}
