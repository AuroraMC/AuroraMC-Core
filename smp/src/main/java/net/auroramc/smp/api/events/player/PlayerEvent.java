/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
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
