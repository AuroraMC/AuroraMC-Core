/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.List;

public abstract class ServerMessage extends Cosmetic {


    public ServerMessage(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data, Rarity rarity) {
        super(id, CosmeticType.SERVER_MESSAGE, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
    }

    public abstract String onJoin(AuroraMCPlayer player);

    public abstract String onLeave(AuroraMCPlayer player);

    @Override
    public void onEquip(AuroraMCPlayer player) {

    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {

    }

}