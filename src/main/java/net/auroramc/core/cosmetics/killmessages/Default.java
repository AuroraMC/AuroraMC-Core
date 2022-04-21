/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.killmessages;

import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;

public class Default extends KillMessage {


    public Default() {
        super(500, "&bDefault", "&3&lDefault", "The default kill messages", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", true, Material.GOLD_SWORD, (short)0, Rarity.COMMON);
    }

    @Override
    public String onKill(AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason) {
        switch (reason) {
            case BOW: {
                if(killer != null) {
                    return String.format("**%s** was shot by **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** was shot.", victim.getPlayer().getName());
                }
            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s** was blown up by **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** was blown up.", victim.getPlayer().getName());
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s** was thrown off a cliff by **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** fell off a cliff.", victim.getPlayer().getName());
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s** was thrown into the void by **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** fell into the void.", victim.getPlayer().getName());
                }
            }
            case MELEE: {
                if (killer != null) {
                    return String.format("**%s** was killed by **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** was killed.", victim.getPlayer().getName());
                }
            }
            case ENTITY: {
                return String.format("**%s** was killed by **%s**.", victim.getPlayer().getName(), WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s** drowned trying to escape from **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** drowned.", victim.getPlayer().getName());
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s** fell in lava trying to escape **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** fell in lava.", victim.getPlayer().getName());
                }
            }
            case FIRE: {
                if(killer != null) {
                    return String.format("**%s** burned to death trying to escape **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s** burned to death.");
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return String.format("**%s** was paintballed by **%s**'s Turret.", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    if (killer != null) {
                        return String.format("**%s** was paintballed by **%s**.", victim.getPlayer().getName(), killer.getPlayer().getName());
                    } else {
                        return String.format("**%s** was paintballed by the game. Somehow.", victim.getPlayer().getName());
                    }
                }
            }
            case TAG: {
                return String.format("**%s** was tagged by **%s**.", victim.getPlayer().getName(), killer.getPlayer().getName());
            }
            case UNKNOWN: {
                if (killer != null ) {
                    return String.format("**%s** was killed by **%s** using magic.", victim.getPlayer().getName(), killer.getPlayer().getName());
                } else {
                    return String.format("**%s was killed by magic.", victim.getPlayer().getName());
                }
            }
        }

        return null;
    }
}
