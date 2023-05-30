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

public class Japan extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("RED", "CIRCLE_MIDDLE"));
        patterns.add(new Item.Pattern("RED", "CIRCLE_MIDDLE"));
        patterns.add(new Item.Pattern("RED", "CIRCLE_MIDDLE"));
    }

    public Japan() {
        super(6, "Japan", "&f&lJa&c&lpan", "&f&l日&c&l本", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, "WHITE", true, Rarity.COMMON);
    }

}
