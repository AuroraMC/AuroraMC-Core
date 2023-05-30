/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
