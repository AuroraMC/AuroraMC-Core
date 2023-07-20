/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;

public class NightSkipListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        AuroraMCServerPlayer pl = ServerAPI.getPlayer(e.getPlayer());
        if (pl == null || pl.isVanished()) {
            e.setUseBed(Event.Result.DENY);
            return;
        }
        int amount = 0;
        double total = 0;
        if (e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            amount++;
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                if (!player.isVanished()) {
                    total++;
                    if (player.isSleeping()) {
                        amount++;
                    }
                }
            }
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "**" + ServerAPI.getPlayer(e.getPlayer()).getByDisguiseName() + "** is now sleeping! (**" + String.format("%.2f", ((amount / total)*100)) + "%**)"));
            }
        }
    }

    @EventHandler
    public void onSkip(TimeSkipEvent e) {
        if (e.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "The night has been skipped!"));
            }
        }
    }

    @EventHandler
    public void onSleep(PlayerBedLeaveEvent e) {
        AuroraMCServerPlayer pl = ServerAPI.getPlayer(e.getPlayer());
        if (pl == null || pl.isVanished()) {
            return;
        }
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "**" + ServerAPI.getPlayer(e.getPlayer()).getByDisguiseName() + "** is no longer sleeping!"));
        }
    }

}
