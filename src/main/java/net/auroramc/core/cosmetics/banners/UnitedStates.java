package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class UnitedStates extends Banner {

    private static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_RIGHT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_RIGHT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_RIGHT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_RIGHT));
        patterns.add(new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_RIGHT));
    }

    public UnitedStates() {
        super(1, "United States Flag", "&c&lUnited States Flag", "&cUSA! USA! USA!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.RED, true);
    }

}
