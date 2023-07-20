/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.servermessages;

import net.auroramc.api.cosmetics.ServerMessage;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;


import java.util.Collections;

public class Hungry extends ServerMessage {
    public Hungry() {
        super(402, "&bHungry", "&3&lHungry", "Show off your hunger to win with these server messages!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MASTER), "Purchase Master at store.auroramc.net to unlock these Server Messages!", true, "NAME_TAG", (short)0, Rarity.RARE);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s** is hungry for &d&lVICTORY!", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getByDisguiseName();
            }
        }
        return String.format("**%s** is looking for &d&lVICTORY&r elsewhere.", name);
    }
}
