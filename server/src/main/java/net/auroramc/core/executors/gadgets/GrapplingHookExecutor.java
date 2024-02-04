/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.executors.gadgets;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerFishEvent;
import net.auroramc.core.api.events.player.PlayerUseCosmeticEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Random;

public class GrapplingHookExecutor extends CosmeticExecutor implements Listener {

    private final Random random = new Random();

    public GrapplingHookExecutor() {
        super(AuroraMCAPI.getCosmetics().get(801));
        Bukkit.getPluginManager().registerEvents(this, ServerAPI.getCore());
    }

    @Override
    public void execute(AuroraMCPlayer player) {
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

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        AuroraMCServerPlayer player = e.getPlayer();
        PlayerUseCosmeticEvent event = new PlayerUseCosmeticEvent(player, this.getCosmetic());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if (this.getCosmetic().equals(player.getActiveCosmetics().get(Cosmetic.CosmeticType.GADGET)) && e.getState() != PlayerFishEvent.State.FISHING) {
            Vector vector = e.getHook().getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(3);
            player.setVelocity(vector);
        }
    }
}
