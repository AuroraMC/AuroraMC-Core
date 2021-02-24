package net.auroramc.core.api.cosmetics;

import net.auroramc.core.permissions.Permission;
import net.auroramc.core.permissions.Rank;
import org.bukkit.entity.Projectile;

import java.util.List;

public abstract class ProjectileTrail extends Cosmetic {

    public ProjectileTrail(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.PROJECTILE_TRAIL, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

    public abstract void onShoot(Projectile projectile);
}
