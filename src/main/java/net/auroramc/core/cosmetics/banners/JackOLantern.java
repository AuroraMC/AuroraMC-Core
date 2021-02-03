package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.List;

public class JackOLantern extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public JackOLantern() {
        super(12, "Jack-O-Lantern", "&6Jack-O&8-Lantern", "&8Watch out for ghosts and ghouls!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Spooky Crates", patterns, DyeColor.BLACK);
    }
}
