/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;

import java.util.Random;
import java.util.UUID;

public abstract class Crate {

    protected static final Random random;

    static {
        random = new Random();
    }

    private final String type;
    private final UUID uuid;
    private final int owner;
    private final long generated;
    protected long opened;
    protected CrateReward loot;

    public Crate(String type, UUID uuid, int owner, CrateReward loot, long generated, long opened) {
        this.type = type;
        this.uuid = uuid;
        this.owner = owner;
        this.loot = loot;
        this.generated = generated;
        this.opened = opened;
    }

    public CrateReward getLoot() {
        return loot;
    }


    public int getOwner() {
        return owner;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getGenerated() {
        return generated;
    }

    public long getOpened() {
        return opened;
    }

    //Generate a reward.
    public abstract CrateReward open(AuroraMCPlayer player);

    public String getType() {
        return type;
    }

    public static final class CrateReward {

        private final Cosmetic cosmetic;
        private final Rank rank;
        private final int plusDays;

        public CrateReward(Cosmetic cosmetic, Rank rank, int plusDays) {

            this.cosmetic = cosmetic;
            this.rank = rank;
            this.plusDays = plusDays;
        }

        public Cosmetic getCosmetic() {
            return cosmetic;
        }

        public int getPlusDays() {
            return plusDays;
        }

        public Rank getRank() {
            return rank;
        }
    }

}
