/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class Derp extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();

        patterns.add(new Item.Pattern("BLACK", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("YELLOW", "STRAIGHT_CROSS"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("PINK", "SQUARE_BOTTOM_RIGHT"));
        patterns.add(new Item.Pattern("YELLOW", "BORDER"));
    }

    public Derp() {
        super(20, "Derp", "&e&lDerp", "&eDerpity Derpity Derp Derp", UnlockMode.CRATE, 1000, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "WHITE", true, Rarity.LEGENDARY);
    }
}
