/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.executors.deatheffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LayAnEggExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public LayAnEggExecutor() {
        super(AuroraMCAPI.getCosmetics().get(701));
    }

    @Override
    public void execute(AuroraMCPlayer player) {
        AuroraMCServerPlayer pl = (AuroraMCServerPlayer) player;
        Item firework = pl.getLocation().getWorld().dropItemNaturally(pl.getLocation(), new ItemStack(Material.EGG));
        firework.setPickupDelay(10000);
        pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.CHICKEN_EGG_POP, 100, 0);
        new BukkitRunnable(){
            @Override
            public void run() {
                firework.remove();
            }
        }.runTaskLater(ServerAPI.getCore(), 200);
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
