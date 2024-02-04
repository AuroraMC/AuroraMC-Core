/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.particleeffects;

import net.auroramc.api.cosmetics.ParticleEffect;

import java.util.Collections;

public class EmeraldSwirl extends ParticleEffect {


    public EmeraldSwirl() {
        super(901, "Emerald Swirl", "&a&lEmerald Swirl", "An emerald swirl.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Particle Effect!", true, "EMERALD", (short)0, Rarity.EPIC);
    }
}
