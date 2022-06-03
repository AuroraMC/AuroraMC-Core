/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.killmessages.levelrewards;

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

public class Flex extends KillMessage {
    public Flex() {
        super(507, "&4&lFlex", "&4&lFlex", "Flex your insane achievements on AuroraMC!", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "Reach AuroraMC Level 100 to unlock these Kill Messages!", true, Material.GOLDEN_APPLE, (short)0, Rarity.LEGENDARY);
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
                    return String.format("**%s** was kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has died **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
            case BOW: {
                if (killer != null) {
                    return String.format("**%s** was archery kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has been shot **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths" + reason.name()) + 1);
                }
            }
            case VOID: {
                if (killer != null) {
                    return String.format("**%s** was void kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has fallen into the void **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
            case FALL: {
                if (killer != null) {
                    return String.format("**%s** was cliff kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has fallen off a cliff **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }

            }
            case TNT: {
                if (killer != null) {
                    return String.format("**%s** was TNT kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has blown up **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
            case ENTITY: {
                return String.format("**%s** has died **#%s** time to entities, this time at the hands of **%s**.", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1, WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));
            }
            case DROWNING: {
                if (killer != null) {
                    return String.format("**%s** was drown kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has drowned **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
            case LAVA: {
                if (killer != null) {
                    return String.format("**%s** was lava kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has died to lava **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
            case FIRE: {
                if (killer != null) {
                    return String.format("**%s** was fire kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has died to fire **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
            case PAINTBALL: {
                if (entity != null) {
                    return String.format("**%s** was paintball kill **#%s** for **%s**'s Turret", victimName, killer.getStats().getStatistic(3, "kills.turret") + 1 , killerName);
                } else {
                    if (killer != null) {
                        return String.format("**%s** was paintball kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(3, "kills") + 1 , killerName);
                    } else {
                        return String.format("**%s** has been magically died to a paintball **%s** times", victimName, victim.getStats().getStatistic(3, "deaths") + 1);
                    }
                }
            }
            case TAG: {
                return String.format("**%s** was tag **#%s** for **%s**", victimName, killer.getStats().getStatistic(103, "tags") + 1, killerName);
            }
            case UNKNOWN: {
                if (killer != null) {
                    return String.format("**%s** was magic kill **#%s** for **%s**", victimName, killer.getStats().getStatistic(gameId, "kills." + reason.name()) + 1, killerName);
                } else {
                    return String.format("**%s** has magically died **#%s** times", victimName, victim.getStats().getStatistic(gameId, "deaths." + reason.name()) + 1);
                }
            }
        }

        return null;
    }
}
