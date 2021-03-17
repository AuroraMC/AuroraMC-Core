package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class NinjaMonkey extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BROWN, PatternType.CREEPER));
        patterns.add(new Pattern(DyeColor.BROWN, PatternType.FLOWER));
    }

    public NinjaMonkey() {
        super(18, "Ninja Monkey", "&4&lNinja Monkey", "&4Watch out!", UnlockMode.ALL, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.WHITE, true);
    }
}
