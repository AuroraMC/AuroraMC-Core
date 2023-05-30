/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.hats;

import net.auroramc.api.cosmetics.Hat;

import java.util.Collections;

public class Goose extends Hat {


    public Goose() {
        super(347, "The Goose", "The Goose", "HONK!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Hat!", "7e04e6449e3f007f9bfe16f3ce4af696f9b4085b8258f232cfa1864d07b73ecd", true, Rarity.RARE);
    }
}
