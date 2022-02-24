/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.servermessages;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class PartyTime extends ServerMessage {
    public PartyTime() {
        super(401, AuroraMCAPI.getFormatter().rainbowBold("Party Time"), AuroraMCAPI.getFormatter().rainbowBold("Party Time"), "Are you ready to party? Show that off!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock these Server Messages!", true, Material.NAME_TAG, (short)0, Rarity.COMMON);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** is ready to &c&lP&e&lA&a&lR&b&lT&d&lY&6&l!", player.getPlayer().getName());
    }

    @Override
    public String onLeave(AuroraMCPlayer player) {
        return String.format("**%s** is continuing to &c&lP&e&lA&a&lR&b&lT&d&lY&r elsewhere.", player.getPlayer().getName());
    }
}
