/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.killmessages;

import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Collections;

public class KillerGoose extends KillMessage {


    public KillerGoose() {
        super(508, "Killer Goose", "&7&lKiller Goose", "Peace was never an option.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Kill Message!", true, "EGG", (short)0, Rarity.EPIC);
    }

    @Override
    public String onKill(AuroraMCPlayer receiver, AuroraMCPlayer killer, AuroraMCPlayer victim, String entity, KillReason reason, int gameId) {
        String victimName = victim.getName();
        String killerName = ((killer == null)?null:killer.getName());
        if (receiver != null) {
            if (receiver.equals(victim)) {
                if (receiver.isDisguised() && receiver.getPreferences().isHideDisguiseNameEnabled()) {
                    victimName = receiver.getName();
                }
            }
            if (receiver.equals(killer)) {
                if (receiver.isDisguised() && receiver.getPreferences().isHideDisguiseNameEnabled()) {
                    killerName = receiver.getName();
                }
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
                return String.format("**%s** was sent by the goose and killed **%s** .", entity, victimName);
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
                        return String.format("**%s** was egg'd by a Missile Strike.", victimName);
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
