/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.managers;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.block.*;
import net.auroramc.core.api.events.enchantment.EnchantItemEvent;
import net.auroramc.core.api.events.enchantment.PrepareItemEnchantEvent;
import net.auroramc.core.api.events.entity.*;
import net.auroramc.core.api.events.inventory.*;
import net.auroramc.core.api.events.player.*;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.DragType;
import org.bukkit.scheduler.BukkitRunnable;

public class EventManager implements Listener {

    @EventHandler
    public void blockBreakEvent(org.bukkit.event.block.BlockBreakEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player!=null && player.isLoaded()) {
            BlockBreakEvent event = new BlockBreakEvent(player, e.getBlock());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void blockDamageEvent(org.bukkit.event.block.BlockDamageEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player!=null && player.isLoaded()) {
            BlockDamageEvent event = new BlockDamageEvent(player, e.getBlock(), e.getItemInHand(), e.getInstaBreak());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setInstaBreak(event.isInstaBreak());
        }
    }

    @EventHandler
    public void blockIgniteEvent(org.bukkit.event.block.BlockIgniteEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player!=null && player.isLoaded()) {
            BlockIgniteEvent event = new BlockIgniteEvent(player, e.getBlock(), BlockIgniteEvent.IgniteCause.valueOf(e.getCause().name()));
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void blockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player!=null && player.isLoaded()) {
            BlockPlaceEvent event = new BlockPlaceEvent(player, e.getBlock());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void signChangeEvent(org.bukkit.event.block.SignChangeEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player!=null && player.isLoaded()) {
            SignChangeEvent event = new SignChangeEvent(player, e.getBlock(), e.getLines());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            for (int i = 0;i<4;i++) {
                e.setLine(i, event.getLine(i));
            }
        }
    }

