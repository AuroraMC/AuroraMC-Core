/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.deatheffects;

import net.auroramc.api.cosmetics.DeathEffect;

import java.util.Collections;

public class LayAnEgg extends DeathEffect {


    public LayAnEgg() {
        super(701, "Lay an Egg", "&e&lLay an Egg", "You are now a chicken. You will lay an egg when you die.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Death Effect!", true, "EGG", (short)0, Rarity.EPIC);
    }
}
