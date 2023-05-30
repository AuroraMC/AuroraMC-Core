/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class France extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("RED", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("RED", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("RED", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("BLUE", "STRIPE_RIGHT"));
        patterns.add(new Item.Pattern("BLUE", "STRIPE_RIGHT"));
        patterns.add(new Item.Pattern("BLUE", "STRIPE_RIGHT"));
    }

    public France() {
        super(3, "France", "&c&lFr&f&lan&9&lce", "&c&lFr&f&lanc&9&lais", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, "WHITE", true, Rarity.COMMON);
    }
}
