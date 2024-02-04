/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.particleeffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.ParticleEffect;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BloodSwirl extends ParticleEffect {

    public BloodSwirl() {
        super(900, "Blood Swirl", "&c&lBlood Swirl", "A blood swirl.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Particle Effect!", true, "REDSTONE", (short)0, Rarity.LEGENDARY);
    }
}
