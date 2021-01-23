package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.List;

public class CandyCane extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public CandyCane() {
        super(11, "Candy Cane", "&cCandy&r &fCane", "&cMmmm tasty...", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
