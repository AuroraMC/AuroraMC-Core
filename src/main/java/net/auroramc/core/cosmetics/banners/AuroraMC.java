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
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.BORDER));
    }

    public AuroraMC() {
        super(3, "AuroraMC?", "&3AuroraMC?", "&3It's the best we could do, okay?", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.WHITE);
    }
}
