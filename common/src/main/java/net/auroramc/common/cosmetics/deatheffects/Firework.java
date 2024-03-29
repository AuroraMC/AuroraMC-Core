/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.deatheffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.DeathEffect;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;

import java.util.Collections;
import java.util.Random;

public class Firework extends DeathEffect {

    final Random random = new Random();

    public Firework() {
        super(700, TextFormatter.rainbow("Firework").toLegacyText(), TextFormatter.rainbow("Firework").toLegacyText(), "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Death Effect!", true, "FIREWORK", (short)0, Rarity.LEGENDARY);
    }
}
