package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class BunniWabbit extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.SILVER, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.FLOWER));
        patterns.add(new Pattern(DyeColor.LIGHT_BLUE, PatternType.TRIANGLE_TOP));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.CROSS));
        patterns.add(new Pattern(DyeColor.LIGHT_BLUE, PatternType.CURLY_BORDER));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM));
    }

    public BunniWabbit() {
        super(13, "Bunni Wabbit", "&b&lBunni Wabbit", "&6I wike cawwots.", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }
}
