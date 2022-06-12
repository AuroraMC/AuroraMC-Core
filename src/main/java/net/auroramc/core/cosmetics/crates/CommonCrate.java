/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.crates;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.Crate;
import net.auroramc.core.api.players.AuroraMCPlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommonCrate extends Crate {

    public CommonCrate(UUID uuid, int owner, CrateReward loot, long generated, long opened) {
        super("COMMON", uuid, owner, loot, generated, opened);
    }

    @Override
    public CrateReward open(AuroraMCPlayer player) {
        int value = random.nextInt(100);
        Cosmetic.Rarity r = Cosmetic.Rarity.COMMON;
        if (value < 1) {
            r = Cosmetic.Rarity.LEGENDARY;
        } else if (value < 5) {
            r = Cosmetic.Rarity.EPIC;
        } else if (value < 13) {
            r = Cosmetic.Rarity.RARE;
        } else if (value < 29) {
            r = Cosmetic.Rarity.UNCOMMON;
        }
        Cosmetic.Rarity rarity = r;
        List<Cosmetic> cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == rarity).collect(Collectors.toList());
        this.loot = new CrateReward(cosmetics.get(random.nextInt(cosmetics.size())), null, 0);
        return loot;
    }
}
