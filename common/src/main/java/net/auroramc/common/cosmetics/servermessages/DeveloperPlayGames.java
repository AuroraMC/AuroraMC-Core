/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.servermessages;

import net.auroramc.api.cosmetics.ServerMessage;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.AuroraMCPlayer;


import java.util.Collections;

public class DeveloperPlayGames extends ServerMessage {
    public DeveloperPlayGames() {
        super(405, "&3&ldeveloper#PlayGames", "&3&ldeveloper#playGames", "Show off that you're taking a well deserved break.", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.DEBUG_INFO), Collections.emptyList(), "", false, "NAME_TAG", (short)0, Rarity.MYTHICAL);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s**.playGames(&atrue&r);", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s**.playGames(&cfalse&r);", name);
    }
}
