/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class RainbowRoad extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("YELLOW", "HALF_HORIZONTAL"));
        patterns.add(new Item.Pattern("ORANGE", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("BLUE", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("LIME", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("PURPLE", "TRIANGLES_BOTTOM"));
        patterns.add(new Item.Pattern("RED", "TRIANGLES_TOP"));
        patterns.add(new Item.Pattern("WHITE", "BRICKS"));
    }

    public RainbowRoad() {
        super(24, "Rainbow Road", "&1&lR&2&la&3&li&4&ln&5&lb&6&lo&7&lw&r &8&lR&9&lo&d&la&e&ld", "&9♫ Do do do do do do do do do do ♫", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "LIGHT_BLUE", true, Rarity.LEGENDARY);
    }
}
