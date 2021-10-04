package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class France extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.STRIPE_RIGHT));
    }

    public France() {
        super(3, "France", "&c&lFr&f&lan&9&lce", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }
}
