/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnderDragonListener implements Listener {

    private static Map<Player, Long> damage = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof EnderDragon) {
            e.setDamage(e.getDamage()*2);
        } else if (e.getEntity() instanceof EnderDragon dragon) {
            if (e.getDamager() instanceof Player pl) {
                AuroraMCServerPlayer player = ServerAPI.getPlayer(pl);
                damage.computeIfPresent(pl, (player1, damage) -> damage + Math.round(e.getFinalDamage()));
            }
        }
    }

    @EventHandler
    public void onHealth(EntitySpawnEvent e) {
        if (e.getEntity() instanceof EnderDragon) {
            double maxHealth = ((EnderDragon) e.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            ((EnderDragon) e.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth*3);

        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof EnderDragon dragon) {

            TextComponent textComponent = new TextComponent("");

            TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
            lines.setBold(true);
            lines.setColor(ChatColor.DARK_AQUA);

            textComponent.addExtra(lines);
            textComponent.addExtra("\n\n");

            TextComponent cmp = new TextComponent("The Ender Dragon has died! And the person who did the most damage was...");
            cmp.setBold(false);
            cmp.setColor(ChatColor.WHITE);
            textComponent.addExtra(cmp);
            textComponent.addExtra("\n");

            Map.Entry<Player, Long> entry = Collections.max(damage.entrySet(), Map.Entry.comparingByValue());

            cmp = new TextComponent(entry.getKey().getName());
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(true);
            textComponent.addExtra(cmp);

            cmp = new TextComponent(" with ");
            cmp.setBold(false);
            cmp.setColor(ChatColor.WHITE);
            textComponent.addExtra(cmp);

            cmp = new TextComponent(entry.getValue() + "hp");
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(true);
            textComponent.addExtra(cmp);

            cmp = new TextComponent(" dealt.");
            cmp.setBold(false);
            cmp.setColor(ChatColor.WHITE);
            textComponent.addExtra(cmp);
            textComponent.addExtra("\n\n");
            textComponent.addExtra(lines);

            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                player.sendMessage(textComponent);
            }
        }
    }

}
