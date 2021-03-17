package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class Pirate extends Banner {

    private static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.SKULL));
    }

    public Pirate() {
        super(2, "Pirate", "&f&lPirate Banner", "&fShiver me timbers!", UnlockMode.ALL, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLACK, true);
    }
}
