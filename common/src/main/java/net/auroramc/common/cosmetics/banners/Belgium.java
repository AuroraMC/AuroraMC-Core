/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Belgium extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("BLACK", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_RIGHT"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_RIGHT"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_RIGHT"));
        patterns.add(new Item.Pattern("YELLOW", "STRIPE_RIGHT"));
    }

    public Belgium() {
        super(5, "Belgium", "&e&lBelgium", "&e&lBelgium", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, "RED", true, Rarity.COMMON);
    }
}
