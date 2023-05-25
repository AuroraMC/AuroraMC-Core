/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.auroramc.core.api.events.block.BlockPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpawnProtectionListener implements Listener {

    @EventHandler
    public void onBlockPLace(BlockPlaceEvent e) {
        Location location = e.getBlock().getLocation().clone();
        location.setY(0);
        if (location.distanceSquared(new Location(Bukkit.getWorld("smp"), 0.5, 0, 0.5)) < 10000 && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            e.setBuild(false);
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextFormatter.pluginMessage("NuttersSMP", "You cannot build here."));
        }
    }

    @EventHandler
    public void onBlockPLace(BlockBreakEvent e) {
        Location location = e.getBlock().getLocation().clone();
        location.setY(0);
        if (location.distanceSquared(new Location(Bukkit.getWorld("smp"), 0.5, 0, 0.5)) < 10000 && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextFormatter.pluginMessage("NuttersSMP", "You cannot build here."));
        }
    }
}
