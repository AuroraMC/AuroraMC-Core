/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.executors.wineffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class EggsplosionExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public EggsplosionExecutor() {
        super(AuroraMCAPI.getCosmetics().get(602));
    }

    @Override
    public void execute(AuroraMCPlayer pl) {
        AuroraMCServerPlayer player = (AuroraMCServerPlayer) pl;
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                if (i <= 90) {
                    Egg egg = player.getLocation().getWorld().spawn(player.getLocation().add(0, 2.5, 0), Egg.class);
                    egg.setVelocity(new Vector(0, 4, 0).normalize());
                    egg.getLocation().getWorld().playSound(egg.getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 0);
                    i++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(ServerAPI.getCore(), 0, 2);
    }

    @Override
    public void execute(AuroraMCPlayer player, double x, double y, double z) {
    }

    @Override
    public void execute(Object entity) {
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
