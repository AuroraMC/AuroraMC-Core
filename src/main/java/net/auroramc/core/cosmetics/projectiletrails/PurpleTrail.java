/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.projectiletrails;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.ProjectileTrail;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PurpleTrail extends ProjectileTrail {
    public PurpleTrail() {
        super(1002, "Purple Trail", "&5&lPurple Trail", "Purple. That's it. Just purple.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", true, Material.EYE_OF_ENDER, (short)0, Rarity.EPIC);
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
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.PORTAL, false, (float) projectile.getLocation().getX(), (float) projectile.getLocation().getY(), (float) projectile.getLocation().getZ(), 0, 0, 0, 5, 5);
                for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
                    if (pl.getPlayer().getWorld().equals(projectile.getWorld())) {
                        ((CraftPlayer) pl.getPlayer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 1);
    }
}
