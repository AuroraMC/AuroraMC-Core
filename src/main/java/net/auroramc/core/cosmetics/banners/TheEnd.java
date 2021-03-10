package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class TheEnd extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.MAGENTA, PatternType.STRAIGHT_CROSS));
        patterns.add(new Pattern(DyeColor.PURPLE, PatternType.SKULL));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP));
    }

    public TheEnd() {
        super(7, "The End", "&5&lThe End", "&5He wants your blocks...", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLACK, true);
    }
}