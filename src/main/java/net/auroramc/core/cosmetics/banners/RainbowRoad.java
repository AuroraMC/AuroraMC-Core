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

public class RainbowRoad extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL));
        patterns.add(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.PURPLE, PatternType.TRIANGLES_BOTTOM));
        patterns.add(new Pattern(DyeColor.RED, PatternType.TRIANGLES_TOP));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.BRICKS));
    }

    public RainbowRoad() {
        super(24, "Rainbow Road", "&1&lR&2&la&3&li&4&ln&5&lb&6&lo&7&lw&r &8&lR&9&lo&d&la&e&ld", "&9♫ Do do do do do do do do do do ♫", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.LIGHT_BLUE, true, Rarity.LEGENDARY);
    }
}
