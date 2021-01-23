package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;

import java.util.List;

public abstract class DeathEffect extends Cosmetic {

    public DeathEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks) {
        super(id, CosmeticType.DEATH_EFFECT, name, displayName, description, unlockMode, currency, permissions, ranks);
    }
}
