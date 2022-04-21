/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.killmessages;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;

public class DeveloperKillPlayer extends KillMessage {

    public DeveloperKillPlayer() {
        super(503, "&3&ldeveloper#killPlayer", "&3&ldeveloper#killPlayer", "Some Description", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.DEBUG_INFO), Collections.emptyList(), "", false, Material.GOLD_SWORD, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public String onKill(AuroraMCPlayer receiver, AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason) {
        String victimName = victim.getPlayer().getName();
        if (receiver.equals(victim)) {
            if (receiver.isDisguised() && receiver.getPreferences().isHideDisguiseNameEnabled()) {
                victimName = receiver.getName();
            }
        }
        String killerName = ((killer == null)?null:killer.getPlayer().getName());
        if (receiver.equals(killer)) {
            if (receiver.isDisguised() && receiver.getPreferences().isHideDisguiseNameEnabled()) {
                killerName = receiver.getName();
            }
        }
        switch (reason) {
            case MELEE: {
                if (killer != null) {
                    return String.format("**%s**.kill(**%s**);", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cMelee&r);", victimName);
                }
            }
            case BOW: {
                if (killer != null) {
                    return String.format("**%s**.shoot(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cBow&r);", victimName);
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s**.throwInVoid(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cVoid&r);", victimName);
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s**.throwOffCliff(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cFall&r);", victimName);
                }

            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s**.explode(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cExplosion&r);", victimName);
                }
            }
            case ENTITY: {
                return String.format("**%s**.setDeathBy(&c%s&r);", victimName, WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s**.drown(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cDrowning&r);", victimName);
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s**.dropInLava(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cLava&r);", victimName);
                }
            }
            case FIRE: {
                if (killer != null) {
                    return String.format("**%s**.setOnFire(**%s**)", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cFire&r);", victimName);
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return String.format("**%s**.setPaintballedByTurret(**%s**;.", killerName, victimName);
                } else {
                    if (killer != null) {
                        return String.format("**%s**.setPaintballed(**%s**);", killerName, victimName);
                    } else {
                        return String.format("**%s**.setPaintballedSomehow(&atrue&r);", victimName);
                    }
                }
            }
            case TAG: {
                return String.format("**%s**.setTagged(**%s**);", killerName, victimName);
            }
            case UNKNOWN: {
                if (killer != null) {
                    return String.format("**%s**.killByMagic(**%s**);", killerName, victimName);
                } else {
                    return String.format("**%s**.setDeathBy(&cmagic&r);", victimName);
                }
            }
        }

        return null;
    }
}
