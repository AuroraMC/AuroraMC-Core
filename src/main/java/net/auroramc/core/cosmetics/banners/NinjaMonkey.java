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

public class NinjaMonkey extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BROWN, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.BROWN, PatternType.FLOWER));
    }

    public NinjaMonkey() {
        super(23, "Ninja Monkey", "&4&lNinja Monkey", "&4Watch out!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }
}
