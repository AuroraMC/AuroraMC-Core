/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.crates;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.Crate;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EmeraldCrate extends Crate {

    public EmeraldCrate(UUID uuid, int owner, CrateReward loot, long generated, long opened) {
        super("EMERALD", uuid, owner, loot, generated, opened);
    }

    @Override
    public CrateReward open(AuroraMCPlayer player) {
        if (AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE).allMatch(cosmetic -> player.getUnlockedCosmetics().contains(cosmetic))) {
            //No reward can be generated as there are no rewards left.
            return null;
        }
        int value = random.nextInt(2000);
            Cosmetic.Rarity r = Cosmetic.Rarity.COMMON;
            if (value < 101 && AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == Cosmetic.Rarity.LEGENDARY).anyMatch(cosmetic -> !player.getUnlockedCosmetics().contains(cosmetic))) {
                r = Cosmetic.Rarity.LEGENDARY;
            } else if (value < 341 && AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == Cosmetic.Rarity.EPIC).anyMatch(cosmetic -> !player.getUnlockedCosmetics().contains(cosmetic))) {
                r = Cosmetic.Rarity.EPIC;
            } else if (value < 741 && AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == Cosmetic.Rarity.RARE).anyMatch(cosmetic -> !player.getUnlockedCosmetics().contains(cosmetic))) {
                r = Cosmetic.Rarity.RARE;
            } else if (value < 1360 && AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == Cosmetic.Rarity.UNCOMMON).anyMatch(cosmetic -> !player.getUnlockedCosmetics().contains(cosmetic))) {
                r = Cosmetic.Rarity.UNCOMMON;
            }
            List<Cosmetic> cosmetics;
            {
                Cosmetic.Rarity rarity = r;
                cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == rarity && !player.getUnlockedCosmetics().contains(cosmetic)).collect(Collectors.toList());
            }
            if (cosmetics.size() > 0) {
                return new CrateReward(cosmetics.get(random.nextInt(cosmetics.size())), null, 0);
            } else {
                while (r != Cosmetic.Rarity.LEGENDARY) {
                    switch (r) {
                        case COMMON:
                            r = Cosmetic.Rarity.UNCOMMON;
                            break;
                        case UNCOMMON:
                            r = Cosmetic.Rarity.RARE;
                            break;
                        case RARE:
                            r = Cosmetic.Rarity.EPIC;
                            break;
                        case EPIC:
                            r = Cosmetic.Rarity.LEGENDARY;
                            break;
                    }
                    Cosmetic.Rarity rarity = r;
                    cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getUnlockMode() == Cosmetic.UnlockMode.CRATE && cosmetic.getRarity() == rarity && !player.getUnlockedCosmetics().contains(cosmetic)).collect(Collectors.toList());
                    if (cosmetics.size() > 0) {
                        return new CrateReward(cosmetics.get(random.nextInt(cosmetics.size())), null, 0);
                    }
                }
                //For whatever reason, a reward could not be generated.
                return null;
            }
    }
}
