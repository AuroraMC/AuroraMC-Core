package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;

import java.util.List;

public abstract class ParticleEffect extends Cosmetic {

    public ParticleEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.PARTICLE, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

}
