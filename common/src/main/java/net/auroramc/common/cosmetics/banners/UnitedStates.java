/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class UnitedStates extends Banner {

    private static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("WHITE", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("BLUE", "SQUARE_TOP_RIGHT"));
        patterns.add(new Item.Pattern("BLUE", "SQUARE_TOP_RIGHT"));
        patterns.add(new Item.Pattern("BLUE", "SQUARE_TOP_RIGHT"));
        patterns.add(new Item.Pattern("BLUE", "SQUARE_TOP_RIGHT"));
        patterns.add(new Item.Pattern("BLUE", "SQUARE_TOP_RIGHT"));
    }

    public UnitedStates() {
        super(1, "United States Flag", "&c&lUnited States Flag", "&cUSA! USA! USA!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "RED", true, Rarity.COMMON);
    }

}
