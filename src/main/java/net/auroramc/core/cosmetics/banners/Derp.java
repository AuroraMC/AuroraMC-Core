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

public class Derp extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRAIGHT_CROSS));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.PINK, PatternType.SQUARE_BOTTOM_RIGHT));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.BORDER));
    }

    public Derp() {
        super(20, "Derp", "&e&lDerp", "&eDerpity Derpity Derp Derp", UnlockMode.CRATE, 1000, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.WHITE, true, Rarity.LEGENDARY);
    }
}
