/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Denmark extends Banner {

    public static List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("WHITE", "CROSS"));
        patterns.add(new Item.Pattern("WHITE", "CROSS"));
        patterns.add(new Item.Pattern("WHITE", "CROSS"));
        patterns.add(new Item.Pattern("WHITE", "CROSS"));
    }

    public Denmark() {
        super(7, "Denmark", "&c&lDen&f&lmark", "&c&lDan&f&lmark", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", patterns, "RED", true, Rarity.COMMON);
    }
}
