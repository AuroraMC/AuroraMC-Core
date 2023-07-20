/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.events.block.BlockBreakEvent;
import net.auroramc.smp.api.events.block.BlockPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class SpawnProtectionListener implements Listener {

    @EventHandler
    public void onBlockPLace(BlockPlaceEvent e) {
        if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equalsIgnoreCase("OVERWORLD")) {
            Location location = e.getBlock().getLocation().clone();
            location.setY(0);
            if (location.distanceSquared(new Location(Bukkit.getWorld("smp"), 0.5, 0, 0.5)) < 2500 && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                e.setBuild(false);
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("NuttersSMP", "You cannot build here."));
            }
        }
    }

    @EventHandler
    public void onBlockPLace(BlockBreakEvent e) {
        if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equalsIgnoreCase("OVERWORLD")) {
            Location location = e.getBlock().getLocation().clone();
            location.setY(0);
            if (location.distanceSquared(new Location(Bukkit.getWorld("smp"), 0.5, 0, 0.5)) < 2500 && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("NuttersSMP", "You cannot build here."));
            }
        }
    }
}
