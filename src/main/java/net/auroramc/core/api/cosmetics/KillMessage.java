/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.List;

public abstract class KillMessage extends Cosmetic {


    public KillMessage(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data, Rarity rarity) {
        super(id, CosmeticType.KILL_MESSAGE, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, material, data, rarity);
    }

    public abstract String onKill(AuroraMCPlayer receiver, AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason);

    @Override
    public void onEquip(AuroraMCPlayer player) {
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
    }

    public enum KillReason {
        MELEE,
        BOW,
        VOID,
        FALL,
        TNT,
        ENTITY,
        DROWNING,
        LAVA,
        FIRE,
        PAINTBALL,
        TAG,
        UNKNOWN
    }

}
