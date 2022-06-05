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

public class AuroraMC extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.BORDER));
    }

    public AuroraMC() {
        super(8, "AuroraMC?", "&3&lAuroraMC?", "&3It's the best we could do, okay?", UnlockMode.STORE_PURCHASE, -1, new ArrayList<>(), new ArrayList<>(), "Purchase the AuroraMC Starter Pack at;&cstore.auroramc.net to unlock this banner!", patterns, DyeColor.WHITE, true, Rarity.EPIC);
    }
}
