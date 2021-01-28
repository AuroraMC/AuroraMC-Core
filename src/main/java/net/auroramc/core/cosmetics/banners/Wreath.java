package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class Wreath extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.RED, PatternType.SKULL));
        patterns.add(new Pattern(DyeColor.GREEN, PatternType.FLOWER));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CIRCLE_MIDDLE));
    }

    public Wreath() {
        super(10, "Wreath", "&2Wreath", "&2♫ It's beginning to look a lot like Christmas! ♫", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
