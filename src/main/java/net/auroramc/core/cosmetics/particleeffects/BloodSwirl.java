/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.particleeffects;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.ParticleEffect;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodSwirl extends ParticleEffect {

    private Map<AuroraMCPlayer, BukkitTask> tasks;

    public BloodSwirl() {
        super(900, "Blood Swirl", "&c&lBlood Swirl", "A blood swirl.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Particle Effect!", true, Material.REDSTONE, (short)0, Rarity.COMMON);
        tasks = new HashMap<>();
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        tasks.put(player, new BukkitRunnable(){

            int t = 0;
            int y = 0;
            boolean up = true;
            @Override
            public void run() {
                Location location = player.getPlayer().getLocation();
                double x = (2 * Math.cos(t)) + location.getX();
                double z = (location.getZ() + 2 * Math.sin(t));

                location.getWorld().playEffect(new Location(location.getWorld(), x, y, z), Effect.COLOURED_DUST, (short)0);

                t += 0.05;
                if (up) {
                    y += 0.05;
                } else {
                    y -= 0.05;
                }
                if (t > 4*Math.PI) {
                    t = 0;
                }
                if (y > 2) {
                    up = false;
                } else if (y < 0) {
                    up = true;
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 5, 5));
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
        if (tasks.containsKey(player)) {
            tasks.get(player).cancel();
            tasks.remove(player);
        }
    }
}
