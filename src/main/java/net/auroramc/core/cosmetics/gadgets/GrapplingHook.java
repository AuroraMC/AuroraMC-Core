/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.gadgets;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Gadget;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class GrapplingHook extends Gadget implements Listener {

    /*private static*/

    public GrapplingHook() {
        super(801, "Grappling Hook", "&c&lGrappling Hook", "Spiderman, Spiderman, does whatever a Spiderman does.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Gadget!", true, Material.FISHING_ROD, (short)0, Rarity.EPIC, 10);
        Bukkit.getPluginManager().registerEvents(this, AuroraMCAPI.getCore());
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setItem(3, new GUIItem(Material.FISHING_ROD, AuroraMCAPI.getFormatter().convert("&c&lGrappling Hook"), 1).getItem());
    }

    @Override
    public void onUse(AuroraMCPlayer player, Location location) {
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
        if (this.equals(player.getActiveCosmetics().get(CosmeticType.GADGET)) && e.getState() != PlayerFishEvent.State.FISHING) {
            Vector vector = e.getHook().getLocation().toVector().subtract(player.getPlayer().getLocation().toVector()).normalize();
            player.getPlayer().setVelocity(vector);
        }
    }

}
