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

public class Hug extends ChatEmote {
    public Hug() {
        super(1114, "Hug", "hug", "⊂(◉‿◉)つ", ChatColor.LIGHT_PURPLE, true, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.RARE);
    }
}
