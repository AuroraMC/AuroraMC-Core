/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.deatheffects;

import net.auroramc.core.api.cosmetics.DeathEffect;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class Confetti extends DeathEffect {

    public Confetti() {
        super(702, "Confetti", "&d&kCo&a&knf&b&ket&e&kti", "A mystery that is yet to be uncovered...", UnlockMode.GIVEAWAY, -1, Collections.emptyList(), Collections.emptyList(), "Unlocked by &kBuilding", true, Material.FIREWORK, (short)0, Rarity.MYTHICAL);
    }

    @Override
    public void onDeath(AuroraMCPlayer player) {

    }
}
