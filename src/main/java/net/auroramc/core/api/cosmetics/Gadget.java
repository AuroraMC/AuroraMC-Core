package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;

import java.util.List;

public abstract class Gadget extends Cosmetic {

    public Gadget(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.GADGET, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

    abstract void onUse(AuroraMCPlayer player);
}
