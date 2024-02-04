/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.Item;

import java.util.*;

public class Plus extends Banner {
    public static final List<Item.Pattern> patterns;

    static {

        //Basic pattern for the GUI
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("CYAN", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("CYAN", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("WHITE", "BORDER"));
    }

    public Plus() {
        super(21, "Plus", "&3&lPlus", "&3Show of your swaggy plus status. Changes based on your chosen plus colour", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), new ArrayList<>(), "Purchase Plus to unlock this banner!", patterns, "WHITE", true, Rarity.UNCOMMON);
    }
}
