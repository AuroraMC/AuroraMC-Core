/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.particleeffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.ParticleEffect;


import java.util.*;

public class PurpleSwirl extends ParticleEffect {

    public PurpleSwirl() {
        super(902, "Purple Swirl", "&5&lPurple Swirl", "A purple swirl.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", true, "EYE_OF_ENDER", (short)0, Rarity.EPIC);
    }
}
