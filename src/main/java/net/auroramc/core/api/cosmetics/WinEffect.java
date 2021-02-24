package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;
import net.auroramc.core.permissions.Rank;

import java.util.List;

public abstract class WinEffect extends Cosmetic {

    public WinEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
        super(id, CosmeticType.WIN_EFFECT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);
    }

    public abstract void onWin(AuroraMCPlayer player);

}
