/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.joinmessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class TakingABreakFromJava extends ServerMessage {
    public TakingABreakFromJava() {
        super(405, "&bTaking a break from &c&lJAVA", "&3&lTaking a break from &c&lJAVA", "Show off that you're taking a well deserved break.", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.DEBUG_INFO), Collections.emptyList(), "", false, Material.NAME_TAG, (short)0);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** is taking a break from &c&lJAVA&r.", player.getPlayer().getName());
    }

    @Override
    public String onLeave(AuroraMCPlayer player) {
        return String.format("**%s** is going back to doing &c&lJAVA&r.", player.getPlayer().getName());
    }
}
