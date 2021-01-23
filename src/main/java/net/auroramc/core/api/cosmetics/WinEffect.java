package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;

import java.util.List;

public abstract class WinEffect extends Cosmetic {

    public WinEffect(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks) {
        super(id, CosmeticType.WIN_EFFECT, name, displayName, description, unlockMode, currency, permissions, ranks);
    }

    public abstract void onWin(AuroraMCPlayer player);

}