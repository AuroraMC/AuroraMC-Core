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

public class CandyCane extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        // [{Pattern:ts,Color:15},{Pattern:mr,Color:1},{Pattern:dls,Color:15},{Pattern:ss,Color:1},{Pattern:cbo,Color:1},{Pattern:bo,Color:1}]}}
        patterns.add(new Item.Pattern("WHITE", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("RED", "RHOMBUS_MIDDLE"));
        patterns.add(new Item.Pattern("WHITE", "STRIPE_DOWNLEFT"));
        patterns.add(new Item.Pattern("RED", "STRIPE_SMALL"));
        patterns.add(new Item.Pattern("RED", "CURLY_BORDER"));
        patterns.add(new Item.Pattern("RED", "BORDER"));
    }

    public CandyCane() {
        super(16, "Candy Cane", "&c&lCandy&r &f&lCane", "&cMmmm tasty...", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Candy Crates", patterns, "RED", true, Rarity.RARE);
    }
}
