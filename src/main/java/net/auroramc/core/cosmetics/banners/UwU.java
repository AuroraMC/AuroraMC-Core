package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.List;

public class UwU extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public UwU() {
        super(17, "UwU", "&dUwU", "&dI wuv wou", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
