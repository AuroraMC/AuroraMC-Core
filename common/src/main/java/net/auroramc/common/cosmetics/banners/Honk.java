/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class Honk extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("WHITE", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("ORANGE", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_BOTTOM"));
        patterns.add(new Item.Pattern("WHITE", "BORDER"));
        patterns.add(new Item.Pattern("ORANGE", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_TOP"));
    }

    public Honk() {
        super(11, "Honk", "&6&lHonk", "&6Honk.", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "BLACK", true, Rarity.EPIC);
    }
}
