/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.nova;

import net.auroramc.api.nova.NovaAntiCheat;
import net.auroramc.api.nova.NovaCheck;
import net.auroramc.api.nova.Violation;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.block.BlockBreakEvent;
import net.minecraft.network.protocol.game.PacketPlayInChat;
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
