package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.List;

public class Derp extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public Derp() {
        super(15, "Derp", "&eDerp", "&eDerpity Derpity Derp Derp", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
