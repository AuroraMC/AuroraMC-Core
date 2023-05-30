/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.List;

public abstract class ServerMessage extends Cosmetic {


    public ServerMessage(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, String material, short data, Rarity rarity) {
        super(id, CosmeticType.SERVER_MESSAGE, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
    }

    public abstract String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player);

    public abstract String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player);

    @Override
    public void onEquip(AuroraMCPlayer player) {

    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {

    }

}