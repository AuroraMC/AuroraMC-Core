/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class Cosmetics extends GUI {

    private AuroraMCServerPlayer player;

    public Cosmetics(AuroraMCServerPlayer player) {
        super("&3&lCosmetics", 5, true);

        this.player = player;

        //Fill in GUI.
        fill("&r&f&lCosmetics", "");

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s's Cosmetics", player.getName()), 1, "", (short) 0, false, player.getName()));

        Map<Integer, Cosmetic> cosmetics = AuroraMCAPI.getCosmetics();

        GUIItem swordGUI = new GUIItem(Material.GOLDEN_SWORD, "&6&lKill Messages", 1, String.format(";&r&fYou have unlocked **%s** &fout of **%s** &fkill messages.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.KILL_MESSAGE).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.KILL_MESSAGE).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.KILL_MESSAGE) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.KILL_MESSAGE) != null);
        ItemStack item = swordGUI.getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);

        this.setItem(1, 1, new GUIItem(item));

        this.setItem(2, 1, new GUIItem(Material.FIREWORK_ROCKET, "&5&lWin Effect", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f win effects.;;&r&7These are animations that will;&r&7play when you win any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.WIN_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.WIN_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.WIN_EFFECT) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.WIN_EFFECT) != null));
        this.setItem(3, 1, new GUIItem(Material.SKELETON_SKULL, "&7&lDeath Effect", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f death effects.;;&r&7These are animations that will;&r&7play when you die in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.DEATH_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.DEATH_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.DEATH_EFFECT) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.DEATH_EFFECT) != null));
        this.setItem(4, 1, new GUIItem(Material.ARROW, "&2&lProjectile Trails", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f projectile trails.;;&r&7These are effects that will follow;&r&7any projectile you use in games.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PROJECTILE_TRAIL).filter(cosmetic -> cosmetic.hasUnlocked(player)).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PROJECTILE_TRAIL).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PROJECTILE_TRAIL) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.PROJECTILE_TRAIL) != null));
        this.setItem(4, 7, new GUIItem(Material.NOTE_BLOCK, "&5&lGadgets", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f gadgets.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.GADGET).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.GADGET).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.GADGET) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.GADGET) != null));
        this.setItem(3, 4, new GUIItem(Material.NAME_TAG, "&9&lServer Messages", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f server messages.;;&r&7These are messages that appear in;&r&7chat when you join or leave a game server.;;&bPurchase a rank at store.auroramc.net to;&buse a custom join message.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.SERVER_MESSAGE).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.SERVER_MESSAGE).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.SERVER_MESSAGE) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.SERVER_MESSAGE) != null));
        this.setItem(2, 4, new GUIItem(Material.NETHER_STAR, "&3&lPlus Symbols", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f Plus symbols.;;&r&7These are icons that appear after your;&r&7name in tab.;;&bYou must have an active Plus Subscription to;&bactivate a Plus Symbol.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PLUS_SYMBOL).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PLUS_SYMBOL).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null));
        this.setItem(1, 7, new GUIItem(Material.PLAYER_HEAD, "&f&lHats", 1, String.format(";&r&fYou have unlocked **%s**&f out of **%s**&f hats.;;&r&7These are special items that appear;&r&7on your head", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.HAT).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.HAT).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short) 0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.HAT) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.HAT) != null, "AuroraMC"));

        GUIItem guiItem = new GUIItem(Material.WHITE_BANNER, "&b&lBanners", 1, String.format(";&r&fYou have unlocked **%s** &fout of **%s** &fbanners.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.BANNER).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.BANNER).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.BANNER) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.BANNER) != null);
        ItemStack itemStack = guiItem.getItemStack();
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.setBaseColor(DyeColor.WHITE);
        meta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_RIGHT));
        meta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_LEFT));
        meta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP));
        meta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_MIDDLE));
        meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(meta);

        this.setItem(2, 7, new GUIItem(itemStack));
        this.setItem(3, 7, new GUIItem(Material.REDSTONE, "&4&lParticle Effects", 1, String.format(";&r&fYou have unlocked **%s** &fout of **%s** &fparticle effects.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PARTICLE).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PARTICLE).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short)0, player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PARTICLE) && player.getActiveCosmetics().get(Cosmetic.CosmeticType.PARTICLE) != null));
        this.setItem(5, 4, new GUIItem(Material.SUNFLOWER, "&3&lCurrency Balance", 1, String.format(";&r&fCurrent Ticket Balance: **%s**;&r&fCurrent Crown Balance: **%s**", String.format("%,d", player.getBank().getTickets()), String.format("%,d", player.getBank().getCrowns()))));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }

        Cosmetic.CosmeticType type = null;

        switch (column) {
            case 1:
                switch (row) {
                    case 1:
                        type = Cosmetic.CosmeticType.KILL_MESSAGE;
                        break;
                    case 2:
                        type = Cosmetic.CosmeticType.WIN_EFFECT;
                        break;
                    case 3:
                        type = Cosmetic.CosmeticType.DEATH_EFFECT;
                        break;
                    case 4:
                        type = Cosmetic.CosmeticType.PROJECTILE_TRAIL;
                        break;
                    default:
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                        return;
                }
                break;
            case 4:
                if (row == 3) {
                    type = Cosmetic.CosmeticType.SERVER_MESSAGE;
                } else if (row == 2) {
                    type = Cosmetic.CosmeticType.PLUS_SYMBOL;
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                    return;
                }
                break;
            case 7:
                switch (row) {
                    case 1:
                        type = Cosmetic.CosmeticType.HAT;
                        break;
                    case 2:
                        type = Cosmetic.CosmeticType.BANNER;
                        break;
                    case 3:
                        type = Cosmetic.CosmeticType.PARTICLE;
                        break;
                    case 4: {
                        type = Cosmetic.CosmeticType.GADGET;
                        break;
                    }
                    default:
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                        return;
                }
                break;
            default:
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                return;
        }

        CosmeticsListing listing = new CosmeticsListing(player, type, item);
        listing.open(player);
    }
}
