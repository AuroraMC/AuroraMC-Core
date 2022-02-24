/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;

import java.util.List;

public abstract class ProjectileTrail extends Cosmetic {

    public ProjectileTrail(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data, Rarity rarity) {
        super(id, CosmeticType.PROJECTILE_TRAIL, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
    }

    public abstract void onShoot(Projectile projectile);
}
