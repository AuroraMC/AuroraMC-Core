/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class BunniWabbit extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("SILVER", "CIRCLE_MIDDLE"));
        patterns.add(new Item.Pattern("WHITE", "FLOWER"));
        patterns.add(new Item.Pattern("LIGHT_BLUE", "TRIANGLE_TOP"));
        patterns.add(new Item.Pattern("WHITE", "CROSS"));
        patterns.add(new Item.Pattern("LIGHT_BLUE", "CURLY_BORDER"));
        patterns.add(new Item.Pattern("WHITE", "TRIANGLES_BOTTOM"));
    }

    public BunniWabbit() {
        super(13, "Bunni Wabbit", "&b&lBunni Wabbit", "&6I wike cawwots.", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "WHITE", true, Rarity.UNCOMMON);
    }
}
