/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Disguise;

public class DisguiseFactory {

    public static Disguise newDisguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        return AuroraMCAPI.getAbstractedMethods().newDisguise(player, name, skin, rank);
    }

    public static Disguise newDisguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        return AuroraMCAPI.getAbstractedMethods().newDisguise(player, name, skin, signature, rank);
    }

    public static Disguise newDisguise(String name, String skin, String signature, Rank rank) {
        return AuroraMCAPI.getAbstractedMethods().newDisguise(name, skin, signature, rank);
    }

}
