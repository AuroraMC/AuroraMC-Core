/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.servermessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class Default extends ServerMessage {

    public Default() {
        super(400, "Default", "&3&lDefault", "The default join message.", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", true, Material.NAME_TAG, (short)0, Rarity.COMMON);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getPlayer().getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** has joined the game.", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getPlayer().getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** has left the game.", name);
    }
}
