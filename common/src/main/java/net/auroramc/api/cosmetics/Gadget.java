/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.abstraction.CosmeticFactory;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.List;

public abstract class Gadget extends Cosmetic {

    private int cooldown;

    public Gadget(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, String material, short data, Rarity rarity, int cooldown) {
        super(id, CosmeticType.GADGET, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
        this.cooldown = cooldown;
    }

    public void onUse(AuroraMCPlayer player, double x, double y, double z) {
        CosmeticFactory.onExecuteCosmetic(player, this, x, y, z);
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        CosmeticFactory.onEquipGadget(player, this);
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
        CosmeticFactory.onUnequipGadget(player, this);
    }

    public int getCooldown() {
        return cooldown;
    }
}
