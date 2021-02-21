package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class StaffManagementPreferences extends GUI {

    private AuroraMCPlayer player;

    public StaffManagementPreferences(AuroraMCPlayer player) {
        super("&3&lStaff Management Preferences", 3, true);
        this.player = player;

        border("&3&lStaff Management Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 2, new GUIItem(Material.REDSTONE_TORCH_ON, "&3Staff Login Notifications", 1, ";&rGives you a notifications if a Junior Moderator;&ror Moderator joins or leaves the network."));
        this.setItem(2, 2, new GUIItem(Material.INK_SACK, "&3Staff Login Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isStaffLoginNotificationsEnabled())?(short)10:(short)8)));

        this.setItem(1, 4, new GUIItem(Material.ANVIL, "&3Approval Notifications", 1, ";&rGives you a notification when there;&ris a new punishment to be approved."));
        this.setItem(2, 4, new GUIItem(Material.INK_SACK, "&3Approval Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isApprovalNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isApprovalNotificationsEnabled())?(short)10:(short)8)));

        this.setItem(1, 6, new GUIItem(Material.PAPER, "&3Approval Processed Notifications", 1, ";&rGives you a notification in chat when another;&ruser processes an approval punishment.")) ;
        this.setItem(2, 6, new GUIItem(Material.INK_SACK, "&3Approval Processed Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?(short)10:(short)8)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                if (column == 2) {
                    player.getPreferences().setStaffLoginNotifications(!player.getPreferences().isStaffLoginNotificationsEnabled());
                    this.updateItem(2, 2, new GUIItem(Material.INK_SACK, "&3Staff Login Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isStaffLoginNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isStaffLoginNotificationsEnabled())?(short)10:(short)8)));
                } else if (column == 4) {
                    player.getPreferences().setApprovalNotifications(!player.getPreferences().isApprovalNotificationsEnabled());
                    this.updateItem(2, 4, new GUIItem(Material.INK_SACK, "&3Approval Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isApprovalNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isApprovalNotificationsEnabled())?(short)10:(short)8)));
                } else {
                    player.getPreferences().setApprovalProcessedNotifications(!player.getPreferences().isApprovalProcessedNotificationsEnabled());
                    this.updateItem(2, 6, new GUIItem(Material.INK_SACK, "&3Approval Processed Notifications", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isApprovalProcessedNotificationsEnabled())?(short)10:(short)8)));
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
