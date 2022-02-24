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

public class Belgium extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT));
    }

    public Belgium() {
        super(5, "Belgium", "&e&lBelgium", "&e&lBelgium", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, DyeColor.RED, true, Rarity.COMMON);
    }
}
