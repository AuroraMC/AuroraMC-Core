/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (AuroraMCAPI.getPlayer(e.getPlayer()) != null && !e.getFrom().equals(e.getTo())) {
            AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
            if (!player.hasMoved()) {
                player.moved();
                for (EntityPlayer player2 : AuroraMCAPI.getFakePlayers().values()) {
                    PlayerConnection con = ((CraftPlayer) e.getPlayer().getPlayer()).getHandle().playerConnection;
                    con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player2));
                    con.sendPacket(new PacketPlayOutNamedEntitySpawn(player2));
                    con.sendPacket(new PacketPlayOutEntityHeadRotation(player2, (byte) ((player2.yaw * 256.0F) / 360.0F)));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, player2));
                        }
                    }.runTaskLater(AuroraMCAPI.getCore(), 40);
                }
            } else if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        for (Hologram hologram : new ArrayList<>(AuroraMCAPI.getHolograms().values())) {
                            hologram.moveCheck(player);
                        }
                    }
                }.runTask(AuroraMCAPI.getCore());
            }
        }
    }

}
