/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.api.player;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.proxy.api.ProxyAPI;

import java.util.UUID;

public class Session {

    private final AuroraMCProxyPlayer player;
    private final UUID sessionUUID;
    private final long startTimestamp;

    public Session(AuroraMCProxyPlayer player) {
        sessionUUID = UUID.randomUUID();
        startTimestamp = System.currentTimeMillis();
        this.player = player;

        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
           AuroraMCAPI.getDbManager().newSession(sessionUUID, player.getUniqueId(), player.getId(), startTimestamp);
           AuroraMCAPI.getDbManager().setProxyUUID(player.getUniqueId(), UUID.fromString(AuroraMCAPI.getInfo().getName()));
        });
    }

    public UUID getSessionUUID() {
        return sessionUUID;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public AuroraMCProxyPlayer getPlayer() {
        return player;
    }

    public synchronized void currentServer(String server) {
        Session session = this;
        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
            AuroraMCAPI.getDbManager().updateServer(player.getUniqueId(), server);
        });
    }

    public void endSession() {
        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
            if (player.getStats() == null) {
                AuroraMCAPI.getDbManager().sessionEndDisconnected(player.getUniqueId(), player.getId(), Session.this.sessionUUID);
            } else {
                AuroraMCAPI.getDbManager().sessionEnd(player.getUniqueId(), player.getId(), player.getStats(), Session.this.sessionUUID);
            }
        });
    }
}
