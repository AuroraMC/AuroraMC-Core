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

public class AuroraMC extends Banner {

    public static final List<Item.Pattern> patterns;

    static {
        patterns = new ArrayList<>();
        patterns.add(new Item.Pattern("CYAN", "STRIPE_RIGHT"));
        patterns.add(new Item.Pattern("CYAN", "STRIPE_LEFT"));
        patterns.add(new Item.Pattern("CYAN", "STRIPE_TOP"));
        patterns.add(new Item.Pattern("CYAN", "STRIPE_MIDDLE"));
        patterns.add(new Item.Pattern("WHITE", "BORDER"));
    }

    public AuroraMC() {
        super(8, "AuroraMC?", "&3&lAuroraMC?", "&3It's the best we could do, okay?", UnlockMode.STORE_PURCHASE, -1, new ArrayList<>(), new ArrayList<>(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this banner!", patterns, "WHITE", true, Rarity.EPIC);
    }
}
