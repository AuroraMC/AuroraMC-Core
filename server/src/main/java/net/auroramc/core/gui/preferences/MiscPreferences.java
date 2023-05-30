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

public class MiscPreferences extends GUI {

    private AuroraMCServerPlayer player;

    public MiscPreferences(AuroraMCServerPlayer player) {
        super("&3&lMisc Preferences", 5, true);
        this.player = player;

        border("&3&lMisc Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.SUGAR_CANE, "&3Hub Player Visibility", 1, ";&r&fDisabling this will make all other players;&r&finvisible whilst you are in the Lobby."));
        this.setItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Player Visibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubVisibilityEnabled())?(short)10:(short)8)));

        this.setItem(1, 5, new GUIItem(Material.REDSTONE, "&3Hub Flight", 1, ";&r&fRecieve the ability of continuous flight;&r&fwhilst you are in one of our Lobbies.;;&bYou must have Plus or a Premium Rank in;&border to use this preference."));
        if (player.hasPermission("elite") || player.hasPermission("plus")) {
            this.setItem(2, 5, new GUIItem(Material.INK_SACK, "&3Hub Flight", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubFlightEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubFlightEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubFlightEnabled())?(short)10:(short)8)));
        } else {
            this.setItem(2, 5, new GUIItem(Material.BARRIER, "&3Hub Flight", 1, ";&cYou must have Plus or a Premium Rank;&cin order to use this preference."));
        }

        this.setItem(3, 3, new GUIItem(Material.HOPPER, "&3Report Notifications", 1, ";&r&fToggle the report handled messages;&r&fyou get when you login.")) ;
        this.setItem(4, 3, new GUIItem(Material.INK_SACK, "&3Report Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isReportNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isReportNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isReportNotificationsEnabled())?(short)10:(short)8)));

        this.setItem(3, 5, new GUIItem(Material.PAINTING, "&3Speed in Hub", 1, ";&r&fReceive the ability to speed around;&r&fthe hub with extra speed.")) ;
        this.setItem(4, 5, new GUIItem(Material.INK_SACK, "&3Speed in Hub", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubSpeedEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubSpeedEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubSpeedEnabled())?(short)10:(short)8)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                switch (row) {
                    case 2:
                        if (column == 3) {
                            player.getPreferences().setHubVisibility(!player.getPreferences().isHubVisibilityEnabled(), true);
                            this.updateItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Player Visibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubVisibilityEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setHubFlight(!player.getPreferences().isHubFlightEnabled(), true);
                            this.updateItem(2, 5, new GUIItem(Material.INK_SACK, "&3Hub Flight", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubFlightEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubFlightEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubFlightEnabled())?(short)10:(short)8)));
                        }
                        break;
                    case 4:
                        if (column == 3) {
                            player.getPreferences().setReportNotifications(!player.getPreferences().isReportNotificationsEnabled(), true);
                            this.updateItem(4, 3, new GUIItem(Material.INK_SACK, "&3Report Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isReportNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isReportNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isReportNotificationsEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setHubSpeed(!player.getPreferences().isHubSpeedEnabled(), true);
                            this.updateItem(4, 5, new GUIItem(Material.INK_SACK, "&3Speed in Hub", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isHubSpeedEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubSpeedEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubSpeedEnabled())?(short)10:(short)8)));
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
