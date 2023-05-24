/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class NightSkipListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        int amount = 0;
        double total = 0;
        if (e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                if (!player.isVanished()) {
                    total++;
                    if (player.isSleeping()) {
                        amount++;
                    }
                }
            }
        }
        if (amount / total > 0.35) {
            e.setUseBed(Event.Result.DENY);
            e.getBed().getWorld().setFullTime(e.getBed().getWorld().getFullTime() + (24000-(e.getBed().getWorld().getFullTime() % 24000)));
        }
    }

}
