/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.killmessages.levelrewards;

import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Collections;

public class Flex extends KillMessage {
    public Flex() {
        super(507, "&4&lFlex", "&4&lFlex", "Flex your insane achievements on AuroraMC!", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "Reach AuroraMC Level 100 to unlock these Kill Messages!", true, "GOLDEN_APPLE", (short)0, Rarity.LEGENDARY);
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
                    return String.format("**%s** was kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has died **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
            case BOW: {
                if (killer != null) {
                    return String.format("**%s** was archery kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has been shot **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths" + reason.name()));
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s** was void kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has fallen into the void **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s** was cliff kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has fallen off a cliff **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }

            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s** was TNT kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has blown up **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
            case ENTITY: {
                return String.format("**%s** has died **#%s** times to entities, this time at the hands of **%s**.", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()), entity);
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s** was drown kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has drowned **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s** was lava kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has died to lava **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
            case FIRE: {
                if (killer != null) {
                    return String.format("**%s** was fire kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has died to fire **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return String.format("**%s** was paintball kill **#%s** for **%s**'s Turret", victimName, killer.getStats().getStatistic(3, "kills;turret") , killerName);
                } else {
                    if (killer != null) {
                        return String.format("**%s** was paintball kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(3, "kills") , killerName);
                    } else {
                        return String.format("**%s** has been died to a paintball in a Missile Strike **%s** times", victimName, victim.getStats().getStatistic(3, "deaths"));
                    }
                }
            }
            case TAG: {
                return String.format("**%s** was tag **#%s** for **%s**", victimName, killer.getStats().getStatistic(103, "tags"), killerName);
            }
            case UNKNOWN: {
                if (killer != null) {
                    return String.format("**%s** was magic kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills;" + reason.name()), killerName);
                } else {
                    return String.format("**%s** has magically died **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths;" + reason.name()));
                }
            }
        }

        return null;
    }
}
