package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class CandyCane extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        // [{Pattern:ts,Color:15},{Pattern:mr,Color:1},{Pattern:dls,Color:15},{Pattern:ss,Color:1},{Pattern:cbo,Color:1},{Pattern:bo,Color:1}]}}
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_SMALL));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CURLY_BORDER));
        patterns.add(new Pattern(DyeColor.RED, PatternType.BORDER));
    }

    public CandyCane() {
        super(11, "Candy Cane", "&cCandy&r &fCane", "&cMmmm tasty...", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
