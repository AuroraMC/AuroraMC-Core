package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;
import net.auroramc.core.permissions.Rank;

import java.util.List;

public abstract class KillMessage extends Cosmetic {


    public KillMessage(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.KILL_MESSAGE, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

    abstract String onKill(AuroraMCPlayer killer, AuroraMCPlayer victim, KillReason reason);

    public enum KillReason {
        MELEE,
        BOW,
        VOID,
        FALL,
        TNT,
        ENTITY
    }

}
