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

public class Unflip extends ChatEmote {
    public Unflip() {
        super(1108, "Unflip", "unflip", "┬─┬ノ( º _ ºノ)", ChatColor.AQUA, false, UnlockMode.CRATE, Collections.emptyList(), Collections.emptyList(), Rarity.LEGENDARY);
    }
}
