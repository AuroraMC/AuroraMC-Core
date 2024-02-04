/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.preferences;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class StaffPreferences extends GUI {

    private AuroraMCServerPlayer player;

    public StaffPreferences(AuroraMCServerPlayer player) {
        super("&3&lStaff Preferences", 3, true);
        this.player = player;

        border("&3&lStaff Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 2, new GUIItem(Material.SUGAR_CANE, "&3Hub Invisibility", 1, ";&r&fBecome completely invisible to all other players;&r&fwhilst you are roaming around the lobby."));
        this.setItem(2, 2, new GUIItem(((player.getPreferences().isHubInvisibilityEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Hub Invisibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubInvisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubInvisibilityEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(1, 4, new GUIItem(Material.REDSTONE_TORCH, "&3Social Media Login Notifications", 1, ";&r&fGives you a notification when social;&r&fmedia ranked users join the network."));
        this.setItem(2, 4, new GUIItem(((player.getPreferences().isSocialMediaNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Social Media Login Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isSocialMediaNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isSocialMediaNotificationsEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(1, 6, new GUIItem(Material.REDSTONE, "&3Ignore Hub Knockback", 1, ";&r&fBecome completely invulnerable to;&r&fKnockback in our Lobby Servers."));
        this.setItem(2, 6, new GUIItem(((player.getPreferences().isIgnoreHubKnockbackEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ignore Hub Knockback", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&cDisabled":"&aEnabled"))));

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case GRAY_DYE:
            case LIME_DYE:
            case ORANGE_DYE:
                if (column == 2) {
                    player.getPreferences().setHubInvisibility(!player.getPreferences().isHubInvisibilityEnabled(), true);
                    this.updateItem(2, 2, new GUIItem(((player.getPreferences().isHubInvisibilityEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Hub Invisibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubInvisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubInvisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubInvisibilityEnabled())?(short)10:(short)8)));
                } else if (column == 4) {
                    player.getPreferences().setSocialMediaNotifications(!player.getPreferences().isSocialMediaNotificationsEnabled(), true);
                    this.updateItem(2, 4, new GUIItem(((player.getPreferences().isSocialMediaNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Social Media Login Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isSocialMediaNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isSocialMediaNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isSocialMediaNotificationsEnabled())?(short)10:(short)8)));
                } else {
                    player.getPreferences().setIgnoreHubKnockback(!player.getPreferences().isIgnoreHubKnockbackEnabled(), true);
                    this.updateItem(2, 6, new GUIItem(((player.getPreferences().isIgnoreHubKnockbackEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ignore Hub Knockback", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?(short)10:(short)8)));
                }
                break;
            case ARROW:
                Preferences prefs = new Preferences(player);
                prefs.open(player);
                break;
            default:
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                break;
        }
    }

}
