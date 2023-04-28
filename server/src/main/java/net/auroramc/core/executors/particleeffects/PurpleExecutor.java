/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.executors.particleeffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PurpleExecutor extends CosmeticExecutor {

    private final static int SWIRLS = 4;

    private Map<UUID, BukkitTask> tasks;

    public PurpleExecutor() {
        super(AuroraMCAPI.getCosmetics().get(902));
        tasks = new HashMap<>();
    }

    @Override
    public void execute(AuroraMCPlayer pl) {
        AuroraMCServerPlayer player = (AuroraMCServerPlayer) pl;
        tasks.put(player.getUniqueId(), new BukkitRunnable(){

            double t = 0;
            double yt = 0;
            @Override
            public void run() {
                Location location = player.getLocation();
                for (int i = 0;i < SWIRLS;i++) {
                    double x = (Math.cos(t)) + location.getX();
                    double y = (Math.cos(yt)) + location.getY() + 1;
                    double z = (location.getZ() + Math.sin(t));

                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.PORTAL, false, (float) x, (float) y, (float) z, 0, 0, 0, 1, 0);
                    for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                        if (pl.getWorld().equals(player.getWorld())) {
                            pl.getCraft().getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                }

                t += 0.1;
                yt += 0.05;
                if (t > 4 * Math.PI) {
                    t = 0;
                }
                if (yt > 4 * Math.PI) {
                    yt = 0;
                }
            }
        }.runTaskTimer(ServerAPI.getCore(), 1, 1));
    }

    @Override
    public void execute(AuroraMCPlayer player, double x, double y, double z) {
    }

    @Override
    public void execute(Object entity) {
    }

    @Override
    public void cancel(AuroraMCPlayer player) {
        if (tasks.containsKey(player.getUniqueId())) {
            tasks.get(player.getUniqueId()).cancel();
            tasks.remove(player.getUniqueId());
        }
    }
}
