/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poland extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("RED", "HALF_HORIZONTAL_MIRROR"));
    }

    public Poland() {
        super(2, "Poland", "&f&lPol&c&land", "&f&lPol&c&lska", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, "WHITE", true, Rarity.COMMON);
    }
}
