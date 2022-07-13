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
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Random;

public class Eggsplosion extends WinEffect {

    public Eggsplosion() {
        super(602, "EGGsplosion", "&7&lEGGsplosion", "What an EGGcelent end to a game!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Win Effect!", true, Material.EGG, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public void onWin(AuroraMCPlayer player) {
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                if (i <= 90) {
                    Egg egg = player.getPlayer().getLocation().getWorld().spawn(player.getPlayer().getLocation().add(0, 2, 0), Egg.class);
                    egg.setVelocity(new Vector(0, 4, 0).normalize());
                    egg.getLocation().getWorld().playSound(egg.getLocation(), Sound.CHICKEN_EGG_POP, 100, 0);
                    i++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 2);
    }
}
