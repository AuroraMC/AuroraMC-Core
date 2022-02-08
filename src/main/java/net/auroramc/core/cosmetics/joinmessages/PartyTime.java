/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.joinmessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class PartyTime extends ServerMessage {
    public PartyTime() {
        super(401, "&bParty Time", "&3&lParty Time", "Are you ready to party? Show that off!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ELITE), "Purchase Elite at store.auroramc.net to unlock these Server Messages!", true, Material.NAME_TAG, (short)0);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** is ready to &c&lP&e&lA&a&lR&b&lT&d&lY&6&l!", player.getPlayer().getName());
    }

    @Override
    public String onLeave(AuroraMCPlayer player) {
        return String.format("**%s** has left the game.");
    }
}
