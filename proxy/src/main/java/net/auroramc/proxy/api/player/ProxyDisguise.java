/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.player;

import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Disguise;

public class ProxyDisguise extends Disguise {

    public ProxyDisguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        super(player, name, skin, rank);
    }

    public ProxyDisguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        super(player, name, skin, signature, rank);
    }

    public ProxyDisguise(String name, String skin, String signature, Rank rank) {
        super(name, skin, signature, rank);
    }

    @Override
    public boolean apply(boolean update) {
        return false;
    }

    @Override
    public boolean switchDisguise() {
        return false;
    }

    @Override
    public boolean undisguise() {
        return false;
    }
}
