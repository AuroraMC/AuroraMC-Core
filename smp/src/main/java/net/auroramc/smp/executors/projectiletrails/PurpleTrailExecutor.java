/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.executors.projectiletrails;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.smp.api.ServerAPI;
import org.bukkit.Particle;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class PurpleTrailExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public PurpleTrailExecutor() {
        super(AuroraMCAPI.getCosmetics().get(1001));
    }

    @Override
    public void execute(AuroraMCPlayer player) {
    }

    @Override
    public void execute(AuroraMCPlayer player, double x, double y, double z) {
    }

    @Override
    public void execute(Object entity) {
        Projectile projectile = (Projectile) entity;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (projectile.isDead() || projectile.isOnGround()) {
                    this.cancel();
                    return;
                }
                projectile.getWorld().spawnParticle(Particle.PORTAL, projectile.getLocation(), 1);
            }
        }.runTaskTimer(ServerAPI.getCore(), 0, 1);
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
