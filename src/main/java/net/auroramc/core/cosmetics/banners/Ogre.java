package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class Ogre extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.CURLY_BORDER));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP));
    }

    public Ogre() {
        super(13, "Ogre", "&2&lOgre", "&2He's gonna get you!", UnlockMode.ALL, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLACK, true);
    }
}
