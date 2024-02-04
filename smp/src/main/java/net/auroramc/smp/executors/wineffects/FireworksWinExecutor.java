/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.executors.wineffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FireworksWinExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public FireworksWinExecutor() {
        super(AuroraMCAPI.getCosmetics().get(600));
    }

    @Override
    public void execute(AuroraMCPlayer pl) {
        AuroraMCServerPlayer player = (AuroraMCServerPlayer) pl;
        new BukkitRunnable(){
            int i = 0;
            final Random random = new Random();
            @Override
            public void run() {
                if (i <= 18) {
                    org.bukkit.entity.Firework firework = player.getLocation().getWorld().spawn(player.getLocation(), org.bukkit.entity.Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.setPower(0);
                    meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(random.nextInt(256),random.nextInt(256),random.nextInt(256))).trail(random.nextBoolean()).flicker(random.nextBoolean()).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]).build());
                    firework.setFireworkMeta(meta);
                    i++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(ServerAPI.getCore(), 0, 10);
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
