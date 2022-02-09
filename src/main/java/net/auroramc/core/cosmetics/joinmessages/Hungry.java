/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.joinmessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class Hungry extends ServerMessage {
    public Hungry() {
        super(402, "&bHungry", "&3&lHungry", "Show off your hunger to win with these server messages!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MASTER), "Purchase Master at store.auroramc.net to unlock these Server Messages!", true, Material.NAME_TAG, (short)0);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** is hungry for &d&lVICTORY!", player.getPlayer().getName());
    }

    @Override
    public String onLeave(AuroraMCPlayer player) {
        return String.format("**%s** has left the game.", player.getPlayer().getName());
    }
}
