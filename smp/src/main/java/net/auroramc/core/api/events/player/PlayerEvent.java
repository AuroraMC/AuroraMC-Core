/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Event;

public abstract class PlayerEvent extends Event {

    private final AuroraMCServerPlayer player;

    public PlayerEvent(AuroraMCServerPlayer player) {
        super(false);
        this.player = player;
    }

    public PlayerEvent(AuroraMCServerPlayer player, boolean isAsync) {
        super(isAsync);
        this.player = player;
    }


    public AuroraMCServerPlayer getPlayer() {
        return player;
    }
}
