/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Banner extends Cosmetic {

    private final List<Pattern> patterns;
    private final DyeColor base;

    public Banner(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, List<Pattern> patterns, DyeColor base, boolean showIfNotUnlocked, Rarity rarity) {
        super(id, CosmeticType.BANNER, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, Material.BANNER, (short) 1, rarity);
        this.patterns = patterns;
        this.base = base;
    }

    public List<Pattern> getPatterns() {
        return new ArrayList<>(patterns);
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.setBaseColor(base);
        for (Pattern pattern : patterns) {
            meta.addPattern(pattern);
        }
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        player.getPlayer().getInventory().setHelmet(item);
    }

    @Override
    public final void onUnequip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
    }

    @Override
    public ItemStack getItem(AuroraMCPlayer player) {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.setBaseColor(base);
        for (Pattern pattern : patterns) {
            meta.addPattern(pattern);
        }

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        meta.setDisplayName(AuroraMCAPI.getFormatter().convert(getDisplayName()));

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(Arrays.asList(AuroraMCAPI.getFormatter().convert("&r&f" + WordUtils.wrap(getDescription(), 40, ";&r&f", false)).split(";")));
        lore.add(AuroraMCAPI.getFormatter().convert("&r&fRarity: " + this.getRarity().getDisplayName()));
        lore.add("");
        if (hasUnlocked(player)) {
            if (player.getActiveCosmetics().get(getType()) != null) {
                if (player.getActiveCosmetics().get(getType()).equals(this)) {
                    lore.add(AuroraMCAPI.getFormatter().convert("&cClick to disable!"));
                } else {
                    lore.add(AuroraMCAPI.getFormatter().convert("&aClick to enable!"));
                }
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
        if (player.getActiveCosmetics().get(getType()) != null) {
            if (player.getActiveCosmetics().get(getType()).equals(this)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }
        item.setItemMeta(meta);

        return item;
    }
}
