/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.DeathEffect;
import net.auroramc.api.cosmetics.KillMessage;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.events.entity.*;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.apache.commons.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type").equals("OVERWORLD")) {
            e.setKeepInventory(true);
            e.setKeepLevel(true);
            e.setDroppedExp(0);
        } else {
            AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getEntity());
            player.setDead(true);
            e.setKeepInventory(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(PlayerDamageEvent e) {
        AuroraMCServerPlayer player = e.getPlayer();
        if (player.isVanished()) {
            e.setCancelled(true);
            return;
        } else {
            if (e instanceof PlayerDamageByPlayerEvent) {
                AuroraMCServerPlayer damager = ((PlayerDamageByPlayerEvent) e).getDamager();
                if (damager.isVanished()) {
                    e.setCancelled(true);
                    return;
                }
            } else if (e instanceof EntityDamageByPlayerEvent) {
                return;
            }
        }
        if (e.getDamage() >= player.getHealth() && !player.isVanished()) {
            Entity entity = null;
            AuroraMCServerPlayer killer = null;
            KillMessage killMessage;
            KillMessage.KillReason killReason = KillMessage.KillReason.MELEE;
            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                killMessage = (KillMessage) player.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
            } else {
                killMessage = (KillMessage) AuroraMCAPI.getCosmetics().get(500);
            }
            if (e instanceof PlayerDamageByPlayerEvent) {

                if (e instanceof PlayerDamageByPlayerRangedEvent) {
                    killer = ((PlayerDamageByPlayerRangedEvent) e).getDamager();
                    killReason = KillMessage.KillReason.BOW;
                    ((PlayerDamageByPlayerRangedEvent) e).getProjectile().remove();
                } else {
                    killer = ((PlayerDamageByPlayerEvent) e).getDamager();
                    if (killer.isVanished()) {
                        e.setCancelled(true);
                        return;
                    }
                    killer.getStats().incrementStatistic(5, "damageDealt", Math.round(e.getDamage() * 100), true);
                    switch (e.getCause()) {
                        case PROJECTILE: {
                            killReason = KillMessage.KillReason.BOW;
                            break;
                        }
                        case VOID: {
                            killReason = KillMessage.KillReason.VOID;
                            break;
                        }
                        case FALL: {
                            killReason = KillMessage.KillReason.FALL;
                            break;
                        }
                        case BLOCK_EXPLOSION: {
                            killReason = KillMessage.KillReason.TNT;
                            break;
                        }
                    }
                }
            } else if (e instanceof PlayerDamageByEntityEvent) {
                if (((PlayerDamageByEntityEvent) e).getDamager() instanceof TNTPrimed) {
                    TNTPrimed primed = (TNTPrimed) ((PlayerDamageByEntityEvent) e).getDamager();
                    if (primed.getSource() instanceof Player) {
                        Player damager = (Player) primed.getSource();
                        killer = ServerAPI.getPlayer(damager);
                        killReason = KillMessage.KillReason.TNT;
                    }
                } else if (((PlayerDamageByEntityEvent) e).getDamager() instanceof Arrow) {
                    if (((Arrow) ((PlayerDamageByEntityEvent) e).getDamager()).getShooter() instanceof Entity) {
                        //Damage by entity.
                        entity = (Entity) ((Arrow) ((PlayerDamageByEntityEvent) e).getDamager()).getShooter();
                        killReason = KillMessage.KillReason.ENTITY;
                    }
                } else {
                    entity = ((PlayerDamageByEntityEvent) e).getDamager();
                    killReason = KillMessage.KillReason.ENTITY;
                }
            } else {
                switch (e.getCause()) {
                    case FALL: {
                        killReason = KillMessage.KillReason.FALL;
                        if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                            killer = player.getLastHitBy();
                        }
                        break;
                    }
                    case VOID: {
                        killReason = KillMessage.KillReason.VOID;
                        if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                            killer = player.getLastHitBy();
                        }
                        break;
                    }
                    case LAVA: {
                        killReason = KillMessage.KillReason.LAVA;
                        if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                            killer = player.getLastHitBy();
                        }
                        break;
                    }
                    case FIRE_TICK: {
                        killReason = KillMessage.KillReason.FIRE;
                        if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                            killer = player.getLastHitBy();
                        }
                        break;
                    }
                    case DROWNING: {
                        killReason = KillMessage.KillReason.DROWNING;
                        if (player.getLastHitBy() != null && System.currentTimeMillis() - player.getLastHitAt() < 60000) {
                            killer = player.getLastHitBy();
                        }
                        break;
                    }
                    default: {
                        killReason = KillMessage.KillReason.UNKNOWN;
                    }
                }
            }
            if (killer != null) {
                if (killer.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE)) {
                    killMessage = (KillMessage) killer.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE);
                } else {
                    killMessage = (KillMessage) AuroraMCAPI.getCosmetics().get(500);
                }
                killer.getStats().incrementStatistic(5, "kills", 1, true);
                killer.getStats().incrementStatistic(5, "kills;" + killReason.name(), 1, true);
            }

            player.setLastHitAt(-1);
            player.setLastHitBy(null);

            player.getStats().incrementStatistic(5, "deaths;" + killReason.name(), 1, true);
            player.getStats().incrementStatistic(5, "deaths", 1, true);

            String ent = ((entity == null)?null: WordUtils.capitalizeFully(WordUtils.capitalizeFully(entity.getType().name().replace("_", " "))));

            for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                player2.sendMessage(TextFormatter.pluginMessage("Kill", killMessage.onKill(player2, killer, player, ent, killReason, 5)));
            }
            List<String> destinations = new ArrayList<>();
            String sender = "SMP-Overworld";
            switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                case "OVERWORLD": {
                    destinations.add("SMP-Nether");
                    destinations.add("SMP-End");
                    break;
                }
                case "END": {
                    destinations.add("SMP-Nether");
                    destinations.add("SMP-Overworld");
                    sender = "SMP-End";
                    break;
                }
                case "NETHER": {
                    destinations.add("SMP-Overworld");
                    destinations.add("SMP-End");
                    sender = "SMP-Nether";
                    break;
                }
            }
            for (String destination : destinations) {
                ProtocolMessage message = new ProtocolMessage(Protocol.MESSAGE, destination, "kill", sender, killMessage.onKill(null, killer, player, ent, killReason, 5));
                CommunicationUtils.sendMessage(message);
            }
        } else if (e instanceof PlayerDamageByPlayerEvent) {
            if (e.getDamage() > 0) {
                AuroraMCServerPlayer player1 = ((PlayerDamageByPlayerEvent) e).getDamager();
                if (!player1.isVanished()) {
                    long time = System.currentTimeMillis();
                    player.setLastHitBy(player1);
                    player.setLastHitAt(time);
                    player1.getStats().incrementStatistic(5, "damageDealt", Math.round(e.getDamage() * 100), true);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (!((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type").equals("OVERWORLD")) {
            player.saveData();
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SMPOverworld");
            out.writeUTF(player.getUniqueId().toString());
            player.sendPluginMessage(out.toByteArray());
        } else {
            player.setDead(false);
            if (player.getBedSpawnLocation() != null && player.getBedSpawnLocation().getWorld().getName().equals("smp")) {
                e.setRespawnLocation(player.getBedSpawnLocation());
            } else {
                e.setRespawnLocation(new Location(Bukkit.getWorld("smp"), 0.5, 63, 0.5));
            }
        }
    }

}
