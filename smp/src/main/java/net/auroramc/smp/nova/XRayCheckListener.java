/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.nova;

import net.auroramc.api.nova.NovaAntiCheat;
import net.auroramc.api.nova.NovaCheck;
import net.auroramc.api.nova.Violation;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.events.block.BlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class XRayCheckListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Material material = e.getBlock().getType();
        new BukkitRunnable(){
            @Override
            public void run() {
                switch (material) {
                    case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> {
                        Violation violation = new Violation(System.currentTimeMillis(), NovaCheck.XRAY_DIAMOND, e.getPlayer());
                        NovaAntiCheat.logViolation(violation);
                    }
                    case ANCIENT_DEBRIS -> {
                        Violation violation = new Violation(System.currentTimeMillis(), NovaCheck.XRAY_NETHERITE, e.getPlayer());
                        NovaAntiCheat.logViolation(violation);
                    }
                    case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> {
                        Violation violation = new Violation(System.currentTimeMillis(), NovaCheck.XRAY_EMERALD, e.getPlayer());
                        NovaAntiCheat.logViolation(violation);
                    }
                    case GOLD_ORE, DEEPSLATE_GOLD_ORE -> {
                        Violation violation = new Violation(System.currentTimeMillis(), NovaCheck.XRAY_GOLD, e.getPlayer());
                        NovaAntiCheat.logViolation(violation);
                    }
                }
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

}
