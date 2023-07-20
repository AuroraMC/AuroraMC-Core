/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.utils;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.BlockLogEvent;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.smp.api.ServerAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BlockLogRollback {

    private Location source;
    private int radius;
    private int hours;

    private int x;
    private int y;
    private int z;

    private int blocksScanned;
    private int blocksToScan;

    public BlockLogRollback(Location source, int radius, int hours) {
        this.source = source;
        this.radius = radius;
        this.hours = hours;

        this.x = source.getBlockX() - radius;
        this.y = 319;
        this.z = source.getBlockZ() - radius;

        blocksToScan = radius*radius*4*384;
    }

    public void rollback() {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (;x < source.getBlockX() + radius;x++) {
                    for (; z < source.getBlockZ() + radius; z++) {
                        for (; y >= -64; y--) {
                            List<BlockLogEvent> events = AuroraMCAPI.getDbManager().getBlockLog(x, y, z, SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), ServerAPI.getLimit(), hours);
                            if (events.size() > 0) {
                                //Get the last event log in the list - its the oldest and therefore we need to return it to before this point.
                                BlockLogEvent event = events.get(events.size() - 1);
                                switch (event.getType()) {
                                    case BURN, BREAK, EXPLODE, FADE, DECAY, PRIME -> {
                                        //Place the block back.
                                        Material material = Material.valueOf(event.getMaterial());
                                        //Location location = new Location(Bukkit.getWorld());

                                    }
                                    case FORM, PLACE, FERTILIZE , SPREAD -> {

                                    }
                                }
                            }
                        }
                        y = 320;
                    }
                    z = source.getBlockZ() - radius;
                }

            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

}
