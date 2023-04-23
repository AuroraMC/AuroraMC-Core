/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.TabCompleteInjector;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.auroramc.api.AuroraMCAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (ServerAPI.getPlayer(e.getPlayer()) == null) {
            return;
        }
        if (ServerAPI.getPlayer(e.getPlayer()).getActiveReportTask() != null) {
            ServerAPI.getPlayer(e.getPlayer()).getActiveReportTask().cancel();
        }
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        player.clearScoreboard();
        ServerAPI.playerLeave(e.getPlayer());
        try {
            for (Cosmetic cosmetic : player.getActiveCosmetics().values()) {
                cosmetic.onUnequip(player);
            }
            TabCompleteInjector.removePlayer(e.getPlayer());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name() + "\n" + ((ServerInfo)AuroraMCAPI.getInfo()).getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);

        e.setQuitMessage(null);
        for (Hologram hologram : ServerAPI.getHolograms().values()) {
            hologram.onLeave(player);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (ServerAPI.getPlayer(e.getPlayer()) == null) {
            return;
        }
        if (ServerAPI.getPlayer(e.getPlayer()).getActiveReportTask() != null) {
            ServerAPI.getPlayer(e.getPlayer()).getActiveReportTask().cancel();
        }

        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        player.clearScoreboard();
        ServerAPI.playerLeave(e.getPlayer());
        try {
            for (Cosmetic cosmetic : player.getActiveCosmetics().values()) {
                cosmetic.onUnequip(player);
            }
            TabCompleteInjector.removePlayer(e.getPlayer());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name() + "\n" + ((ServerInfo)AuroraMCAPI.getInfo()).getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);

        e.setLeaveMessage(null);
        for (Hologram hologram : ServerAPI.getHolograms().values()) {
            hologram.onLeave(player);
        }
    }

}
