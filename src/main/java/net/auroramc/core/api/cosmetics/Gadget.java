/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Gadget extends Cosmetic {

    private int cooldown;

    public Gadget(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data, Rarity rarity, int cooldown) {
        super(id, CosmeticType.GADGET, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
        this.cooldown = cooldown;
    }

    public abstract void onUse(AuroraMCPlayer player, Location location);

    @Override
    public void onUnequip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setItem(3, new ItemStack(Material.AIR));
    }

    public int getCooldown() {
        return cooldown;
    }
}
