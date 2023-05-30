/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.auroramc.core.api.events.block.BlockPlaceEvent;
import net.auroramc.core.api.player.SMPPlayer;
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
    public void onBuild(BlockPlaceEvent e) {
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
