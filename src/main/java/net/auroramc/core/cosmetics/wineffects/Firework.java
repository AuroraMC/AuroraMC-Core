/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.wineffects;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.WinEffect;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Random;

public class Firework extends WinEffect {

    public Firework() {
        super(600, AuroraMCAPI.getFormatter().rainbow("Firework"), AuroraMCAPI.getFormatter().rainbow("Firework"), "Some description", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the Celebration Bundle at store.auroramc.net to unlock this Win Effect!", true, Material.FIREWORK, (short)0);
    }

    @Override
    public void onWin(AuroraMCPlayer player) {
        new BukkitRunnable(){
            int i = 0;
            final Random random = new Random();
            @Override
            public void run() {
                if (i <= 18) {
                    org.bukkit.entity.Firework firework = player.getPlayer().getLocation().getWorld().spawn(player.getPlayer().getLocation(), org.bukkit.entity.Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.setPower(1);
                    meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(random.nextInt(256),random.nextInt(256),random.nextInt(256))).trail(random.nextBoolean()).flicker(random.nextBoolean()).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]).build());
                    firework.setFireworkMeta(meta);
                    i++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 10);
    }
}
