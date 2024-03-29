/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.events.block.BlockBreakEvent;
import net.auroramc.api.utils.BlockLogEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockLoggerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBuild(net.auroramc.smp.api.events.block.BlockPlaceEvent e) {
        if (e.isCancelled() || !e.canBuild()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, e.getPlayer().getUuid(), BlockLogEvent.LogType.PLACE, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, e.getPlayer().getUuid(), BlockLogEvent.LogType.BREAK, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDestroy(BlockExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, null, BlockLogEvent.LogType.EXPLODE, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onIgnite(BlockBurnEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, null, BlockLogEvent.LogType.BURN, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFade(BlockFadeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, null, BlockLogEvent.LogType.FADE, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFertilise(BlockFertilizeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, ((e.getPlayer() == null)?null:e.getPlayer().getUniqueId()), BlockLogEvent.LogType.FERTILIZE, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onForm(BlockFormEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, null, BlockLogEvent.LogType.FORM, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpread(BlockSpreadEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, null, BlockLogEvent.LogType.SPREAD, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDecay(LeavesDecayEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, null, BlockLogEvent.LogType.DECAY, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTnt(TNTPrimeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        long timestamp = System.currentTimeMillis();
        Material type = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockLogEvent event = new BlockLogEvent(timestamp, ((e.getPrimingEntity() instanceof Player)?e.getPrimingEntity().getUniqueId():null), BlockLogEvent.LogType.DECAY, new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0, null), type.name());
                AuroraMCAPI.getDbManager().logBlockEvent(event);

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

}
