package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class TheGoose extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.ORANGE, PatternType.MOJANG));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_DOWNRIGHT));
        patterns.add(new Pattern(DyeColor.CYAN, PatternType.HALF_VERTICAL_MIRROR));
    }

    public TheGoose() {
        super(5, "The Goose", "&f&lThe&r &6&lGoose", "&fPeace was never an option.", UnlockMode.ALL, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.CYAN, true);
    }
}
