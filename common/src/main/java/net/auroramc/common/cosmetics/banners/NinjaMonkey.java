/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class NinjaMonkey extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("BROWN", "CREEPER"));
        patterns.add(new Item.Pattern("BROWN", "FLOWER"));
    }

    public NinjaMonkey() {
        super(23, "Ninja Monkey", "&4&lNinja Monkey", "&4Watch out!", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "WHITE", true, Rarity.EPIC);
    }
}
