/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
                name = player.getName();
            }
        }
        return String.format("**%s**.playGames(&atrue&r);", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s**.playGames(&cfalse&r);", name);
    }
}
