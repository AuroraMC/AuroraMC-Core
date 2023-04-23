/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Crate;

import java.util.UUID;

public class CrateFactory {

    public static Crate generateIronCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return AuroraMCAPI.getAbstractedMethods().generateIronCrate(uuid, owner, loot, generated, opened);
    }

    public static Crate generateGoldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return AuroraMCAPI.getAbstractedMethods().generateGoldCrate(uuid, owner, loot, generated, opened);
    }

    public static Crate generateDiamondCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return AuroraMCAPI.getAbstractedMethods().generateDiamondCrate(uuid, owner, loot, generated, opened);
    }

    public static Crate generateEmeraldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return AuroraMCAPI.getAbstractedMethods().generateEmeraldCrate(uuid, owner, loot, generated, opened);
    }

}
