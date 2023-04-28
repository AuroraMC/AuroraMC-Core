/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class UnitedKingdom extends Banner {

    private static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("WHITE", "STRIPE_DOWNLEFT"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_DOWNRIGHT"));
        patterns.add(new Item.Pattern("RED", "CROSS"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("RED", "STRAIGHT_CROSS"));
    }

    public UnitedKingdom() {
        super(0, "United Kingdom Flag", "&c&lUnited Kingdom Flag", "&c&lRULE BRITANIA!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "BLUE", true, Rarity.COMMON);
    }
}
