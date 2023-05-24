/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.AuroraMC;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.PlayerInteractEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class LockChestListener implements Listener {

    private static Map<UUID, String> waitingForInput = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.CHEST) {
                    if (e.getPlayer().isSneaking()) {
                        //Protect chest if not already protected.
                        if (waitingForInput.containsKey(e.getPlayer().getUniqueId())) {
                            String command = waitingForInput.remove(e.getPlayer().getUniqueId());
                            if (command.startsWith("add")) {
                                String uuid = command.split(";")[1];
                                if (AuroraMC.getInternal().contains("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ())) {
                                    UUID owner = UUID.fromString(AuroraMC.getInternal().getString("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner"));
                                    if (e.getPlayer().getUniqueId().equals(owner)) {
                                        List<String> strings = new ArrayList<>(AuroraMC.getInternal().getStringList("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members"));
                                        strings.add(uuid);
                                        AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members", strings);
                                        try {
                                            AuroraMC.getInternal().save(AuroraMC.getInternalFile());
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Adding members completed successfully!"));
                                    } else {
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "You cannot modify this chest as this chest does not belong to you."));
                                    }
                                }
                            } else if (command.startsWith("remove")) {
                                String uuid = command.split(";")[1];
                                if (AuroraMC.getInternal().contains("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ())) {
                                    UUID owner = UUID.fromString(AuroraMC.getInternal().getString("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner"));
                                    if (e.getPlayer().getUniqueId().equals(owner)) {
                                        List<String> strings = new ArrayList<>(AuroraMC.getInternal().getStringList("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members"));
                                        if (uuid.startsWith("team")) {
                                            for (String s : new ArrayList<>(strings)) {
                                                if (s.startsWith("team")) {
                                                    strings.remove(s);
                                                }
                                            }
                                        } else {
                                            strings.remove(uuid);
                                        }
                                        AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members", strings);
                                        try {
                                            AuroraMC.getInternal().save(AuroraMC.getInternalFile());
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Removing members completed successfully!"));
                                    } else {
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "You cannot modify this chest as this chest does not belong to you."));
                                    }
                                }
                            } else {
                                UUID owner = UUID.fromString(AuroraMC.getInternal().getString("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner"));
                                List<String> strings = new ArrayList<>(AuroraMC.getInternal().getStringList("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members"));
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        String ownerName = AuroraMCAPI.getDbManager().getNameFromUUID(owner.toString());
                                        List<String> members = new ArrayList<>();
                                        for (String s : strings) {
                                            if (s.startsWith("team")) {
                                                String name = AuroraMCAPI.getDbManager().getSMPTeamName(UUID.fromString(s.split("~")[1]));
                                                members.add("Team: " + name);
                                            } else if (s.startsWith("player")) {
                                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(s.split("~")[1]);
                                                members.add("Player: " + name);
                                            }
                                        }
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Chest Information:\n" +
                                                "**Owner:** " + ownerName + "\n" +
                                                "**Members:**: " + String.join(", ", members)));
                                    }
                                }.runTaskAsynchronously(ServerAPI.getCore());
                            }
                        } else {
                            if (AuroraMC.getInternal().contains("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ())) {
                                UUID uuid = UUID.fromString(AuroraMC.getInternal().getString("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner"));
                                if (e.getPlayer().getUniqueId().equals(uuid)) {
                                    AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ(), null);
                                    try {
                                        AuroraMC.getInternal().save(AuroraMC.getInternalFile());
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "You cannot unlock this chest as this chest does not belong to you."));
                                }
                            } else {
                                AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members", new ArrayList<>());
                            }
                        }
                    } else {
                        //Check if chest is protected.
                        if (AuroraMC.getInternal().contains("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ())) {
                            //It is, does the player have access.
                            UUID uuid = UUID.fromString(AuroraMC.getInternal().getString("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner"));
                            if (!e.getPlayer().getUniqueId().equals(uuid)) {
                                List<String> members = AuroraMC.getInternal().getStringList("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members");
                                for (String s : members) {
                                    String[] ss = s.split("~");
                                    if (ss[0].equals("team")) {
                                        UUID team = UUID.fromString(ss[1]);
                                        if (e.getPlayer().getSmpTeam() != null) {
                                            if (e.getPlayer().getSmpTeam().getUuid().equals(team)) {
                                                return;
                                            }
                                        }
                                    } else if (ss[0].equals("player")) {
                                        UUID target = UUID.fromString(ss[1]);
                                        if (e.getPlayer().getUniqueId().equals(target)) {
                                            return;
                                        }
                                    }
                                }

                                e.setCancelled(true);
                                TextComponent component = new TextComponent("This chest is locked.");
                                component.setColor(ChatColor.DARK_AQUA);
                                component.setBold(true);
                                e.getPlayer().sendHotBar(component);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Map<UUID, String> getWaitingForInput() {
        return waitingForInput;
    }
}
