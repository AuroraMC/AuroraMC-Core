package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.List;

public abstract class FriendStatus extends Cosmetic {

    public FriendStatus(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, String title, char colour, boolean showInGUI) {
        super(id, CosmeticType.FRIEND_STATUS, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showInGUI, Material.PAPER, (short) 1);
    }
}
