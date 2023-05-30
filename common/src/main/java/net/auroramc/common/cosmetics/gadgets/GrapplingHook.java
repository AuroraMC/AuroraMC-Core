/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.gadgets;

import net.auroramc.api.cosmetics.Gadget;

import java.util.Collections;

public class GrapplingHook extends Gadget {


    public GrapplingHook() {
        super(801, "Grappling Hook", "&c&lGrappling Hook", "Spiderman, Spiderman, does whatever a Spiderman does.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Gadget!", true, "FISHING_ROD", (short)0, Rarity.EPIC, 0);
    }

}
