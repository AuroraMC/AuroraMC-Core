/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.projectiletrails;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.ProjectileTrail;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FireworkTrail extends ProjectileTrail {
    public FireworkTrail() {
        super(1000, AuroraMCAPI.getFormatter().rainbow("Firework Trail"), AuroraMCAPI.getFormatter().rainbow("Firework Trail"), "A trail of fireworks. This one is loud. Sorry.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Projectile Trail!", true, Material.FIREWORK, (short)0, Rarity.LEGENDARY);
    }
    final Random random = new Random();

    @Override
    public void onShoot(Projectile projectile) {
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
                }.runTaskLater(AuroraMCAPI.getCore(), 2);
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 2);
    }
}
