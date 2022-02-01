/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.gui;

import net.auroramc.core.api.AuroraMCAPI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GUIItem {

    private final ItemStack item;

    public GUIItem(ItemStack item) {
        this.item = item;
    }

    public GUIItem(Material material) {
        this(material, null);
    }

    public GUIItem(Material material, String name) {
        this(material, name, 1);
    }

    public GUIItem(Material material, String name, int amount) {
        this(material, name, amount, null);
    }

    public GUIItem(Material material, String name, int amount, String lore) {
        this(material, name, amount, lore, (short) 0);
    }

    public GUIItem(Material material, String name, int amount, String lore, short data) {
        this(material, name, amount, lore, data, false);
    }

    public GUIItem(Material material, String name, int amount, String lore, short data, boolean glowing) {
        this(material, name, amount, lore, data, glowing, "");
    }

    public GUIItem(Material material, String name, int amount, String lore, short data, boolean glowing, String skullOwner) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta im = item.getItemMeta();
        im.spigot().setUnbreakable(true);
        im.setDisplayName(AuroraMCAPI.getFormatter().convert(name));
        if (lore != null) {
            im.setLore(Arrays.asList(AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().highlight(lore)).split(";")));
        }
        if (glowing) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (skullOwner != null && !skullOwner.equals("")) {
            SkullMeta sm = (SkullMeta) im;
            sm.setOwner(skullOwner);
            im = sm;
        }
        item.setItemMeta(im);

        this.item = item;
    }

    public GUIItem(Material material, String name, int amount, String lore, short data, boolean glowing, Color color) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(AuroraMCAPI.getFormatter().convert(name));
        if (lore != null) {
            im.setLore(Arrays.asList(AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().highlight(lore)).split(";")));
        }
        if (glowing) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (color != null) {
            LeatherArmorMeta am = (LeatherArmorMeta) im;
            am.setColor(color);
            im = am;
        }
        item.setItemMeta(im);

        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
