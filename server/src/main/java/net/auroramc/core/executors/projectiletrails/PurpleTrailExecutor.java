/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.executors.projectiletrails;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
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
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.PORTAL, false, (float) projectile.getLocation().getX(), (float) projectile.getLocation().getY(), (float) projectile.getLocation().getZ(), 0, 0, 0, 1, 0);
                for (AuroraMCServerPlayer pl : ServerAPI.getPlayers()) {
                    if (pl.getWorld().equals(projectile.getWorld())) {
                        pl.getCraft().getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }.runTaskTimer(ServerAPI.getCore(), 0, 1);
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
