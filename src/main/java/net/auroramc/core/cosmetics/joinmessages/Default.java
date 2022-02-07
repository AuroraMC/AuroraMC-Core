/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.joinmessages;

import net.auroramc.core.api.cosmetics.JoinMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class Default extends JoinMessage {

    public Default() {
        super(400, "&3&lDefault", "&3&lDefault", "The default join message.", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", true, Material.NAME_TAG, (short)0);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** has joined the game.", player.getPlayer().getName());
    }
}
