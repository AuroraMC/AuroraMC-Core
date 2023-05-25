/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.killmessages;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;

import java.util.Collections;

public class Rainbow extends KillMessage {

    public Rainbow() {
        super(502, TextFormatter.rainbowBold("Rainbow").toLegacyText(), TextFormatter.rainbowBold("Rainbow").toLegacyText(), "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock these Kill Messages!", true, "FIREWORK", (short)0, Rarity.LEGENDARY);
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
            case MELEE: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s was killed by %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s was killed.", victimName)).toLegacyText();
                }
            }
            case BOW: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s was shot by %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s was shot.", victimName)).toLegacyText();
                }
            }
            case VOID: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s was thrown into the void by %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s fell into the void.", victimName)).toLegacyText();
                }
            }
            case FALL: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s was thrown off a cliff by %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s was thrown off a cliff.", victimName)).toLegacyText();
                }

            }
            case TNT: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s was blown up by %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s blew up", victimName)).toLegacyText();
                }
            }
            case ENTITY: {
                return TextFormatter.rainbow(String.format("**%s** was killed by **%s**.", victimName, entity)).toLegacyText();
            }
            case DROWNING: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s drowned trying to escape %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s drowned.", victimName)).toLegacyText();
                }
            }
            case LAVA: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s fell in lava trying to escape %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s fell in lava.", victimName)).toLegacyText();
                }
            }
            case FIRE: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s burnt to death trying to escape %s", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s burnt to death.", victimName)).toLegacyText();
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return TextFormatter.rainbow(String.format("%s was paintballed by %s's Turret.", victimName, killerName)).toLegacyText();
                } else {
                    if (killer != null) {
                        return TextFormatter.rainbow(String.format("%s was paintballed by %s.", victimName, killerName)).toLegacyText();
                    } else {
                        return TextFormatter.rainbow(String.format("%s was paintballed by a Missile Strike.", victimName)).toLegacyText();
                    }
                }
            }
            case TAG: {
                return TextFormatter.rainbow(String.format("%s was tagged by %s.", victimName, killerName)).toLegacyText();
            }
            case UNKNOWN: {
                if (killer != null) {
                    return TextFormatter.rainbow(String.format("%s was killed by %s using magic", victimName, killerName)).toLegacyText();
                } else {
                    return TextFormatter.rainbow(String.format("%s was killed by magic.", victimName)).toLegacyText();
                }
            }
        }

        return null;
    }
}
