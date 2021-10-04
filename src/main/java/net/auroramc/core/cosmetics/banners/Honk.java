package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class Honk extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.ORANGE, PatternType.HALF_HORIZONTAL_MIRROR));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        patterns.add(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
    }

    public Honk() {
        super(11, "Honk", "&6&lHonk", "&6Honk.", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLACK, true);
    }
}
