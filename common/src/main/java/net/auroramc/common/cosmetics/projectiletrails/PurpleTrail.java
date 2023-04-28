/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.projectiletrails;

import net.auroramc.api.cosmetics.ProjectileTrail;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PurpleTrail extends ProjectileTrail {
    public PurpleTrail() {
        super(1002, "Purple Trail", "&5&lPurple Trail", "Purple. That's it. Just purple.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", true, "EYE_OF_ENDER", (short)0, Rarity.EPIC);
    }
}
