package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.List;

public class RainbowRoad extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public RainbowRoad() {
        super(19, "Rainbow Road", "&1R&2a&3i&4n&5b&6o&7w&r &8R&9o&da&ed", "&9♫ Do do do do do do do do do do ♫", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
