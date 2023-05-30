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
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FireworkTrailExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public FireworkTrailExecutor() {
        super(AuroraMCAPI.getCosmetics().get(1000));
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
                Location location = projectile.getLocation();
                org.bukkit.entity.Firework firework = location.getWorld().spawn(location, org.bukkit.entity.Firework.class);
                firework.setVelocity(projectile.getVelocity());
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(0);
                meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(random.nextInt(256),random.nextInt(256),random.nextInt(256))).trail(random.nextBoolean()).flicker(random.nextBoolean()).with(FireworkEffect.Type.BURST).build());
                firework.setFireworkMeta(meta);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        firework.detonate();
                    }
                }.runTaskLater(ServerAPI.getCore(), 2);
            }
        }.runTaskTimer(ServerAPI.getCore(), 0, 1);
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
