/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.banners;

import net.auroramc.api.cosmetics.Banner;
import net.auroramc.api.utils.Item;


import java.util.ArrayList;
import java.util.List;

public class TheGoose extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("ORANGE", "MOJANG"));
        patterns.add(new Item.Pattern("WHITE", "CURLY_BORDER"));
        patterns.add(new Item.Pattern("WHITE", "HALF_HORIZONTAL"));
        patterns.add(new Item.Pattern("CYAN", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("CYAN", "STRIPE_DOWNRIGHT"));
        patterns.add(new Item.Pattern("CYAN", "HALF_VERTICAL_MIRROR"));
    }

    public TheGoose() {
        super(10, "The Goose", "&f&lThe&r &6&lGoose", "&fPeace was never an option.", UnlockMode.CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Crates", patterns, "CYAN", true, Rarity.EPIC);
    }
}
