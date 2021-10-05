package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Japan extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE));
    }

    public Japan() {
        super(6, "Japan", "&f&lJa&c&lpan", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }

}
