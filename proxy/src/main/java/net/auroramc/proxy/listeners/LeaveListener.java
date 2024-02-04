/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.listeners;

import net.auroramc.proxy.api.ProxyAPI;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        ProxyAPI.playerLeave(ProxyAPI.getPlayer(e.getPlayer()));
    }

}
