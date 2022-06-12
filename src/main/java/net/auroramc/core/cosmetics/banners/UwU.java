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

public class UwU extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.PINK, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.PINK, PatternType.SKULL));
        patterns.add(new Pattern(DyeColor.PINK, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.PINK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.PINK, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.PINK, PatternType.BORDER));
    }

    public UwU() {
        super(22, "UwU", "&d&lUwU", "&dI wuv wou", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLACK, true, Rarity.EPIC);
    }
}
