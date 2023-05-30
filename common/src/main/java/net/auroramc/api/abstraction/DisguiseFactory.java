/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
