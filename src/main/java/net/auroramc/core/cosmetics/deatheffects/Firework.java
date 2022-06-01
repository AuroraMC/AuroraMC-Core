/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.deatheffects;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.DeathEffect;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collections;
import java.util.Random;

public class Firework extends DeathEffect {

    final Random random = new Random();

    public Firework() {
        super(700, AuroraMCAPI.getFormatter().rainbow("Firework"), AuroraMCAPI.getFormatter().rainbow("Firework"), "", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Death Effect!", true, Material.FIREWORK, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public void onDeath(AuroraMCPlayer player) {
        org.bukkit.entity.Firework firework = player.getPlayer().getLocation().getWorld().spawn(player.getPlayer().getLocation(), org.bukkit.entity.Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(random.nextInt(256),random.nextInt(256),random.nextInt(256))).trail(random.nextBoolean()).flicker(random.nextBoolean()).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]).build());
        firework.setFireworkMeta(meta);
    }
}
