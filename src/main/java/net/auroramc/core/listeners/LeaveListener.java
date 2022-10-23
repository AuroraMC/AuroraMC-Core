/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.TabCompleteInjector;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.auroramc.core.api.utils.holograms.HologramLine;
import net.auroramc.core.api.utils.holograms.universal.UniversalHologramLine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (AuroraMCAPI.getPlayer(e.getPlayer()) == null) {
            return;
        }
        if (AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask() != null) {
            AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask().cancel();
        }
        AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
        player.clearScoreboard();
        AuroraMCAPI.playerLeave(e.getPlayer());
        try {
            for (Cosmetic cosmetic : player.getActiveCosmetics().values()) {
                cosmetic.onUnequip(player);
            }
            TabCompleteInjector.removePlayer(e.getPlayer());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getServerInfo().getName(), AuroraMCAPI.getServerInfo().getNetwork().name() + "\n" + AuroraMCAPI.getServerInfo().getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);

        e.setQuitMessage(null);
        for (Hologram hologram : AuroraMCAPI.getHolograms().values()) {
            hologram.onLeave(player);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (AuroraMCAPI.getPlayer(e.getPlayer()) == null) {
            return;
        }
        if (AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask() != null) {
            AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask().cancel();
        }

        AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
        player.clearScoreboard();
        AuroraMCAPI.playerLeave(e.getPlayer());
        try {
            for (Cosmetic cosmetic : player.getActiveCosmetics().values()) {
                cosmetic.onUnequip(player);
            }
            TabCompleteInjector.removePlayer(e.getPlayer());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getServerInfo().getName(), AuroraMCAPI.getServerInfo().getNetwork().name() + "\n" + AuroraMCAPI.getServerInfo().getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);

        e.setLeaveMessage(null);
        for (Hologram hologram : AuroraMCAPI.getHolograms().values()) {
            hologram.onLeave(player);
        }
    }

}
