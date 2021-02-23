package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;

import java.util.List;

public abstract class DeathEffect extends Cosmetic {

    public DeathEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.DEATH_EFFECT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

    abstract void onDeath(AuroraMCPlayer player);
}
