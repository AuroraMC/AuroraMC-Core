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

public class ScreamingGhost extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CROSS));
    }

    public ScreamingGhost() {
        super(19, "Screaming Ghost", "&8&lScreaming Ghost", "&8Ooooooooh! Spook!", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Spooky Crates", patterns, DyeColor.WHITE, true, Rarity.RARE);
    }
}
