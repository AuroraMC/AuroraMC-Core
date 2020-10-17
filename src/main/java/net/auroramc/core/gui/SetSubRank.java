package net.auroramc.core.gui;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.permissions.SubRank;
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

import java.util.ArrayList;
import java.util.Collections;
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
        List<Integer> ids = new ArrayList<>(AuroraMCAPI.getSubRanks().keySet());
        Collections.sort(ids);

        for (int i : ids) {
            SubRank rank = AuroraMCAPI.getSubRanks().get(i);
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
        SubRank rank = AuroraMCAPI.getSubRanks().get(rankId);

        if (currentSubranks.contains(rank)) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("You have revoked the **%s** SubRank from **%s**.", rank.getName(), name)));

            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                if (player.isOnline()) {
                    player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Permissions", String.format("The SubRank **%s** has been revoked from you.", rank.getName())));
                    AuroraMCAPI.getPlayer(player).revokeSubrank(rank);
                }
            }

            currentSubranks.remove(rank);
            item.removeEnchantment(Enchantment.DURABILITY);


            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().revokeSubrank(id, rank);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("You have given the **%s** SubRank to **%s**.", rank.getName(), name)));

            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                if (player.isOnline()) {
                    player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Permissions", String.format("The SubRank **%s** has been given to you.", rank.getName())));
                    AuroraMCAPI.getPlayer(player).grantSubrank(rank);
                }
            }

            currentSubranks.add(rank);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            item.setItemMeta(meta);
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().giveSubrank(id, rank);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
    }
}
