/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.servermessages;

import net.auroramc.core.api.cosmetics.ServerMessage;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Material;

import java.util.Collections;

public class Murderer extends ServerMessage {
    public Murderer() {
        super(403, "&bMurderer", "&3&lMurderer", "Some description.", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "Subscribe to Plus at store.auroramc.net to unlock these Server Messages!", true, Material.NAME_TAG, (short)0, Rarity.COMMON);
    }

    @Override
    public String onJoin(AuroraMCPlayer player) {
        return String.format("**%s** wants to &c&lKILL!", player.getPlayer().getName());
    }

    @Override
    public String onLeave(AuroraMCPlayer player) {
        return String.format("**%s** has finished their &c&lRAMPAGE.", player.getPlayer().getName());
    }
}
