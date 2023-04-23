/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class TheEnd extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("MAGENTA", "STRAIGHT_CROSS"));
        patterns.add(new Item.Pattern("PURPLE", "SKULL"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("BLACK", "CREEPER"));
        patterns.add(new Item.Pattern("BLACK", "TRIANGLE_TOP"));
    }

    public TheEnd() {
        super(12, "The End", "&5&lThe End", "&5He wants your blocks...", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "BLACK", true, Rarity.RARE);
    }
}