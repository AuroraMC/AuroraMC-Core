/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.deatheffects;

import net.auroramc.api.cosmetics.DeathEffect;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Collections;

public class Confetti extends DeathEffect {

    public Confetti() {
        super(702, "Confetti", "&d&kCo&a&knf&b&ket&e&kti", "A mystery that is yet to be uncovered...", UnlockMode.GIVEAWAY, -1, Collections.emptyList(), Collections.emptyList(), "Unlocked by &kBuilding", true, "GOLDEN_CARROT", (short)0, Rarity.MYTHICAL);
    }

    @Override
    public void onDeath(AuroraMCPlayer player) {

    }
}
