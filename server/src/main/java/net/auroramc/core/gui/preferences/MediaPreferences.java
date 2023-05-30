/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MediaPreferences extends GUI {

    private AuroraMCServerPlayer player;

    public MediaPreferences(AuroraMCServerPlayer player) {
        super("&3&lMedia Preferences", 5, true);
        this.player = player;

        border("&3&lMedia Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.SUGAR_CANE, "&3Hub Invisibility", 1, ";&r&fBecome completely invisible to all other players;&r&fwhilst you are roaming around the lobby."));
        this.setItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Invisibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubInvisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubInvisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubInvisibilityEnabled())?(short)10:(short)8)));

        this.setItem(1, 5, new GUIItem(Material.REDSTONE, "&3Ignore Hub Knockback", 1, ";&r&fBecome completely invulnerable to;&r&fKnockback in our Lobby Servers."));
        this.setItem(2, 5, new GUIItem(Material.INK_SACK, "&3Ignore Hub Knockback", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?(short)10:(short)8)));

        this.setItem(3, 3, new GUIItem(Material.HOPPER, "&3Hub Forcefield", 1, ";&r&fPrevent any player from being able to come;&r&fwithin a certain radius of you in the Lobby.")) ;
        this.setItem(4, 3, new GUIItem(Material.INK_SACK, "&3Hub Forcefield", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubForcefieldEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubForcefieldEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubForcefieldEnabled())?(short)10:(short)8)));

        this.setItem(3, 5, new GUIItem(Material.PAINTING, "&3Hide Disguise Name", 1, ";&r&fHave your disguise username show as your;&r&freal username, on your screen only.")) ;
        this.setItem(4, 5, new GUIItem(Material.INK_SACK, "&3Hide Disguise Name", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHideDisguiseNameEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHideDisguiseNameEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHideDisguiseNameEnabled())?(short)10:(short)8)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                switch (row) {
                    case 2:
                        if (column == 3) {
                            player.getPreferences().setHubInvisibility(!player.getPreferences().isHubInvisibilityEnabled(), true);
                            this.updateItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Invisibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubInvisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubInvisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubInvisibilityEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setIgnoreHubKnockback(!player.getPreferences().isIgnoreHubKnockbackEnabled(), true);
                            this.updateItem(2, 5, new GUIItem(Material.INK_SACK, "&3Ignore Hub Knockback", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?(short)10:(short)8)));
                        }
                        break;
                    case 4:
                        if (column == 3) {
                            player.getPreferences().setHubForcefield(!player.getPreferences().isHubForcefieldEnabled(), true);
                            this.updateItem(4, 3, new GUIItem(Material.INK_SACK, "&3Hub Forcefield", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubForcefieldEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubForcefieldEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubForcefieldEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setHideDisguiseName(!player.getPreferences().isHideDisguiseNameEnabled(), true);
                            this.updateItem(4, 5, new GUIItem(Material.INK_SACK, "&3Hide Disguise Name", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHideDisguiseNameEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHideDisguiseNameEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHideDisguiseNameEnabled())?(short)10:(short)8)));
                        }
                        break;
                }
                break;
            case ARROW:
                Preferences prefs = new Preferences(player);
                prefs.open(player);
                break;
            default:
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                break;
        }
    }

}
