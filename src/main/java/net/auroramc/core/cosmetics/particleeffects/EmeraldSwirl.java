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

public class EmeraldSwirl extends ParticleEffect {

    private Map<UUID, BukkitTask> tasks;

    public EmeraldSwirl() {
        super(901, "Emerald Swirl", "&a&lEmerald Swirl", "An Emerald swirl.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Particle Effect!", true, Material.EMERALD, (short)0, Rarity.MYTHICAL);
        tasks = new HashMap<>();
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        tasks.put(player.getPlayer().getUniqueId(), new BukkitRunnable(){

            double t = 0;
            double yt = 0;
            @Override
            public void run() {
                Location location = player.getPlayer().getLocation();
                double x = (Math.cos(t)) + location.getX();
                double y = (Math.cos(yt)) + location.getY() + 1;
                double z = (location.getZ() + Math.sin(t));

                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, false, (float) x, (float) y, (float) z, 0, 0, 0, 1, 0);
                for (AuroraMCPlayer pl : AuroraMCAPI.getPlayers()) {
                    if (pl.getPlayer().getWorld().equals(player.getPlayer().getWorld())) {
                        ((CraftPlayer) pl.getPlayer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
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
