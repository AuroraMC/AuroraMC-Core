/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public class PortalListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        e.setCancelled(true);
        Bukkit.getLogger().info("test");
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equals("OVERWORLD")) {
                out.writeUTF("SMPNether");
            } else if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equals("NETHER")) {
                out.writeUTF("SMPOverworld");
            } else {
                return;
            }

            out.writeUTF(player.getUniqueId().toString());
            player.saveData();
            player.sendPluginMessage(out.toByteArray());
        } else if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equals("OVERWORLD")) {
                out.writeUTF("SMPEnd");
            } else if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equals("END")) {
                out.writeUTF("SMPOverworld");
            } else {
                return;
            }
            out.writeUTF(player.getUniqueId().toString());
            player.saveData();
            player.sendPluginMessage(out.toByteArray());
        }
    }

}
