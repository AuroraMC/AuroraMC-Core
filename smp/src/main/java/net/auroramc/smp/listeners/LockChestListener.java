/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.AuroraMC;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.events.block.BlockBreakEvent;
import net.auroramc.smp.api.events.block.BlockDamageEvent;
import net.auroramc.smp.api.events.block.BlockIgniteEvent;
import net.auroramc.smp.api.events.player.PlayerInteractEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class LockChestListener implements Listener {

    private static Map<UUID, String> waitingForInput = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.BLAST_FURNACE || e.getClickedBlock().getType() == Material.SMOKER || e.getClickedBlock().getType().name().contains("ANVIL") || e.getClickedBlock().getType().name().endsWith("DOOR")) {
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
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "Adding members completed successfully!"));
                                    } else {
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "You cannot modify this container as this chest does not belong to you."));
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
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "Removing members completed successfully!"));
                                    } else {
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "You cannot modify this container as this chest does not belong to you."));
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
                                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "Container Information:\n" +
                                                "**Owner:** " + ownerName + "\n" +
                                                "**Members:** " + String.join(", ", members)));
                                    }
                                }.runTaskAsynchronously(ServerAPI.getCore());
                            }
                        } else {
                            if (AuroraMC.getInternal().contains("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ())) {
                                UUID uuid = UUID.fromString(AuroraMC.getInternal().getString("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner"));
                                if (e.getPlayer().getUniqueId().equals(uuid)) {
                                    if (e.getClickedBlock().getState() instanceof Chest chest) {
                                        if (chest.getInventory().getHolder() instanceof DoubleChest doubleChest) {
                                            Chest left = (Chest) doubleChest.getLeftSide();
                                            Chest right = (Chest) doubleChest.getRightSide();

                                            assert left != null;
                                            AuroraMC.getInternal().set("chests." + left.getLocation().getBlockX() + "." + left.getLocation().getBlockY() + "." + left.getLocation().getBlockZ(), null);
                                            assert right != null;
                                            AuroraMC.getInternal().set("chests." + right.getLocation().getBlockX() + "." + right.getLocation().getBlockY() + "." + right.getLocation().getBlockZ(), null);
                                        } else {
                                            AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ(), null);
                                        }
                                    } else if (e.getClickedBlock().getType().name().endsWith("_DOOR")) {
                                        Block block = e.getClickedBlock();
                                        Block above = e.getClickedBlock().getRelative(0, 1, 0);
                                        Block below = e.getClickedBlock().getRelative(0, -1, 0);

                                        if (above.getType() == block.getType()) {
                                            AuroraMC.getInternal().set("chests." + block.getLocation().getBlockX() + "." + block.getLocation().getBlockY() + "." + block.getLocation().getBlockZ(), null);
                                            AuroraMC.getInternal().set("chests." + above.getLocation().getBlockX() + "." + above.getLocation().getBlockY() + "." + above.getLocation().getBlockZ(), null);
                                        } else if (below.getType() == block.getType()) {
                                            AuroraMC.getInternal().set("chests." + block.getLocation().getBlockX() + "." + block.getLocation().getBlockY() + "." + block.getLocation().getBlockZ(), null);
                                            AuroraMC.getInternal().set("chests." + below.getLocation().getBlockX() + "." + below.getLocation().getBlockY() + "." + below.getLocation().getBlockZ(), null);
                                        } else {
                                            //something has gone drastically wrong if this is true.
                                            return;
                                        }
                                    } else {
                                        AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ(), null);
                                    }
                                    try {
                                        AuroraMC.getInternal().save(AuroraMC.getInternalFile());
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "Container unlocked!"));
                                } else {
                                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Containers", "You cannot unlock this container as this container does not belong to you."));
                                }
                            } else {
                                if (e.getClickedBlock().getState() instanceof Chest chest) {
                                    if (chest.getInventory().getHolder() instanceof DoubleChest doubleChest) {
                                        Chest left = (Chest) doubleChest.getLeftSide();
                                        Chest right = (Chest) doubleChest.getRightSide();
                                        assert left != null;
                                        AuroraMC.getInternal().set("chests." + left.getLocation().getBlockX() + "." + left.getLocation().getBlockY() + "." + left.getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + left.getLocation().getBlockX() + "." + left.getLocation().getBlockY() + "." + left.getLocation().getBlockZ() + ".members", new ArrayList<>());

                                        assert right != null;
                                        AuroraMC.getInternal().set("chests." + right.getLocation().getBlockX() + "." + right.getLocation().getBlockY() + "." + right.getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + right.getLocation().getBlockX() + "." + right.getLocation().getBlockY() + "." + right.getLocation().getBlockZ() + ".members", new ArrayList<>());
                                    } else {
                                        AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members", new ArrayList<>());
                                    }
                                } else if (e.getClickedBlock().getType().name().endsWith("_DOOR")) {
                                    Block block = e.getClickedBlock();
                                    Block above = e.getClickedBlock().getRelative(0, 1, 0);
                                    Block below = e.getClickedBlock().getRelative(0, -1, 0);

                                    if (above.getType() == block.getType()) {
                                        AuroraMC.getInternal().set("chests." + block.getLocation().getBlockX() + "." + block.getLocation().getBlockY() + "." + block.getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + block.getLocation().getBlockX() + "." + block.getLocation().getBlockY() + "." + block.getLocation().getBlockZ() + ".members", new ArrayList<>());

                                        AuroraMC.getInternal().set("chests." + above.getLocation().getBlockX() + "." + above.getLocation().getBlockY() + "." + above.getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + above.getLocation().getBlockX() + "." + above.getLocation().getBlockY() + "." + above.getLocation().getBlockZ() + ".members", new ArrayList<>());
                                    } else if (below.getType() == block.getType()) {
                                        AuroraMC.getInternal().set("chests." + block.getLocation().getBlockX() + "." + block.getLocation().getBlockY() + "." + block.getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + block.getLocation().getBlockX() + "." + block.getLocation().getBlockY() + "." + block.getLocation().getBlockZ() + ".members", new ArrayList<>());

                                        AuroraMC.getInternal().set("chests." + below.getLocation().getBlockX() + "." + below.getLocation().getBlockY() + "." + below.getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                        AuroraMC.getInternal().set("chests." + below.getLocation().getBlockX() + "." + below.getLocation().getBlockY() + "." + below.getLocation().getBlockZ() + ".members", new ArrayList<>());
                                    } else {
                                        //something has gone drastically wrong if this is true.
                                        return;
                                    }
                                } else {
                                    AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".owner", e.getPlayer().getUniqueId().toString());
                                    AuroraMC.getInternal().set("chests." + e.getClickedBlock().getLocation().getBlockX() + "." + e.getClickedBlock().getLocation().getBlockY() + "." + e.getClickedBlock().getLocation().getBlockZ() + ".members", new ArrayList<>());
                                }
                                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Container locked!"));
                            }
                        }
                        e.setCancelled(true);
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
                                TextComponent component = new TextComponent("This container is locked.");
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.BLAST_FURNACE || e.getBlock().getType() == Material.SMOKER || e.getBlock().getType().name().contains("ANVIL") || e.getBlock().getType().name().endsWith("DOOR")) {
            if (AuroraMC.getInternal().contains("chests." + e.getBlock().getLocation().getBlockX() + "." + e.getBlock().getLocation().getBlockY() + "." + e.getBlock().getLocation().getBlockZ())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Chests must be unlocked in order to break them."));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockDamageEvent e) {
        if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.BLAST_FURNACE || e.getBlock().getType() == Material.SMOKER || e.getBlock().getType().name().contains("ANVIL") || e.getBlock().getType().name().endsWith("DOOR")) {
            if (AuroraMC.getInternal().contains("chests." + e.getBlock().getLocation().getBlockX() + "." + e.getBlock().getLocation().getBlockY() + "." + e.getBlock().getLocation().getBlockZ())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Chests must be unlocked in order to break them."));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockIgniteEvent e) {
        if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.BLAST_FURNACE || e.getBlock().getType() == Material.SMOKER || e.getBlock().getType().name().contains("ANVIL") || e.getBlock().getType().name().endsWith("DOOR")) {
            if (AuroraMC.getInternal().contains("chests." + e.getBlock().getLocation().getBlockX() + "." + e.getBlock().getLocation().getBlockY() + "." + e.getBlock().getLocation().getBlockZ())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Chests", "Chests must be unlocked in order to break them."));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(InventoryMoveItemEvent e) {
        if (e.getSource().getHolder() instanceof Container container) {
            if (AuroraMC.getInternal().contains("chests." + container.getBlock().getLocation().getBlockX() + "." + container.getBlock().getLocation().getBlockY() + "." + container.getBlock().getLocation().getBlockZ())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockExplodeEvent e) {
        if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.BLAST_FURNACE || e.getBlock().getType() == Material.SMOKER || e.getBlock().getType().name().contains("ANVIL") || e.getBlock().getType().name().endsWith("DOOR")) {
            if (AuroraMC.getInternal().contains("chests." + e.getBlock().getLocation().getBlockX() + "." + e.getBlock().getLocation().getBlockY() + "." + e.getBlock().getLocation().getBlockZ())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockFromToEvent e) {

    }

    public static Map<UUID, String> getWaitingForInput() {
        return waitingForInput;
    }
}
