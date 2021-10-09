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

public class AngryCraig extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.LIGHT_BLUE, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.SILVER, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.SILVER, PatternType.BORDER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.SILVER, PatternType.HALF_HORIZONTAL));
    }

    public AngryCraig() {
        super(9, "Angry Craig", "&8&lAngry Craig", "&8Don't mess with him!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }
}
