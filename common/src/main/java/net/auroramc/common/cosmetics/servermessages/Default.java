/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.servermessages;

import net.auroramc.api.cosmetics.ServerMessage;
import net.auroramc.api.player.AuroraMCPlayer;


import java.util.Collections;

public class Default extends ServerMessage {

    public Default() {
        super(400, "Default", "&3&lDefault", "The default join message.", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", true, "NAME_TAG", (short)0, Rarity.COMMON);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** has joined the game.", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** has left the game.", name);
    }
}
