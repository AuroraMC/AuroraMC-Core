package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class Blitzen extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BRICKS));
        patterns.add(new Pattern(DyeColor.BROWN, PatternType.TRIANGLE_BOTTOM));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT));
        patterns.add(new Pattern(DyeColor.BROWN, PatternType.SQUARE_BOTTOM_RIGHT));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM));
    }

    public Blitzen() {
        super(9, "Blitzen", "&4&lBlitzen", "&4One of Santa's Reindeer", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Candy Crates", patterns, DyeColor.BLACK, true);
    }
}
