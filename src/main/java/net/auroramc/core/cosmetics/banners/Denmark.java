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

public class Denmark extends Banner {

    public static List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CROSS));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CROSS));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CROSS));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CROSS));
    }

    public Denmark() {
        super(7, "Denmark", "&c&lDen&f&lmark", "", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, DyeColor.RED, true);
    }
}
