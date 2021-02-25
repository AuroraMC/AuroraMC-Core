package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;

import java.util.List;

public abstract class Morph extends Cosmetic{

    public Morph(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.MORPH, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

}
