/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Objects;

public class PortalListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        e.setCancelled(true);
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

    @EventHandler
    public void onPortalCreate(PortalCreateEvent e) {
        if (e.getReason() == PortalCreateEvent.CreateReason.FIRE) {
            //e.setCancelled(true);
            if (e.getEntity()!=null && e.getEntity() instanceof Player) {
                AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
                player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "The nether is currently disabled!"));
            }
        }
    }

}