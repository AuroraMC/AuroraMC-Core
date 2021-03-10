package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.List;

public abstract class DeathEffect extends Cosmetic {

    public DeathEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data) {
        super(id, CosmeticType.DEATH_EFFECT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data);
    }

    abstract void onDeath(AuroraMCPlayer player);
}
