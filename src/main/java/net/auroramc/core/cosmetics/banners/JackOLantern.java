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

public class JackOLantern extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.TRIANGLES_TOP));
    }

    public JackOLantern() {
        super(17, "Jack-O-Lantern", "&6&lJack-O&8&l-Lantern", "&8Watch out for ghosts and ghouls!", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Spooky Crates", patterns, DyeColor.ORANGE, true, Rarity.RARE);
    }
}
