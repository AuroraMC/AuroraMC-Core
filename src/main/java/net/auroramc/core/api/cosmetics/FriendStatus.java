/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.List;

public abstract class FriendStatus extends Cosmetic {

    private final String title;
    private final char colour;

    public FriendStatus(int id, String name, String displayName, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, String title, char colour, boolean showInGUI) {
        super(id, CosmeticType.FRIEND_STATUS, name, displayName, "", unlockMode, currency, permissions, ranks, unlockMessage, showInGUI, Material.PAPER, (short) 1);
        this.title = title;
        this.colour = colour;
    }

    public String getTitle() {
        return title;
    }

    public char getColour() {
        return colour;
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
    }
}
