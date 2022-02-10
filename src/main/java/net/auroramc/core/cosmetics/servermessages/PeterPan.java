/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.servermessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class PeterPan extends ServerMessage {
    public PeterPan() {
        super(404, "&bPeter Pan", "&3&lPeter Pan", "Some description.", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates.", true, Material.NAME_TAG, (short)0, Rarity.COMMON);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** is ready to &e&lTAKE FLIGHT!", player.getPlayer().getName());
    }

    @Override
    public String onLeave(AuroraMCPlayer player) {
        return String.format("**%s** has left the game.", player.getPlayer().getName());
    }
}
