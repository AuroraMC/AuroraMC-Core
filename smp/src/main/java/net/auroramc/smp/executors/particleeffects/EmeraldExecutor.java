/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.executors.particleeffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmeraldExecutor extends CosmeticExecutor {

    private final static int SWIRLS = 4;

    private Map<UUID, BukkitTask> tasks;

    public EmeraldExecutor() {
        super(AuroraMCAPI.getCosmetics().get(901));
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


                    location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
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
