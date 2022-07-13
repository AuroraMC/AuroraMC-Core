/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.deatheffects;

import net.auroramc.core.AuroraMC;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.DeathEffect;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Random;

public class LayAnEgg extends DeathEffect {

    final Random random = new Random();

    public LayAnEgg() {
        super(701, "Lay an Egg", "&e&lLay an Egg", "You are now a chicken. You will lay an egg when you die.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Death Effect!", true, Material.EGG, (short)0, Rarity.EPIC);
    }

    @Override
    public void onDeath(AuroraMCPlayer player) {
        Item firework = player.getPlayer().getLocation().getWorld().spawn(player.getPlayer().getLocation(), Item.class);
        firework.setItemStack(new ItemStack(Material.EGG));
        firework.setPickupDelay(10000);
        player.getPlayer().getLocation().getWorld().playSound(player.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 100, 0);
        new BukkitRunnable(){
            @Override
            public void run() {
                firework.remove();
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 200);
    }
}
