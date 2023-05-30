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

public class StaffManagementPreferences extends GUI {

    private AuroraMCServerPlayer player;

    public StaffManagementPreferences(AuroraMCServerPlayer player) {
        super("&3&lStaff Management Preferences", 3, true);
        this.player = player;

        border("&3&lStaff Management Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 2, new GUIItem(Material.REDSTONE_TORCH, "&3Staff Login Notifications", 1, ";&r&fGives you a notifications if a Junior Moderator;&r&for Moderator joins or leaves the network."));
        this.setItem(2, 2, new GUIItem(((player.getPreferences().isStaffLoginNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Staff Login Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(1, 4, new GUIItem(Material.ANVIL, "&3Approval Notifications", 1, ";&r&fGives you a notification when there;&r&fis a new punishment to be approved."));
        this.setItem(2, 4, new GUIItem(((player.getPreferences().isApprovalNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Approval Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isApprovalNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalNotificationsEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(1, 6, new GUIItem(Material.PAPER, "&3Approval Processed Notifications", 1, ";&r&fGives you a notification in chat when another;&r&fuser processes an approval punishment.")) ;
        this.setItem(2, 6, new GUIItem(((player.getPreferences().isApprovalProcessedNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Approval Processed Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&cDisabled":"&aEnabled"))));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case GRAY_DYE:
            case LIME_DYE:
            case ORANGE_DYE:
                if (column == 2) {
                    player.getPreferences().setStaffLoginNotifications(!player.getPreferences().isStaffLoginNotificationsEnabled(), true);
                    this.updateItem(2, 2, new GUIItem(((player.getPreferences().isStaffLoginNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Staff Login Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&cDisabled":"&aEnabled"))));
                } else if (column == 4) {
                    player.getPreferences().setApprovalNotifications(!player.getPreferences().isApprovalNotificationsEnabled(), true);
                    this.updateItem(2, 4, new GUIItem(((player.getPreferences().isApprovalNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Approval Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isApprovalNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalNotificationsEnabled())?"&cDisabled":"&aEnabled"))));
                } else {
                    player.getPreferences().setApprovalProcessedNotifications(!player.getPreferences().isApprovalProcessedNotificationsEnabled(), true);
                    this.updateItem(2, 6, new GUIItem(((player.getPreferences().isApprovalProcessedNotificationsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Approval Processed Notifications", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&cDisabled":"&aEnabled"))));
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
