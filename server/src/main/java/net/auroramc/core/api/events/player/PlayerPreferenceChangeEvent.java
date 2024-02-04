/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.HandlerList;

public class PlayerPreferenceChangeEvent extends PlayerEvent {

    public static HandlerList handlerList = new HandlerList();


    public PlayerPreferenceChangeEvent(AuroraMCServerPlayer player) {
        super(player);
    }

    public PlayerPreferenceChangeEvent(AuroraMCServerPlayer player, boolean isAsync) {
        super(player, isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
