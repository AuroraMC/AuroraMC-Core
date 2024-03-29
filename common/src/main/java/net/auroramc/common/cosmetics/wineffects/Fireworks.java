/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.wineffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.WinEffect;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;

import java.util.Collections;
import java.util.Random;

public class Fireworks extends WinEffect {

    public Fireworks() {
        super(600, TextFormatter.rainbow("Fireworks").toLegacyText(), TextFormatter.rainbow("Fireworks").toLegacyText(), "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Win Effect!", true, "FIREWORK", (short)0, Rarity.LEGENDARY);
    }
}
