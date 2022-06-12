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

public class GoldCrate extends Crate {

    public GoldCrate(UUID uuid, int owner, CrateReward loot, long generated, long opened) {
        super("GOLD", uuid, owner, loot, generated, opened);
    }

    @Override
    public CrateReward open(AuroraMCPlayer player) {
        int value = random.nextInt(100);
        Cosmetic.Rarity r = Cosmetic.Rarity.COMMON;
        if (value < 4) {
            r = Cosmetic.Rarity.LEGENDARY;
        } else if (value < 12) {
            r = Cosmetic.Rarity.EPIC;
        } else if (value < 26) {
            r = Cosmetic.Rarity.RARE;
        } else if (value < 50) {
            r = Cosmetic.Rarity.UNCOMMON;
        }
        Cosmetic.Rarity rarity = r;
        List<Cosmetic> cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == rarity).collect(Collectors.toList());
        this.loot = new CrateReward(cosmetics.get(random.nextInt(cosmetics.size())), null, 0);
        return loot;
    }
}
