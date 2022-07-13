/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.killmessages;

import net.auroramc.core.api.cosmetics.KillMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.Collections;

public class KillerGoose extends KillMessage {


    public KillerGoose() {
        super(508, "Killer Goose", "&7&lKiller Goose", "Peace was never an option.", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", true, Material.EGG, (short)0, Rarity.RARE);
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
            case BOW: {
                if(killer != null) {
                    return String.format("**%s** was egg'd too hard by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** was egg'd too hard.", victimName);
                }
            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s** was EGGsploded by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** EGGsploded.", victimName);
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s** was BONK'd off a cliff by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** was BONK'd off a cliff.", victimName);
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s** was BONK'd into the void by **%s**", victimName, killerName);
                } else {
                    return String.format("The goose threw **%s** into the void.", victimName);
                }
            }
            case MELEE: {
                if (killer != null) {
                    return String.format("**%s** was BONK'd by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** died. Mess with the HONK, you get the BONK.", victimName);
                }
            }
            case ENTITY: {
                return String.format("**%s** was sent by the goose and killed **%s** .", WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))), victimName);
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s** drowned running away from **%s**. Luckily, geese can swim.", victimName, killerName);
                } else {
                    return String.format("**%s** drowned.", victimName);
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s** roasted in lava trying to escape **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** roasted in lava.", victimName);
                }
            }
            case FIRE: {
                if(killer != null) {
                    return String.format("**%s** was roasted trying to escape **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** was roasted by the goose.", victimName);
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return String.format("**%s** was egg'd by **%s**'s Turret.", victimName, killerName);
                } else {
                    if (killer != null) {
                        return String.format("**%s** was egg'd by **%s**.", victimName, killerName);
                    } else {
                        return String.format("**%s** was egg'd by the game. Somehow.", victimName);
                    }
                }
            }
            case TAG: {
                return String.format("**%s** was BONK'd by **%s**.", victimName, killerName);
            }
            case UNKNOWN: {
                if (killer != null) {
                    return String.format("**%s** was BONK'd by **%s**.", victimName, killerName);
                } else {
                    return String.format("**%s** died. Peace was never an option.", victimName);
                }
            }
        }

        return null;
    }
}
