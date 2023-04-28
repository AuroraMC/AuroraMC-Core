/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

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

    public void opened(CrateReward reward) {
        this.opened = System.currentTimeMillis();
        this.loot = reward;
        Crate crate = this;
        ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setOpened(crate));
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

        @Override
        public String toString() {
            if (cosmetic != null) {
                return "cosmetic:" + cosmetic.getId();
            } else if (rank != null) {
                return "rank:" + rank.getId();
            } else {
                return "plus:" + plusDays;
            }
        }

        public String getRewardTitle() {
            if (cosmetic != null) {
                return cosmetic.getName() + "&b Cosmetic";
            } else if (rank != null) {
                return rank.getName() + "&b Rank";
            } else {
                return plusDays + " Plus Days";
            }
        }
    }

}
