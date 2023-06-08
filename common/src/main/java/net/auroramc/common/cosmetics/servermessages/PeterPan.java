/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.servermessages;

import net.auroramc.api.cosmetics.ServerMessage;
import net.auroramc.api.player.AuroraMCPlayer;


import java.util.Collections;

public class PeterPan extends ServerMessage {
    public PeterPan() {
        super(404, "&bPeter Pan", "&3&lPeter Pan", "", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates.", true, "NAME_TAG", (short)0, Rarity.UNCOMMON);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s** is ready to &e&lTAKE FLIGHT!", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s** has their feet back on the floor.", name);
    }
}
