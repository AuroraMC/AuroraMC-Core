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

public class ScreamingGhost extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();

        patterns.add(new Item.Pattern("BLACK", "CREEPER"));
        patterns.add(new Item.Pattern("BLACK", "STRIPE_CENTER"));
        patterns.add(new Item.Pattern("BLACK", "RHOMBUS_MIDDLE"));
        patterns.add(new Item.Pattern("WHITE", "TRIANGLE_TOP"));
        patterns.add(new Item.Pattern("WHITE", "CURLY_BORDER"));
        patterns.add(new Item.Pattern("WHITE", "CROSS"));
    }

    public ScreamingGhost() {
        super(19, "Screaming Ghost", "&8&lScreaming Ghost", "&8Ooooooooh! Spook!", UnlockMode.SPECIAL_CRATE, -1, new ArrayList<>(), new ArrayList<>(), "Found in Spooky Crates", patterns, "WHITE", true, Rarity.RARE);
    }
}
