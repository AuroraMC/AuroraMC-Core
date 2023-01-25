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

public class Unflip extends ChatEmote {
    public Unflip() {
        super(1108, "Unflip", "unflip", "┬─┬ノ( º _ ºノ)", ChatColor.AQUA, false, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.LEGENDARY);
    }
}
