/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.gadgets;

import net.auroramc.core.api.cosmetics.Gadget;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class GrapplingHook extends Gadget {

    /*private static*/

    public GrapplingHook() {
        super(801, "Grappling Hook", "&c&lGrappling Hook", "Spiderman, Spiderman, does whatever a Spiderman does.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "You are now a chicken. You will lay an egg when you die.", true, Material.FISHING_ROD, (short)0, Rarity.EPIC, 10);
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
    }

    @Override
    public void onUse(AuroraMCPlayer player, Location location) {
        /*player.getPlayer().
        if (player.) {

        }*/
    }

}
