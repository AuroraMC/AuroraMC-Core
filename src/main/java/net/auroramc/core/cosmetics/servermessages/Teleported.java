/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.servermessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class Teleported extends ServerMessage {
    public Teleported() {
        super(406, "&bTeleported", "&5&lTeleported", "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", true, Material.ENDER_PEARL, (short)0, Rarity.EPIC);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getPlayer().getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** &dTeleported In!", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getPlayer().getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** &cTeleported away...", name);
    }
}
