/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api;

import net.auroramc.api.AuroraMCAPI;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;

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
    public Object scheduleAsyncTaskLater(Runnable runnable, long delay) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(ServerAPI.getCore(), delay);
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
    public void broadcastNovaMessage(BaseComponent message) {
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            if (player1.getRank().hasPermission("moderation")) {
                player1.sendMessage(message);
            }
        }
    }

    @Override
    public void firePreferenceEvent(AuroraMCPlayer player) {
        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent((AuroraMCServerPlayer) player);
        Bukkit.getPluginManager().callEvent(e);
    }

    @Override
    public JSONArray getPluginData() {
        JSONArray data = new JSONArray();

        try {

            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                Enumeration<URL> resources = plugin.getClass().getClassLoader()
                        .getResources("META-INF/MANIFEST.MF");
                while (resources.hasMoreElements()) {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    // check that this is your manifest and do what you need or get the next one
                    Attributes attributes = manifest.getMainAttributes();

                    String buildNumber = attributes.getValue("Jenkins-Build-Number");
                    String gitCommit = attributes.getValue("Git-Commit");
                    String branch = attributes.getValue("Branch");
                    if (buildNumber != null && gitCommit != null && branch != null) {
                        JSONObject object = new JSONObject();
                        object.put("name", attributes.getValue("Module-Name"));
                        object.put("build", buildNumber);
                        object.put("commit", gitCommit);
                        object.put("branch", (branch.equals("null")?"master":branch));
                        data.put(object);
                    }
                }
            }
        } catch (IOException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }

        return data;
    }
}
