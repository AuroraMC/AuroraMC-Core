package net.auroramc.core.gui.cosmetics;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
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

import java.util.ArrayList;
import java.util.Map;

public class Cosmetics extends GUI {

    private AuroraMCPlayer player;

    public Cosmetics(AuroraMCPlayer player) {
        super("&3&lCosmetics", 5, true);

        this.player = player;

        //Fill in GUI.
        fill("&3&lCosmetics", "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Cosmetics", player.getPlayer().getName()), 1, "", (short) 3, false, player.getPlayer().getName()));

        Map<Integer, Cosmetic> cosmetics = AuroraMCAPI.getCosmetics();

        GUIItem swordGUI = new GUIItem(Material.GOLD_SWORD, "&6&lKill Messages", 1, String.format(";&rYou have unlocked **%s** out of **%s** kill messages.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.KILL_MESSAGE).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.KILL_MESSAGE).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()));
        ItemStack item = swordGUI.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);

        this.setItem(1, 1, new GUIItem(item));

        this.setItem(2, 1, new GUIItem(Material.FIREWORK, "&5&lWin Effect", 1, String.format(";&rYou have unlocked **%s** out of **%s** win effects.;;&r&7These are animations that will;&r&7play when you win any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.WIN_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.WIN_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));
        this.setItem(3, 1, new GUIItem(Material.SKULL_ITEM, "&7&lDeath Effect", 1, String.format(";&rYou have unlocked **%s** out of **%s** death effects.;;&r&7These are animations that will;&r&7play when you die in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.DEATH_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.DEATH_EFFECT).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));
        this.setItem(4, 1, new GUIItem(Material.ARROW, "&2&lProjectile Trails", 1, String.format(";&rYou have unlocked **%s** out of **%s** projectile trails.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PROJECTILE_TRAIL).filter(cosmetic -> cosmetic.hasUnlocked(player)).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PROJECTILE_TRAIL).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));
        this.setItem(2, 4, new GUIItem(Material.LEASH, "&c&lPets", 1, String.format(";&rYou have unlocked **%s** out of **%s** pets.;;&r&7These are effects that will follow;&r&7any projectile you use in games.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PET).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PET).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));
        this.setItem(3, 4, new GUIItem(Material.NOTE_BLOCK, "&5&lGadgets", 1, String.format(";&rYou have unlocked **%s** out of **%s** gadgets.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.GADGET).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.GADGET).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));
        this.setItem(1, 7, new GUIItem(Material.SKULL_ITEM, "&f&lHats", 1, String.format(";&rYou have unlocked **%s** out of **%s** hats.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.HAT).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.HAT).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()), (short) 3, false, "AuroraMC"));

        GUIItem guiItem = new GUIItem(Material.BANNER, "&b&lBanners", 1, String.format(";&rYou have unlocked **%s** out of **%s** banners.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.BANNER).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.BANNER).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count()));
        ItemStack itemStack = guiItem.getItem();
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
        this.setItem(3, 7, new GUIItem(Material.REDSTONE, "&4&lParticle Effects", 1, String.format(";&rYou have unlocked **%s** out of **%s** particle effects.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PARTICLE).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.PARTICLE).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));
        this.setItem(4, 7, new GUIItem(Material.MONSTER_EGG, "&a&lMorphs", 1, String.format(";&rYou have unlocked **%s** out of **%s** morphs.;;&r&7These are messages that appear in;&r&7chat when you kill a player in any game.", cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.MORPH).filter(cosmetic -> cosmetic.hasUnlocked(player)).count(), cosmetics.values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.MORPH).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).count())));

        this.setItem(5, 4, new GUIItem(Material.DOUBLE_PLANT, "&3&lTicket Balance", 1, String.format(";&rCurrent Ticket Balance: **%s**", player.getBank().getTickets())));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
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
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                        return;
                }
                break;
            case 4:
                switch (row) {
                    case 2:
                        type = Cosmetic.CosmeticType.PET;
                        break;
                    case 3:
                        type = Cosmetic.CosmeticType.GADGET;
                        break;
                    default:
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
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
                    case 4:
                        type = Cosmetic.CosmeticType.MORPH;
                        break;
                    default:
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                        return;
                }
                break;
            default:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                return;
        }

        CosmeticsListing listing = new CosmeticsListing(player, type, item);
        listing.open(player);
        AuroraMCAPI.openGUI(player, listing);
    }
}
