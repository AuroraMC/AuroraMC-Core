/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class JackOLantern extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("ORANGE", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("RED", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("ORANGE", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("GRAY", "CREEPER"));
        patterns.add(new Item.Pattern("LIME", "TRIANGLES_TOP"));
    }

    public JackOLantern() {
        super(17, "Jack-O-Lantern", "&6&lJack-O&8&l-Lantern", "&8Watch out for ghosts and ghouls!", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Spooky Crates", patterns, "ORANGE", true, Rarity.RARE);
    }
}
