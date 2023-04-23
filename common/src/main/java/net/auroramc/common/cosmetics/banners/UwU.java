/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class UwU extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("PINK", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("PINK", "SKULL"));
        patterns.add(new Item.Pattern("PINK", "HALF_HORIZONTAL_MIRROR"));
        patterns.add(new Item.Pattern("PINK", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("PINK", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("PINK", "BORDER"));
    }

    public UwU() {
        super(22, "UwU", "&d&lUwU", "&dI wuv wou", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "BLACK", true, Rarity.EPIC);
    }
}
