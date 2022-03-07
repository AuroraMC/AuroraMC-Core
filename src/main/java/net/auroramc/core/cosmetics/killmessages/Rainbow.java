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
        super(502, AuroraMCAPI.getFormatter().rainbowBold("Rainbow"), AuroraMCAPI.getFormatter().rainbowBold("Rainbow"), "Some Description", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock these Kill Messages!", true, Material.FIREWORK, (short)0, Rarity.MYTHICAL);
    }

    @Override
    public String onKill(AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason) {
        switch (reason) {
            case MELEE: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was killed by %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was killed."), victim.getPlayer().getName());
                }
            }
            case BOW: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was shot by %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was shot."), victim.getPlayer().getName());
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was thrown into the void by %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s fell into the void."), victim.getPlayer().getName());
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was thrown off a cliff by %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was thrown off a cliff."), victim.getPlayer().getName());
                }

            }
            case TNT: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was blown up by %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s blew up"), victim.getPlayer().getName());
                }
            }
            case ENTITY: {
                return String.format(AuroraMCAPI.getFormatter().rainbow("**%s** was killed by **%s**."), victim.getPlayer().getName(), WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s drowned trying to escape %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s drowned."), victim.getPlayer().getName());
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s fell in lava trying to escape %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s fell in lava."), victim.getPlayer().getName());
                }
            }
            case FIRE: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s burnt to death trying to escape %s"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s burnt to death."), victim.getPlayer().getName());
                }
            }
            case UNKNOWN: {
                if (killer != null) {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was killed by %s using magic"), victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format(AuroraMCAPI.getFormatter().rainbow("%s was killed by magic."), victim.getPlayer().getName());
                }
            }
        }

        return null;
    }
}
