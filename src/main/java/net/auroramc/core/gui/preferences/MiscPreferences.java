/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MiscPreferences extends GUI {

    private AuroraMCPlayer player;

    public MiscPreferences(AuroraMCPlayer player) {
        super("&3&lMisc Preferences", 5, true);
        this.player = player;

        border("&3&lMisc Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.SUGAR_CANE, "&3Hub Player Visibility", 1, ";&rDisabling this will make all other players;&rinvisible whilst you are in the Lobby."));
        this.setItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Player Visibility", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubVisibilityEnabled())?(short)10:(short)8)));

        this.setItem(1, 5, new GUIItem(Material.REDSTONE, "&3Hub Flight", 1, ";&rRecieve the ability of continuous flight;&rwhilst you are in one of our Lobbies.;;&bYou must have Plus or a Premium Rank in;&border to use this preference."));
        if (player.hasPermission("elite") || player.hasPermission("plus")) {
            this.setItem(2, 5, new GUIItem(Material.INK_SACK, "&3Hub Flight", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubFlightEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubFlightEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubFlightEnabled())?(short)10:(short)8)));
        } else {
            this.setItem(2, 5, new GUIItem(Material.BARRIER, "&3Hub Flight", 1, ";&cYou must have Plus or a Premium Rank;&cin order to use this preference."));
        }

        this.setItem(3, 3, new GUIItem(Material.HOPPER, "&3Report Notifications", 1, ";&rToggle the report handled messages;&ryou get when you login.")) ;
        this.setItem(4, 3, new GUIItem(Material.INK_SACK, "&3Report Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isReportNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isReportNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isReportNotificationsEnabled())?(short)10:(short)8)));

        this.setItem(3, 5, new GUIItem(Material.PAINTING, "&3Speed in Hub", 1, ";&rReceive the ability to speed around;&rthe hub with extra speed.")) ;
        this.setItem(4, 5, new GUIItem(Material.INK_SACK, "&3Speed in Hub", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubSpeedEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubSpeedEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubSpeedEnabled())?(short)10:(short)8)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                switch (row) {
                    case 2:
                        if (column == 3) {
                            player.getPreferences().setHubVisibility(!player.getPreferences().isHubVisibilityEnabled());
                            this.updateItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Player Visibility", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubVisibilityEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setHubFlight(!player.getPreferences().isHubFlightEnabled());
                            this.updateItem(2, 5, new GUIItem(Material.INK_SACK, "&3Hub Flight", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubFlightEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubFlightEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubFlightEnabled())?(short)10:(short)8)));
                        }
                        break;
                    case 4:
                        if (column == 3) {
                            player.getPreferences().setReportNotifications(!player.getPreferences().isReportNotificationsEnabled());
                            this.updateItem(4, 3, new GUIItem(Material.INK_SACK, "&3Report Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isReportNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isReportNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isReportNotificationsEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setHubSpeed(!player.getPreferences().isHubSpeedEnabled());
                            this.updateItem(4, 5, new GUIItem(Material.INK_SACK, "&3Speed in Hub", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubSpeedEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubSpeedEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubSpeedEnabled())?(short)10:(short)8)));
                        }
                        break;
                }
                break;
            case ARROW:
                Preferences prefs = new Preferences(player);
                prefs.open(player);
                AuroraMCAPI.openGUI(player, prefs);
                break;
            default:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                break;
        }
    }

}
