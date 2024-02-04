/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.gadgets;

import net.auroramc.api.cosmetics.Gadget;

import java.util.Collections;

public class GrapplingHook extends Gadget {


    public GrapplingHook() {
        super(801, "Grappling Hook", "&c&lGrappling Hook", "Spiderman, Spiderman, does whatever a Spiderman does.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Gadget!", true, "FISHING_ROD", (short)0, Rarity.EPIC, 0);
    }

}
