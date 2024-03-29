/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;

import java.util.ArrayList;
import java.util.List;

public class AngryCraig extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("LIGHT_BLUE", "RHOMBUS_MIDDLE"));
        patterns.add(new Item.Pattern("SILVER", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("GRAY", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("SILVER", "BORDER"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("SILVER", "HALF_HORIZONTAL"));
    }

    public AngryCraig() {
        super(9, "Angry Craig", "&8&lAngry Craig", "&8Don't mess with him!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "WHITE", true, Rarity.RARE);
    }
}
