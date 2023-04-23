/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.CosmeticFactory;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.List;

public abstract class PlusSymbol extends Cosmetic {

    private final char symbol;

    public PlusSymbol(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, String material, short data, char symbol, Rarity rarity) {
        super(id, CosmeticType.PLUS_SYMBOL, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        CosmeticFactory.onEquipSymbol(player, this);
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
        CosmeticFactory.onUnequipSymbol(player, this);
    }
}
