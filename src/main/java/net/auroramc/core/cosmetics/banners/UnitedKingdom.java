/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class UnitedKingdom extends Banner {

    private static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CROSS));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRAIGHT_CROSS));
    }

    public UnitedKingdom() {
        super(0, "United Kingdom Flag", "&c&lUnited Kingdom Flag", "&c&lRULE BRITANIA!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLUE, true, Rarity.COMMON);
    }
}