    @EventHandler
    public void enchantItemEvent(org.bukkit.event.enchantment.EnchantItemEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getEnchanter());
        if (player!=null && player.isLoaded()) {
            EnchantItemEvent event = new EnchantItemEvent(player, e.getView(), e.getEnchantBlock(), e.getItem(), e.getExpLevelCost(), e.getEnchantsToAdd(), e.whichButton());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setExpLevelCost(event.getLevel());
            e.getEnchantsToAdd().clear();
            e.getEnchantsToAdd().putAll(event.getEnchants());
        }
    }

    @EventHandler
    public void prepareItemEnchantEvent(org.bukkit.event.enchantment.PrepareItemEnchantEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getEnchanter());
        if (player!=null && player.isLoaded()) {
            PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, e.getView(), e.getEnchantBlock(), e.getItem(), e.getExpLevelCostsOffered(), e.getEnchantmentBonus());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) e;
            if (entityEvent.getEntity() instanceof Player) {
                if (entityEvent.getDamager() instanceof Player) {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) entityEvent.getEntity());
                    if (player != null && player.isLoaded()) {
                        AuroraMCServerPlayer damager = ServerAPI.getPlayer((Player) entityEvent.getDamager());
                        PlayerDamageEvent event = new PlayerDamageByPlayerEvent(player, damager, PlayerDamageEvent.DamageCause.valueOf(e.getCause().name()), e.getFinalDamage());
                        Bukkit.getPluginManager().callEvent(event);
                        AuroraMCAPI.getLogger().info("called1 " + event.isCancelled());
                        e.setDamage(event.getDamage());
                        e.setCancelled(event.isCancelled());
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) entityEvent.getEntity());
                    if (player != null && player.isLoaded()) {
                        PlayerDamageEvent event = new PlayerDamageByEntityEvent(player, entityEvent.getDamager(), PlayerDamageEvent.DamageCause.valueOf(e.getCause().name()), e.getFinalDamage());
                        Bukkit.getPluginManager().callEvent(event);
                        AuroraMCAPI.getLogger().info("called2");
                        e.setDamage(event.getDamage());
                        e.setCancelled(event.isCancelled());
                    } else {
                        e.setCancelled(true);
                    }
                }
            } else if (entityEvent.getDamager() instanceof Player) {
                AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) entityEvent.getDamager());
                if (player != null && player.isLoaded()) {
                    PlayerDamageEvent event = new EntityDamageByPlayerEvent(player, e.getEntity(), PlayerDamageEvent.DamageCause.valueOf(e.getCause().name()), e.getFinalDamage());
                    Bukkit.getPluginManager().callEvent(event);
                    AuroraMCAPI.getLogger().info("called3");
                    e.setDamage(event.getDamage());
                    e.setCancelled(event.isCancelled());
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                PlayerDamageEvent event = new PlayerDamageEvent(player, PlayerDamageEvent.DamageCause.valueOf(e.getCause().name()), e.getFinalDamage());
                Bukkit.getPluginManager().callEvent(event);
                AuroraMCAPI.getLogger().info("called4");
                e.setDamage(event.getDamage());
                e.setCancelled(event.isCancelled());
            }
        }
    }

    @EventHandler
    public void foodLevelChangeEvent(org.bukkit.event.entity.FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                FoodLevelChangeEvent event = new FoodLevelChangeEvent(player, e.getFoodLevel());
                Bukkit.getPluginManager().callEvent(event);
                e.setCancelled(event.isCancelled());
                e.setFoodLevel(event.getLevel());
            }
        }
    }

    @EventHandler
    public void entityCombustEvent(org.bukkit.event.entity.EntityCombustEvent e) {
        if (e instanceof EntityCombustByEntityEvent) {
            EntityCombustByEntityEvent entityEvent = (EntityCombustByEntityEvent) e;
            if (e.getEntity() instanceof Player) {
                if (entityEvent.getCombuster() instanceof Player) {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
                    if (player != null && player.isLoaded()) {
                        AuroraMCServerPlayer combust = ServerAPI.getPlayer((Player) entityEvent.getCombuster());
                        if (combust != null && player.isLoaded()) {
                            PlayerCombustByPlayerEvent event = new PlayerCombustByPlayerEvent(player, e.getDuration(), combust);
                            Bukkit.getPluginManager().callEvent(event);
                            e.setCancelled(event.isCancelled());
                            e.setDuration(event.getDuration());
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
                    if (player != null && player.isLoaded()) {
                        PlayerCombustByEntityEvent event = new PlayerCombustByEntityEvent(player, e.getDuration(), ((EntityCombustByEntityEvent) e).getCombuster());
                        Bukkit.getPluginManager().callEvent(event);
                        e.setCancelled(event.isCancelled());
                        e.setDuration(event.getDuration());
                    } else {
                        e.setCancelled(true);
                    }
                }
            } else {
                if (entityEvent.getCombuster() instanceof Player) {
                    if (e.getEntity() instanceof Player) {
                        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
                        if (player != null && player.isLoaded()) {
                            EntityCombustByPlayerEvent event = new EntityCombustByPlayerEvent(player, e.getDuration(), ((EntityCombustByEntityEvent) e).getCombuster());
                            Bukkit.getPluginManager().callEvent(event);
                            e.setCancelled(event.isCancelled());
                            e.setDuration(event.getDuration());
                        } else {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        } else if (e instanceof EntityCombustByBlockEvent) {
            if (e.getEntity() instanceof Player) {
                AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
                if (player != null && player.isLoaded()) {
                    PlayerCombustByBlockEvent event = new PlayerCombustByBlockEvent(player, e.getDuration(), ((EntityCombustByBlockEvent) e).getCombuster());
                    Bukkit.getPluginManager().callEvent(event);
                    e.setCancelled(event.isCancelled());
                    e.setDuration(event.getDuration());
                } else {
                    e.setCancelled(true);
                }
            }
        } else {
            if (e.getEntity() instanceof Player) {
                AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
                if (player != null && player.isLoaded()) {
                    PlayerCombustEvent event = new PlayerCombustEvent(player, e.getDuration());
                    Bukkit.getPluginManager().callEvent(event);
                    e.setCancelled(event.isCancelled());
                    e.setDuration(event.getDuration());
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerCreatePortalEvent(org.bukkit.event.entity.EntityCreatePortalEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                PlayerCreatePortalEvent event = new PlayerCreatePortalEvent(player, e.getBlocks(), e.getPortalType());
                Bukkit.getPluginManager().callEvent(event);
                e.setCancelled(event.isCancelled());
            }
        }
    }

    @EventHandler
    public void playerLeashEntityEvent(org.bukkit.event.entity.PlayerLeashEntityEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(player, e.getEntity(), e.getLeashHolder());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void craftItemEvent(org.bukkit.event.inventory.CraftItemEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getWhoClicked());
        if (player != null && player.isLoaded()) {
            CraftItemEvent event = new CraftItemEvent(player, e.getRecipe(), e.getView(), e.getSlotType(), e.getSlot(), e.getClick(), e.getAction(), e.getHotbarButton());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setCurrentItem(event.getCurrentItem());
        }
    }

    @EventHandler
    public void furnaceExtractEvent(org.bukkit.event.inventory.FurnaceExtractEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            FurnaceExtractEvent event = new FurnaceExtractEvent(player, e.getBlock(), e.getItemType(), e.getItemAmount(), e.getExpToDrop());
            Bukkit.getPluginManager().callEvent(event);
            e.setExpToDrop(event.getExpToDrop());
        }
    }

    @EventHandler
    public void inventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getWhoClicked());
        if (player != null && player.isLoaded()) {
            InventoryClickEvent event = new InventoryClickEvent(player, e.getView(), e.getSlotType(), e.getRawSlot(), e.getClick(), e.getAction(), e.getHotbarButton());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            if (event.isCancelled()) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.updateInventory();
                    }
                }.runTaskLater(ServerAPI.getCore(), 1);
            }
        }
    }

    @EventHandler
    public void inventoryCloseEvent(org.bukkit.event.inventory.InventoryCloseEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getPlayer());
        if (player != null && player.isLoaded()) {
            InventoryCloseEvent event = new InventoryCloseEvent(player, e.getView());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void inventoryDragEvent(org.bukkit.event.inventory.InventoryDragEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getWhoClicked());
        if (player != null && player.isLoaded()) {
            InventoryDragEvent event = new InventoryDragEvent(player, e.getView(), e.getCursor(), e.getOldCursor(), e.getType() == DragType.SINGLE, e.getNewItems());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void inventoryOpenEvent(org.bukkit.event.inventory.InventoryOpenEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getPlayer());
        if (player != null && player.isLoaded()) {
            InventoryOpenEvent event = new InventoryOpenEvent(player, e.getView());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void prepareCraftItemEvent(org.bukkit.event.inventory.PrepareItemCraftEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getViewers().get(0));
        if (player != null && player.isLoaded()) {
            PrepareItemCraftEvent event = new PrepareItemCraftEvent(player, e.getInventory(), e.getView(), e.isRepair());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void asyncPlayerChatEvent(org.bukkit.event.player.AsyncPlayerChatEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, e.getMessage());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setMessage(event.getMessage());
        }
    }

    @EventHandler
    public void playerArmorStandManipulateEvent(org.bukkit.event.player.PlayerArmorStandManipulateEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerArmorStandManipulateEvent event = new PlayerArmorStandManipulateEvent(player, e.getRightClicked());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void playerBedEnterEvent(org.bukkit.event.player.PlayerBedEnterEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, e.getBed());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerBedLeaveEvent(org.bukkit.event.player.PlayerBedLeaveEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, e.getBed());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void playerBucketEmptyEvent(org.bukkit.event.player.PlayerBucketEmptyEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerBucketEmptyEvent event = new PlayerBucketEmptyEvent(player, e.getBlockClicked(), e.getBlockFace(), e.getBucket(), e.getItemStack());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void playerBucketFillEvent(org.bukkit.event.player.PlayerBucketFillEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerBucketEmptyEvent event = new PlayerBucketEmptyEvent(player, e.getBlockClicked(), e.getBlockFace(), e.getBucket(), e.getItemStack());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void playerDropItemEvent(org.bukkit.event.player.PlayerDropItemEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerDropItemEvent event = new PlayerDropItemEvent(player, e.getItemDrop());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerEditBookEvent(org.bukkit.event.player.PlayerEditBookEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerEditBookEvent event = new PlayerEditBookEvent(player, e.getSlot(), e.getPreviousBookMeta(), e.getNewBookMeta(), e.isSigning());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setSigning(event.isSigning());
            e.setNewBookMeta(event.getNewBookMeta());
        }
    }

    @EventHandler
    public void playerEggThrowEvent(org.bukkit.event.player.PlayerEggThrowEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerEggThrowEvent event = new PlayerEggThrowEvent(player, e.getEgg(), e.isHatching(), e.getNumHatches(), e.getHatchingType());
            Bukkit.getPluginManager().callEvent(event);
            e.setHatching(event.isHatching());
            e.setHatchingType(event.getHatchingType());
            e.setNumHatches(event.getNumHatches());
        }
    }

    @EventHandler
    public void playerExpChangeEvent(org.bukkit.event.player.PlayerExpChangeEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, e.getAmount());
            Bukkit.getPluginManager().callEvent(event);
            e.setAmount(event.getAmount());
        }
    }

    @EventHandler
    public void playerFishEvent(org.bukkit.event.player.PlayerFishEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerFishEvent event = new PlayerFishEvent(player, e.getCaught(), e.getHook(), PlayerFishEvent.State.valueOf(e.getState().name()));
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setExpToDrop(event.getExpToDrop());
        }
    }

    @EventHandler
    public void playerGameModeChangeEvent(org.bukkit.event.player.PlayerGameModeChangeEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(player, e.getNewGameMode());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerInteractAtEntityEvent(org.bukkit.event.player.PlayerInteractAtEntityEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerInteractAtEntityEvent event = new PlayerInteractAtEntityEvent(player, e.getRightClicked(), e.getClickedPosition());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerInteractAtEntityEvent(org.bukkit.event.player.PlayerInteractEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerInteractEvent event = new PlayerInteractEvent(player, e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerInteractEntityEvent(org.bukkit.event.player.PlayerInteractEntityEvent e) {
        if (!(e instanceof org.bukkit.event.player.PlayerInteractAtEntityEvent)) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
            if (player != null && player.isLoaded()) {

                PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, e.getRightClicked());
                Bukkit.getPluginManager().callEvent(event);
                e.setCancelled(event.isCancelled());
            }
        }
    }

    @EventHandler
    public void playerItemBreakEvent(org.bukkit.event.player.PlayerItemBreakEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerItemBreakEvent event = new PlayerItemBreakEvent(player, e.getBrokenItem());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void playerItemConsumeEvent(org.bukkit.event.player.PlayerItemConsumeEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerItemConsumeEvent event = new PlayerItemConsumeEvent(player, e.getItem());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setItem(event.getItem());
        }
    }

    @EventHandler
    public void playerItemDamageEvent(org.bukkit.event.player.PlayerItemDamageEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerItemDamageEvent event = new PlayerItemDamageEvent(player, e.getItem(), e.getDamage());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setDamage(event.getDamage());
        }
    }

    @EventHandler
    public void playerItemHeldEvent(org.bukkit.event.player.PlayerItemHeldEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(player, e.getPreviousSlot(), e.getNewSlot());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerMoveEvent(org.bukkit.event.player.PlayerMoveEvent e) {
        if (!(e instanceof org.bukkit.event.player.PlayerTeleportEvent)) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
            if (player != null && player.isLoaded()) {
                PlayerMoveEvent event = new PlayerMoveEvent(player, e.getFrom(), e.getTo());
                Bukkit.getPluginManager().callEvent(event);
                e.setCancelled(event.isCancelled());
                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
            }
        }
    }

    @EventHandler
    public void playerLevelChangeEvent(org.bukkit.event.player.PlayerLevelChangeEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, e.getOldLevel(), e.getNewLevel());
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void playerPortalEnterEvent(org.bukkit.event.entity.EntityPortalEnterEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                PlayerPortalEnterEvent event = new PlayerPortalEnterEvent(player, e.getLocation());
                Bukkit.getPluginManager().callEvent(event);
            }
        }

    }

    @EventHandler
    public void playerPortalExitEvent(org.bukkit.event.entity.EntityPortalExitEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                PlayerPortalExitEvent event = new PlayerPortalExitEvent(player, e.getFrom(), e.getTo(), e.getBefore(), e.getAfter(), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    @EventHandler
    public void playerTeleportEvent(org.bukkit.event.player.PlayerPortalEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerPortalEvent event = new PlayerPortalEvent(player, e.getFrom(), e.getTo(), e.getPortalTravelAgent(), PlayerTeleportEvent.TeleportCause.valueOf(e.getCause().name()));
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setFrom(event.getFrom());
            e.setTo(event.getTo());
            e.setPortalTravelAgent(event.getTravelAgent());
            e.useTravelAgent(event.useTravelAgent());
        }
    }

    @EventHandler
    public void playerRegainHealthEvent(org.bukkit.event.entity.EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                PlayerRegainHealthEvent event = new PlayerRegainHealthEvent(player, e.getAmount(), PlayerRegainHealthEvent.RegainReason.valueOf(e.getRegainReason().name()));
                Bukkit.getPluginManager().callEvent(event);
                e.setCancelled(event.isCancelled());
                e.setAmount(event.getAmount());
            }
        }
    }

    @EventHandler
    public void playerResourcePackStatusEvent(org.bukkit.event.player.PlayerResourcePackStatusEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerResourcePackStatusEvent event = new PlayerResourcePackStatusEvent(player, PlayerResourcePackStatusEvent.Status.valueOf(e.getStatus().name()));
            Bukkit.getPluginManager().callEvent(event);
        }
    }


    @EventHandler
    public void playerPickupItemEvent(org.bukkit.event.player.PlayerPickupItemEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerPickupItemEvent event = new PlayerPickupItemEvent(player, e.getItem(), e.getRemaining());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerShearEntityEvent(org.bukkit.event.player.PlayerShearEntityEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerShearEntityEvent event = new PlayerShearEntityEvent(player, e.getEntity());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerShearEntityEvent(org.bukkit.event.entity.EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            AuroraMCServerPlayer player = ServerAPI.getPlayer((Player) e.getEntity());
            if (player != null && player.isLoaded()) {
                PlayerShootBowEvent event = new PlayerShootBowEvent(player, e.getBow(), (Projectile) e.getProjectile(), e.getForce());
                Bukkit.getPluginManager().callEvent(event);
                e.setCancelled(event.isCancelled());
            }
        }
    }

    @EventHandler
    public void playerTeleportEvent(org.bukkit.event.player.PlayerTeleportEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerTeleportEvent event = new PlayerTeleportEvent(player, e.getFrom(), e.getTo(), PlayerTeleportEvent.TeleportCause.valueOf(e.getCause().name()));
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setFrom(event.getFrom());
            e.setTo(event.getTo());
        }
    }

    @EventHandler
    public void playerToggleFlightEvent(org.bukkit.event.player.PlayerToggleFlightEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(player, e.isFlying());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerToggleSneakEvent(org.bukkit.event.player.PlayerToggleSneakEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(player, e.isSneaking());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerToggleSprintEvent(org.bukkit.event.player.PlayerToggleSprintEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerToggleSprintEvent event = new PlayerToggleSprintEvent(player, e.isSprinting());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerUnLeashEntityEvent(org.bukkit.event.player.PlayerUnleashEntityEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(player, e.getEntity());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
        }
    }

    @EventHandler
    public void playerVelocityEvent(org.bukkit.event.player.PlayerVelocityEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        if (player != null && player.isLoaded()) {
            PlayerVelocityEvent event = new PlayerVelocityEvent(player, e.getVelocity());
            Bukkit.getPluginManager().callEvent(event);
            e.setCancelled(event.isCancelled());
            e.setVelocity(event.getVelocity());
        }
    }
}
