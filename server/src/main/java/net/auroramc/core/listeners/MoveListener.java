/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerMoveEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() != null && !e.getFrom().equals(e.getTo())) {
            AuroraMCServerPlayer player = e.getPlayer();
            if (!player.hasMoved()) {
                player.moved();
                for (EntityPlayer player2 : ServerAPI.getFakePlayers().values()) {
                    player2.getDataWatcher().watch(10, (byte)127);
                    PlayerConnection con = e.getPlayer().getCraft().getHandle().playerConnection;
                    con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player2));
                    con.sendPacket(new PacketPlayOutNamedEntitySpawn(player2));
                    con.sendPacket(new PacketPlayOutEntityHeadRotation(player2, (byte) ((player2.yaw * 256.0F) / 360.0F)));
                    con.sendPacket(new PacketPlayOutEntityMetadata(player2.getId(), player2.getDataWatcher(), true));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, player2));
                        }
                    }.runTaskLater(ServerAPI.getCore(), 40);
                }
            } else if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        for (Hologram hologram : new ArrayList<>(ServerAPI.getHolograms().values())) {
                            hologram.moveCheck(player);
                        }
                    }
                }.runTask(ServerAPI.getCore());
            }
        }
    }

}
