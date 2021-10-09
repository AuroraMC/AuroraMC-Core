/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Hat extends Cosmetic {

    private ItemStack head;

    public Hat(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, String headUrl, boolean showIfNotUnlocked) {
        super(id, CosmeticType.HAT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, Material.SKULL, (short)3);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", headUrl).getBytes())));

        head = new ItemStack(Material.SKULL_ITEM, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().convert(displayName)));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return;
        }
        field.setAccessible(true);
        try {
            field.set(meta, profile);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
    }

    @Override
    public final void onEquip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(head);
    }

    @Override
    public final void onUnequip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
    }

    @Override
    public ItemStack getItem(AuroraMCPlayer player) {
        ItemStack item = head.clone();
        ItemMeta meta = item.getItemMeta();
        boolean hasUnlocked = hasUnlocked(player);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(Arrays.asList(AuroraMCAPI.getFormatter().convert("&r" + WordUtils.wrap(getDescription(), 40, ";&r", false)).split(";")));
        lore.add("");
        if (hasUnlocked) {
            if (player.getActiveCosmetics().get(getType()).equals(this)) {
                lore.add(AuroraMCAPI.getFormatter().convert("&cClick to disable!"));
            } else {
                lore.add(AuroraMCAPI.getFormatter().convert("&aClick to enable!"));
            }
        } else {
            if (getUnlockMode() == UnlockMode.TICKETS) {
                if (player.getBank().getTickets() >= getCurrency()) {
                    lore.add(AuroraMCAPI.getFormatter().convert(String.format("&eClick to unlock for %s tickets!", getCurrency())));
                } else {
                    lore.add(AuroraMCAPI.getFormatter().convert("&cYou have insufficient funds to purchase"));
                    lore.add(AuroraMCAPI.getFormatter().convert("&cthis cosmetic."));
                }
            } else {
                lore.add(AuroraMCAPI.getFormatter().convert("&9" + getUnlockMessage()));
            }
        }

        meta.setLore(lore);
        if (player.getActiveCosmetics().get(getType()).equals(this)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }
}
