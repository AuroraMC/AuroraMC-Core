/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class Wreath extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("YELLOW", "CREEPER"));
        patterns.add(new Item.Pattern("RED", "SKULL"));
        patterns.add(new Item.Pattern("GREEN", "FLOWER"));
        patterns.add(new Item.Pattern("BLACK", "CIRCLE_MIDDLE"));
        patterns.add(new Item.Pattern("BLACK", "CIRCLE_MIDDLE"));
        patterns.add(new Item.Pattern("BLACK", "CIRCLE_MIDDLE"));
    }

    public Wreath() {
        super(15, "Wreath", "&2&lWreath", "&2♫ It's beginning to look a lot like &2Christmas! ♫", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "BLACK", true, Rarity.RARE);
    }
}
