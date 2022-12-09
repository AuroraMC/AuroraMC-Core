/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
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
import java.util.Random;

public class EmeraldTrail extends ProjectileTrail {
    public EmeraldTrail() {
        super(1001, "Emerald Trail", AuroraMCAPI.getFormatter().convert("&a&lEmerald Trail"), "A trail of Emeralds. Kinda. Sort of.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Projectile Trail!", true, Material.EMERALD, (short)0, Rarity.EPIC);
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
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, false, (float) projectile.getLocation().getX(), (float) projectile.getLocation().getY(), (float) projectile.getLocation().getZ(), 0, 0, 0, 1, 0);
                for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
                    if (pl.getPlayer().getWorld().equals(projectile.getWorld())) {
                        ((CraftPlayer) pl.getPlayer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 1);
    }
}
