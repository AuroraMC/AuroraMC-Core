package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poland extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR));
    }

    public Poland() {
        super(2, "Poland", "&f&lPol&c&land", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }
}
