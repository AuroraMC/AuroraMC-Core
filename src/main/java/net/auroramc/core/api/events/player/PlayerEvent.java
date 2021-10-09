/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Event;

public abstract class PlayerEvent extends Event {

    private final AuroraMCPlayer player;

    public PlayerEvent(AuroraMCPlayer player) {
        super(false);
        this.player = player;
    }

    public PlayerEvent(AuroraMCPlayer player, boolean isAsync) {
        super(isAsync);
        this.player = player;
    }


    public AuroraMCPlayer getPlayer() {
        return player;
    }
}
