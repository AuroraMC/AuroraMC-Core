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
        super(503, "&3&ldeveloper#killPlayer", "&3&ldeveloper#killPlayer", "Some Description", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.DEVELOPER), "", false, Material.COCOA, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public String onKill(AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason) {
        switch (reason) {
            case MELEE: {
                if (killer != null) {
                    return String.format("**%s**.kill(**%s**);", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cMelee&r);", victim.getPlayer().getName());
                }
            }
            case BOW: {
                if (killer != null) {
                    return String.format("**%s**.shoot(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cBow&r);", victim.getPlayer().getName());
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s**.throwInVoid(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cVoid&r);", victim.getPlayer().getName());
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s**.throwOffCliff(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cFall&r);", victim.getPlayer().getName());
                }

            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s**.explode(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cExplosion&r);", victim.getPlayer().getName());
                }
            }
            case ENTITY: {
                return String.format("**%s**.setDeathBy(&c%s&r);", victim.getPlayer().getName(), WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s**.drown(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cDrowning&r);", victim.getPlayer().getName());
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s**.dropInLava(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cLava&r);", victim.getPlayer().getName());
                }
            }
            case FIRE: {
                if (killer != null) {
                    return String.format("**%s**.setOnFire(**%s**)", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cFire&r);", victim.getPlayer().getName());
                }
            }
            case UNKNOWN: {
                if (killer != null) {
                    return String.format("**%s**.killByMagic(**%s**);", killer.getPlayer().getName(), victim.getPlayer().getName());
                } else {
                    return String.format("**%s**.setDeathBy(&cmagic&r);", victim.getPlayer().getName());
                }
            }
        }

        return null;
    }
}
