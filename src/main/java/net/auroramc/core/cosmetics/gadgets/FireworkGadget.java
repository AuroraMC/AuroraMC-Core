/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.gadgets;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Gadget;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collections;
import java.util.Random;

public class FireworkGadget extends Gadget {
    public FireworkGadget() {
        super(800, AuroraMCAPI.getFormatter().rainbow("Firework Gadget"), AuroraMCAPI.getFormatter().rainbow("Firework Gadget"), "&oYou're actually giving us permission to do this? &7That is correct, Longbottom. &oTo blow it up? Boom? &7BOOM!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Gadget!", true, Material.FIREWORK, (short)0, Rarity.LEGENDARY, 2);
    }

    final Random random = new Random();

    @Override
    public void onUse(AuroraMCPlayer player, Location location) {
        org.bukkit.entity.Firework firework = location.getWorld().spawn(location, org.bukkit.entity.Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(random.nextInt(256),random.nextInt(256),random.nextInt(256))).trail(random.nextBoolean()).flicker(random.nextBoolean()).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]).build());
        firework.setFireworkMeta(meta);
    }


    @Override
    public void onEquip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setItem(3, new GUIItem(Material.FIREWORK, AuroraMCAPI.getFormatter().rainbowBold("Firework Gadget"), 1).getItem());
    }
}
