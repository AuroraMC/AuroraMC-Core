/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.projectiletrails;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.ProjectileTrail;
import net.auroramc.api.utils.TextFormatter;

import java.util.Collections;
import java.util.Random;

public class FireworkTrail extends ProjectileTrail {
    public FireworkTrail() {
        super(1000, TextFormatter.rainbow("Firework Trail").toLegacyText(), TextFormatter.rainbow("Firework Trail").toLegacyText(), "A trail of fireworks. This one is loud. Sorry.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Projectile Trail!", true, "FIREWORK", (short)0, Rarity.LEGENDARY);
    }
}
