/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.wineffects;

import net.auroramc.core.api.cosmetics.WinEffect;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class ConfettiCannon extends WinEffect {

    public ConfettiCannon() {
        super(603, "Confetti Cannon", "&d&kConf&a&ketti&r &b&kCann&e&kon", "A mystery that is yet to be uncovered...", UnlockMode.GIVEAWAY, -1, Collections.emptyList(), Collections.emptyList(), "Unlocked by &kBuilding", true, Material.GOLDEN_CARROT, (short)0, Rarity.MYTHICAL);
    }

    @Override
    public void onWin(AuroraMCPlayer player) {

    }
}
