/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.servermessages;

import net.auroramc.api.cosmetics.ServerMessage;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;


import java.util.Collections;
import java.util.List;

public class Teleported extends ServerMessage {
    public Teleported() {
        super(406, "&bTeleported", "&5&lTeleported", "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", true, "ENDER_PEARL", (short)0, Rarity.EPIC);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s** &dTeleported In!", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s** &cTeleported away...", name);
    }
}
