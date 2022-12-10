/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.particleeffects;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.ParticleEffect;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BloodSwirl extends ParticleEffect {

    private final static int SWIRLS = 4;

    private Map<UUID, BukkitTask> tasks;

    public BloodSwirl() {
        super(900, "Blood Swirl", "&c&lBlood Swirl", "A blood swirl.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Particle Effect!", true, Material.REDSTONE, (short)0, Rarity.LEGENDARY);
        tasks = new HashMap<>();
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        tasks.put(player.getPlayer().getUniqueId(), new BukkitRunnable(){

            double t = 0;
            @Override
            public void run() {
                Location location = player.getPlayer().getLocation();
                for (int i = 0;i < SWIRLS;i++) {
                    double x = (Math.cos(t + (((2.0 / SWIRLS)*Math.PI) * i))) + location.getX();
                    double y = (Math.cos(t) ) + location.getY() + 1;
                    double z = (location.getZ() + Math.sin(t + (((2.0 / SWIRLS)*Math.PI) * i)));

                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, false, (float)x, (float)y, (float)z, 1, 0, 0, 1, 0);
                    for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
                        if (pl.getPlayer().getWorld().equals(player.getPlayer().getWorld())) {
                            ((CraftPlayer)pl.getPlayer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                }

                t += 0.05;
                if (t > 4*Math.PI) {
                    t = 0;
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 1, 1));
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
        if (tasks.containsKey(player.getPlayer().getUniqueId())) {
            tasks.get(player.getPlayer().getUniqueId()).cancel();
            tasks.remove(player.getPlayer().getUniqueId());
        }
    }
}
