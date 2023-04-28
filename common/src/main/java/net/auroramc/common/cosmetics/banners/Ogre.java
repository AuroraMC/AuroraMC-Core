/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class Ogre extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();

        patterns.add(new Item.Pattern("WHITE", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("LIME", "CURLY_BORDER"));
        patterns.add(new Item.Pattern("LIME", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("BLACK", "RHOMBUS_MIDDLE"));
        patterns.add(new Item.Pattern("LIME", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("LIME", "CREEPER"));
        patterns.add(new Item.Pattern("BLACK", "TRIANGLES_TOP"));
    }

    public Ogre() {
        super(18, "Ogre", "&2&lOgre", "&2He's gonna get you!", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "BLACK", true, Rarity.RARE);
    }
}
