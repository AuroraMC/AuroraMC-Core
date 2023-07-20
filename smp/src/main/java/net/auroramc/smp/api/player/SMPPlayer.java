/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.player;

import net.auroramc.api.permissions.Rank;

import java.util.UUID;

public class SMPPlayer {

    private final int id;
    private final String name;
    private final UUID uuid;
    private AuroraMCServerPlayer player;
    private Rank rank;

    public SMPPlayer(int id, String name, UUID uuid, AuroraMCServerPlayer player, Rank rank) {
        this.name = name;
        this.uuid = uuid;
        this.player = player;
        this.id = id;
        this.rank = rank;
    }

    public AuroraMCServerPlayer getPlayer() {
        return player;
    }

    public String getName() {
        return name;
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

    public void setPlayer(AuroraMCServerPlayer player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMPPlayer that = (SMPPlayer) o;
        return id == that.id && uuid.equals(that.uuid);
    }

}
