/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.DiscordWebhook;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class SetSubRank extends GUI {

    AuroraMCPlayer player;
    String name;
    UUID uuid;
    int id;
    List<SubRank> currentSubranks;


    public SetSubRank(AuroraMCPlayer player, String name, UUID uuid, int id, List<SubRank> currentSubranks) {
        super(String.format("&3&lSet %s's SubRank", name), 1, true);

        this.player = player;
        this.name = name;
        this.uuid = uuid;
        this.id = id;
        this.currentSubranks = currentSubranks;

        int row = 0;
        int column = 0;

        //Generate GUI.
        for (SubRank rank : SubRank.values()) {
            this.setItem(row, column, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getMenuColor(), rank.getName()), 1, String.format("&r&7Toggle %s's %s SubRank;;&r&7ID: &a%s", name, rank.getName(), rank.getId()), (short) 0, currentSubranks.contains(rank), rank.getColor()));
            column++;
            if (column > 8) {
                row++;
                column = 0;
            }
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        List<String> lore = item.getItemMeta().getLore();
        String sid = lore.get(lore.size() - 1);
        sid = ChatColor.stripColor(sid.split(" ")[1]);
        int rankId = Integer.parseInt(sid);
        SubRank rank = SubRank.getByID(rankId);

        if (currentSubranks.contains(rank)) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("You have revoked the **%s** SubRank from **%s**.", rank.getName(), name)));

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("RemoveSubRank");
            out.writeUTF(uuid.toString());
            out.writeInt(rankId);
            this.player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

            currentSubranks.remove(rank);
            item.removeEnchantment(Enchantment.DURABILITY);


            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().revokeSubrank(id, rank);
                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929016334912733205/bFwMdYwk1mI2adr1aubBW3aUDEHcWViNUsfOa_5GrD9KVT2ijI3N5NHKesknQuJNW1H1");

                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("SubRank Added").setDescription(String.format("**%s** has removed SubRank **%s** from player **%s**.", player.getName(), rank.name(), name)).setColor(new Color(rank.getColor().asRGB())));
                    try {
                        discordWebhook.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("You have given the **%s** SubRank to **%s**.", rank.getName(), name)));

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("AddSubRank");
            out.writeUTF(uuid.toString());
            out.writeInt(rankId);
            this.player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

            currentSubranks.add(rank);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            item.setItemMeta(meta);
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().giveSubrank(id, rank);
                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929016334912733205/bFwMdYwk1mI2adr1aubBW3aUDEHcWViNUsfOa_5GrD9KVT2ijI3N5NHKesknQuJNW1H1");

                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("SubRank Removed").setDescription(String.format("**%s** has added SubRank **%s** to player **%s**.", player.getName(), rank.name(), name)).setColor(new Color(rank.getColor().asRGB())));
                    try {
                        discordWebhook.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
    }
}
