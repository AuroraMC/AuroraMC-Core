/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.List;

public abstract class PlusSymbol extends Cosmetic {

    private final char symbol;

    public PlusSymbol(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data, char symbol, Rarity rarity) {
        super(id, CosmeticType.PLUS_SYMBOL, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
            player1.updateNametag(player);
        }
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
        for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
            player1.updateNametag(player);
        }
    }
}
