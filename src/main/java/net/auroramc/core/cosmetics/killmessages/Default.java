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
        super(500, "&3&lDefault Kill Messages", "&3&lDefault Kill Messages", "The default kill messages", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", true, Material.GOLD_SWORD, (short)0, Rarity.COMMON);
    }

    @Override
    public String onKill(AuroraMCPlayer killer, AuroraMCPlayer victim, Entity entity, KillReason reason) {
        switch (reason) {
            case BOW: {
                return String.format("**%s** was shot by **%s**", victim.getPlayer().getName(), killer.getPlayer().getName());
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
            case ENTITY:{
                return String.format("**%s** was a noob and was killed by **%s**.", victim.getPlayer().getName(), WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));
            }
        }

        return null;
    }
}
