package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Banner;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Plus extends Banner {

    public static final List<Pattern> patterns;

    static {
        patterns = new ArrayList<>();
    }

    public Plus() {
        super(16, "Plus", "&3Plus", "&3Show of your swaggy plus status. Changes based on your chosen plus colour", UnlockMode.PERMISSION, -1, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("plus"))), new ArrayList<>(), patterns, DyeColor.BLACK);
    }
}
