/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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