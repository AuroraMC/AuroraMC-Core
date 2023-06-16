/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.utils.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.auroramc.api.utils.Item;
import net.auroramc.api.utils.TextFormatter;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIItem {

    private final ItemStack item;

    public GUIItem(ItemStack item) {
        this.item = item;
    }

    public GUIItem(Item item, boolean base64) {
        this(Material.matchMaterial(item.getMaterial()), item.getName(), item.getAmount(), item.getLore(), item.getData(), item.isGlowing(), item.getSkullOwner());
    }

    public GUIItem(Item item) {
        this(Material.matchMaterial(item.getMaterial()), item.getName(), item.getAmount(), item.getLore(), item.getData(), item.isGlowing(), Color.fromRGB(item.getR(), item.getG(), item.getB()));
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

    public GUIItem(String name, int amount, String lore, boolean glowing, String baseColor, List<Item.Pattern> patterns) {
        ItemStack item = new ItemStack(Material.valueOf(baseColor + "_BANNER"), amount);
        ItemMeta im = item.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (name != null) {
            im.setDisplayName(TextFormatter.convert(name));
        }
        if (lore != null) {
            im.setLore(Arrays.asList(TextFormatter.convert(TextFormatter.highlightRaw(lore)).split(";")));
        }
        if (glowing) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        BannerMeta banner = (BannerMeta) im;
        banner.setBaseColor(DyeColor.valueOf(baseColor));
        for (Item.Pattern pattern1 : patterns) {
            banner.addPattern(new Pattern(DyeColor.valueOf(pattern1.getDye()), PatternType.valueOf(pattern1.getPatternType())));
        }
        item.setItemMeta(banner);
        this.item = item;
    }

    public GUIItem(String name, int amount, String lore, boolean glowing, String base64) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        ItemMeta im = item.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (name != null) {
            im.setDisplayName(TextFormatter.convert(name));
        }
        if (lore != null) {
            im.setLore(Arrays.asList(TextFormatter.convert(TextFormatter.highlightRaw(lore)).split(";")));
        }
        if (glowing) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }


        //Set profile of skull.
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", base64));
        SkullMeta meta = (SkullMeta) im;
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            this.item = new ItemStack(Material.AIR);
            return;
        }
        field.setAccessible(true);
        try {
            field.set(meta, profile);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }



        item.setItemMeta(im);
        this.item = item;
    }

    public GUIItem(Material material, String name, int amount, String lore, short data, boolean glowing, String skullOwner) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta im = item.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (name != null) {
            im.setDisplayName(TextFormatter.convert(name));
        }
        if (lore != null) {
            im.setLore(Arrays.asList(TextFormatter.convert(TextFormatter.highlightRaw(lore)).split(";")));
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
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (name != null) {
            im.setDisplayName(TextFormatter.convert(name));
        }
        if (lore != null) {
            im.setLore(Arrays.asList(TextFormatter.convert(TextFormatter.highlightRaw(lore)).split(";")));
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

    public ItemStack getItemStack() {
        return item;
    }

    public static GUIItem fromItem(Item item) {
        if (item.getBase64() != null) {
            return new GUIItem(item.getName(), item.getAmount(), item.getLore(), item.isGlowing(), item.getBase64());
        } else if (item.getBaseColour() != null) {
            return new GUIItem(item.getName(), item.getAmount(), item.getLore(), item.isGlowing(), item.getBaseColour(), item.getPatterns());
        } else if (item.getR() != -1) {
            return new GUIItem(item);
        } else {
            return new GUIItem(item, false);
        }
    }
}

