/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.player.party;

import net.auroramc.api.permissions.Rank;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;

import java.util.UUID;

public class PartyPlayer {

    private final int id;
    private final String name;
    private final UUID uuid;
    private final AuroraMCProxyPlayer player;
    private final UUID proxyUUID;
    private Rank rank;

    public PartyPlayer(int id, String name, UUID uuid, AuroraMCProxyPlayer player, UUID proxyUUID, Rank rank) {
        this.name = name;
        this.uuid = uuid;
        this.player = player;
        this.proxyUUID = proxyUUID;
        this.id = id;
        this.rank = rank;
    }

    public AuroraMCProxyPlayer getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public UUID getProxyUUID() {
        return proxyUUID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartyPlayer that = (PartyPlayer) o;
        return id == that.id && uuid.equals(that.uuid);
    }
}