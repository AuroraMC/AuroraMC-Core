/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.executors.projectiletrails;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Location;
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
