/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;

import java.util.ArrayList;
import java.util.List;

public class Blitzen extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("WHITE", "CIRCLE_MIDDLE"));
        patterns.add(new Item.Pattern("BLACK", "BRICKS"));
        patterns.add(new Item.Pattern("BROWN", "TRIANGLE_BOTTOM"));
        patterns.add(new Item.Pattern("BLACK", "SQUARE_BOTTOM_LEFT"));
        patterns.add(new Item.Pattern("BROWN", "SQUARE_BOTTOM_RIGHT"));
        patterns.add(new Item.Pattern("BLACK", "TRIANGLES_BOTTOM"));
    }

    public Blitzen() {
        super(14, "Blitzen", "&4&lBlitzen", "&4One of Santa's Reindeer", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Candy Crates", patterns, "BLACK", true, Rarity.RARE);
    }
}
