/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Germany extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("BLACK", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_BOTTOM"));
    }

    public Germany() {
        super(4, "Germany", "&c&lGermany", "&c&lDeutschland", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, "RED", true, Rarity.COMMON);
    }
}
