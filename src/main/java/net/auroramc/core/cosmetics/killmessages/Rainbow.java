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

public class Rainbow extends KillMessage {

    public Rainbow() {
        super(502, AuroraMCAPI.getFormatter().rainbowBold("Rainbow"), AuroraMCAPI.getFormatter().rainbowBold("Rainbow"), "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock these Kill Messages!", true, Material.FIREWORK, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public String onKill(AuroraMCPlayer receiver, AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason, int gameId) {
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
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was killed by %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was killed.", victimName));
                }
            }
            case BOW: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was shot by %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was shot.", victimName));
                }
            }
            case VOID: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was thrown into the void by %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s fell into the void.", victimName));
                }
            }
            case FALL: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was thrown off a cliff by %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was thrown off a cliff.", victimName));
                }

            }
            case TNT: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was blown up by %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s blew up", victimName));
                }
            }
            case ENTITY: {
                return AuroraMCAPI.getFormatter().rainbow(String.format("**%s** was killed by **%s**.", victimName, WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " ")))));
            }
            case DROWNING: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s drowned trying to escape %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s drowned.", victimName));
                }
            }
            case LAVA: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s fell in lava trying to escape %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s fell in lava.", victimName));
                }
            }
            case FIRE: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s burnt to death trying to escape %s", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s burnt to death.", victimName));
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was paintballed by %s's Turret.", victimName, killerName));
                } else {
                    if (killer != null) {
                        return AuroraMCAPI.getFormatter().rainbow(String.format("%s was paintballed by %s.", victimName, killerName));
                    } else {
                        return AuroraMCAPI.getFormatter().rainbow(String.format("%s was paintballed by the game. Somehow.", victimName));
                    }
                }
            }
            case TAG: {
                return AuroraMCAPI.getFormatter().rainbow(String.format("%s was tagged by %s.", victimName, killerName));
            }
            case UNKNOWN: {
                if (killer != null) {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was killed by %s using magic", victimName, killerName));
                } else {
                    return AuroraMCAPI.getFormatter().rainbow(String.format("%s was killed by magic.", victimName));
                }
            }
        }

        return null;
    }
}
