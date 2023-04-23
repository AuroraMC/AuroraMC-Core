/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.wineffects;

import net.auroramc.api.cosmetics.WinEffect;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Collections;

public class ConfettiCannon extends WinEffect {

    public ConfettiCannon() {
        super(603, "Confetti Cannon", "&d&kConf&a&ketti&r &b&kCann&e&kon", "A mystery that is yet to be uncovered...", UnlockMode.GIVEAWAY, -1, Collections.emptyList(), Collections.emptyList(), "Unlocked by &kBuilding", true, "GOLDEN_CARROT", (short)0, Rarity.MYTHICAL);
    }
}
