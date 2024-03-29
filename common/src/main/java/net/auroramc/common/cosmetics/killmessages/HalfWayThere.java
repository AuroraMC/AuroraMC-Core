/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.killmessages;

import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Collections;

public class HalfWayThere extends KillMessage {


    public HalfWayThere() {
        super(501, "&cHalf Way There", "&c&lHalf Way There", "WOOOAAAAHHHH WE'RE HALF WAY THERE, WOOOOOOAAAAAAAHHHHHHH LIVING ON A PRAYER", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "", false, "BOOK", (short)0, Rarity.EPIC);
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
                    return String.format("**%s** was shot by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** was shot.", victimName);
                }
            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s** was blown up by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** was blown up.", victimName);
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s** was thrown off a cliff by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** fell off a cliff.", victimName);
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s** was thrown into the void by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** fell into the void.", victimName);
                }
            }
            case MELEE: {
                if (killer != null) {
                    return String.format("**%s** was killed by **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** was killed.", victimName);
                }
            }
            case ENTITY:{
                    return String.format("**%s** was killed by **%s**.", victimName, entity);
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s** drowned trying to escape from **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** drowned.", victimName);
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s** fell in lava trying to escape **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** fell in lava.", victimName);
                }
            }
            case FIRE: {
                if(killer != null) {
                    return String.format("**%s** burned to death trying to escape **%s**", victimName, killerName);
                } else {
                    return String.format("**%s** burned to death.");
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return String.format("**%s** was paintballed by **%s**'s Turret.", victimName, killerName);
                } else {
                    if (killer != null) {
                        return String.format("**%s** was paintballed by **%s**.", victimName, killerName);
                    } else {
                        return String.format("**%s** was paintballed by a Missile Strike.", victimName);
                    }
                }
            }
            case TAG: {
                return String.format("**%s** was tagged by **%s**.", victimName, killerName);
            }
            case UNKNOWN: {
                if (killer != null ) {
                    return String.format("**%s** was killed by **%s** using magic.", victimName, killerName);
                } else {
                    return String.format("**%s was killed by magic.", victimName);
                }
            }
        }

        return null;
    }
}
