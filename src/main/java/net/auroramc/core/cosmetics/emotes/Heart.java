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

public class Heart extends ChatEmote {
    public Heart() {
        super(1101, "Heart", "heart", "❤", ChatColor.LIGHT_PURPLE, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.RARE);
    }
}
