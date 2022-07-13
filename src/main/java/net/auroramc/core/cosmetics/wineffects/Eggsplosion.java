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
import org.bukkit.entity.Egg;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Random;

public class Eggsplosion extends WinEffect {

    public Eggsplosion() {
        super(602, "EGGsplosion", "EGGsplosion", "What an EGGcelent end to a game!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Win Effect!", true, Material.FIREWORK, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public void onWin(AuroraMCPlayer player) {
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                if (i <= 36) {
                    Egg egg = player.getPlayer().getLocation().getWorld().spawn(player.getPlayer().getLocation(), Egg.class);
                    egg.setVelocity(new Vector(0, 4, 0).normalize());
                    i++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 5);
    }
}
