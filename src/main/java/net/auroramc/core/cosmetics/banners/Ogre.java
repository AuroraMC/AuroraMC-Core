package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.List;

public class Ogre extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public Ogre() {
        super(13, "Ogre", "&2Ogre", "&2He's gonna get you!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, DyeColor.BLACK);
    }
}
