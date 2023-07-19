/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.listeners;

import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.events.player.PlayerMoveEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.holograms.Hologram;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() != null && !e.getFrom().equals(e.getTo())) {
            AuroraMCServerPlayer player = e.getPlayer();
            if (!player.hasMoved()) {
                player.moved();
                for (EntityPlayer player2 : ServerAPI.getFakePlayers().values()) {
                    player2.aj().a(DataWatcherRegistry.a.a(10), (byte)127);
                    PlayerConnection con = e.getPlayer().getCraft().getHandle().c;
                    con.a(ClientboundPlayerInfoUpdatePacket.a(List.of(player2)));
                    con.a(new PacketPlayOutNamedEntitySpawn(player2));
                    con.a(new PacketPlayOutEntityHeadRotation(player2, (byte) ((player2.getBukkitYaw() * 256.0F) / 360.0F)));
                    con.a(new PacketPlayOutEntityMetadata(player2.af(), player2.aj().b()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            con.a(new ClientboundPlayerInfoRemovePacket(List.of(player2.ct())));
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
