/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.crates;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.Crate;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DiamondCrate extends Crate {

    public DiamondCrate(UUID uuid, int owner, CrateReward loot, long generated, long opened) {
        super("DIAMOND", uuid, owner, loot, generated, opened);
    }

    @Override
    public CrateReward open(AuroraMCPlayer player) {
        int value = random.nextInt(2000);
        Cosmetic.Rarity r = Cosmetic.Rarity.COMMON;
        if (value < 1) {
            if (player.hasPermission("master")) {
                if (player.hasPermission("plus")) {
                    r = Cosmetic.Rarity.LEGENDARY;
                } else {
                    this.loot = new CrateReward(null, null, 7);
                    return loot;
                }
            } else if (player.hasPermission("elite")) {
                this.loot = new CrateReward(null, Rank.MASTER, 0);
                return loot;
            } else {
                this.loot = new CrateReward(null, Rank.ELITE, 0);
                return loot;
            }
        } else if (value < 101) {
            r = Cosmetic.Rarity.LEGENDARY;
        } else if (value < 341) {
            r = Cosmetic.Rarity.EPIC;
        } else if (value < 741) {
            r = Cosmetic.Rarity.RARE;
        } else if (value < 1360) {
            r = Cosmetic.Rarity.UNCOMMON;
        }
        Cosmetic.Rarity rarity = r;
        List<Cosmetic> cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == rarity).collect(Collectors.toList());
        this.loot = new CrateReward(cosmetics.get(random.nextInt(cosmetics.size())), null, 0);
        return loot;
    }
}
