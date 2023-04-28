/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api;

import net.auroramc.api.abstraction.AbstractedMethods;
import net.auroramc.api.cosmetics.*;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Disguise;
import net.auroramc.api.utils.Item;
import net.auroramc.common.cosmetics.crates.DiamondCrate;
import net.auroramc.common.cosmetics.crates.EmeraldCrate;
import net.auroramc.common.cosmetics.crates.GoldCrate;
import net.auroramc.common.cosmetics.crates.IronCrate;
import net.auroramc.core.api.events.player.PlayerPreferenceChangeEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.ServerDisguise;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ServerAbstractedMethods extends AbstractedMethods {


    @Override
    public Disguise newDisguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        return new ServerDisguise((AuroraMCServerPlayer) player, name, skin, rank);
    }

    @Override
    public Disguise newDisguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        return new ServerDisguise((AuroraMCServerPlayer) player, name, skin, signature, rank);
    }

    @Override
    public Disguise newDisguise(String name, String skin, String signature, Rank rank) {
        return new ServerDisguise(name, skin, signature, rank);
    }

    @Override
    public void scheduleAsyncTask(Runnable runnable) {
        new BukkitRunnable(){
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @Override
    public void scheduleSyncTask(Runnable runnable) {
        new BukkitRunnable(){
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(ServerAPI.getCore());
    }

    @Override
    public Crate generateIronCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new IronCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateGoldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new GoldCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateDiamondCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new DiamondCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateEmeraldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new EmeraldCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public void onEquipBanner(AuroraMCPlayer player, Banner banner) {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.setBaseColor(DyeColor.valueOf(banner.getBase()));
        for (Item.Pattern pattern : banner.getPatterns()) {
            meta.addPattern(new Pattern(DyeColor.valueOf(pattern.getDye()), PatternType.valueOf(pattern.getPatternType())));
        }
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        ((AuroraMCServerPlayer)player).getInventory().setHelmet(item);
    }

    @Override
    public void onUnequipBanner(AuroraMCPlayer player, Banner banner) {
        ((AuroraMCServerPlayer)player).getInventory().setHelmet(new ItemStack(Material.AIR));
    }

    @Override
    public void onEquipHat(AuroraMCPlayer player, Hat hat) {
        ((AuroraMCServerPlayer)player).getInventory().setHelmet(GUIItem.fromItem(hat.getItem(player)).getItemStack());
    }

    @Override
    public void onUnequipHat(AuroraMCPlayer player, Hat hat) {
        ((AuroraMCServerPlayer)player).getInventory().setHelmet(new ItemStack(Material.AIR));
    }

    @Override
    public void onEquipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            player1.updateNametag(player);
        }
    }

    @Override
    public void onUnequipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            player1.updateNametag(player);
        }
    }

    @Override
    public void onEquipGadget(AuroraMCPlayer player, Gadget gadget) {
        ((AuroraMCServerPlayer)player).getInventory().setItem(3, GUIItem.fromItem(gadget.getItem(player)).getItemStack());
    }

    @Override
    public void onUnequipGadget(AuroraMCPlayer player, Gadget gadget) {
        ((AuroraMCServerPlayer)player).getInventory().setItem(3, new ItemStack(Material.AIR));
    }

    @Override
    public void broadcastModerationMessage(BaseComponent message, AuroraMCPlayer issuer) {
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            if (!player1.equals(issuer)) {
                if (player1.getRank().hasPermission("moderation")) {
                    player1.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void firePreferenceEvent(AuroraMCPlayer player) {
        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent((AuroraMCServerPlayer) player);
        Bukkit.getPluginManager().callEvent(e);
    }
}
