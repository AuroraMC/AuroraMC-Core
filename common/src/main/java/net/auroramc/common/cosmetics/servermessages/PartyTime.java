/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.servermessages;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.ServerMessage;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;


import java.util.Collections;

public class PartyTime extends ServerMessage {
    public PartyTime() {
        super(401, TextFormatter.rainbowBold("Party Time").toLegacyText(), TextFormatter.rainbowBold("Party Time").toLegacyText(), "Are you ready to party? Show that off!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock these Server Messages!", true, "NAME_TAG", (short)0, Rarity.LEGENDARY);
    }

    @Override
    public String onJoin(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** is ready to &c&lP&e&lA&a&lR&b&lT&d&lY&6&l!", name);
    }

    @Override
    public String onLeave(AuroraMCPlayer recipient, AuroraMCPlayer player) {
        String name = player.getName();
        if (recipient.equals(player)) {
            if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                name = player.getName();
            }
        }
        return String.format("**%s** is continuing to &c&lP&e&lA&a&lR&b&lT&d&lY&r elsewhere.", name);
    }
}
