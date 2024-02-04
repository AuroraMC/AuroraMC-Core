/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.executors.deatheffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworkExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public FireworkExecutor() {
        super(AuroraMCAPI.getCosmetics().get(700));
    }

    @Override
    public void execute(AuroraMCPlayer player) {
        AuroraMCServerPlayer pl = (AuroraMCServerPlayer) player;
        org.bukkit.entity.Firework firework = pl.getLocation().getWorld().spawn(pl.getLocation(), org.bukkit.entity.Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(random.nextInt(256),random.nextInt(256),random.nextInt(256))).trail(random.nextBoolean()).flicker(random.nextBoolean()).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]).build());
        firework.setFireworkMeta(meta);
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
