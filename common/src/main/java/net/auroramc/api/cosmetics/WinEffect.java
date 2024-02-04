/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.abstraction.CosmeticFactory;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.List;

public abstract class WinEffect extends Cosmetic {

    public WinEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, String material, short data, Rarity rarity) {
        super(id, CosmeticType.WIN_EFFECT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
    }

    public void onWin(AuroraMCPlayer player) {
        CosmeticFactory.onExecuteCosmetic(player, this);
    }

}
