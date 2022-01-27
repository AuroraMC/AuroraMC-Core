/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Germany extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
    }

    public Germany() {
        super(4, "Germany", "&c&lGermany", "&c&lDeutschland", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, DyeColor.RED, true);
    }
}
