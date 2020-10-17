package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MediaPreferences extends GUI {

    private AuroraMCPlayer player;

    public MediaPreferences(AuroraMCPlayer player) {
        super("&3&lMedia Preferences", 5, true);
        this.player = player;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lStaff Preferences", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lStaff Preferences", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lStaff Preferences", 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lStaff Preferences", 1, "", (short) 7));
        }
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.SUGAR_CANE, "&3Hub Invisibility", 1, ";&rBecome completely invisible to all other players;&rwhilst you are roaming around the lobby."));
        this.setItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Invisibility", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubInvisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubInvisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubInvisibilityEnabled())?(short)10:(short)8)));

        this.setItem(1, 5, new GUIItem(Material.REDSTONE, "&3Ignore Hub Knockback", 1, ";&rBecome completely invulnerable to;&rKnockback in our Lobby Servers."));
        this.setItem(2, 5, new GUIItem(Material.INK_SACK, "&3Ignore Hub Knockback", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?(short)10:(short)8)));

        this.setItem(3, 3, new GUIItem(Material.HOPPER, "&3Hub Forcefield", 1, ";&rPrevent any player from being able to come;&rwithin a certain radius of you in the Lobby.")) ;
        this.setItem(4, 3, new GUIItem(Material.INK_SACK, "&3Hub Forcefield", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubForcefieldEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubForcefieldEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubForcefieldEnabled())?(short)10:(short)8)));

        this.setItem(3, 5, new GUIItem(Material.PAINTING, "&3Hide Disguise Name", 1, ";&rHave your disguise username show as your;&rreal username, on your screen only.")) ;
        this.setItem(4, 5, new GUIItem(Material.INK_SACK, "&3Hide Disguise Name", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHideDisguiseNameEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHideDisguiseNameEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHideDisguiseNameEnabled())?(short)10:(short)8)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                switch (row) {
                    case 2:
                        if (column == 3) {
                            player.getPreferences().setHubInvisibility(!player.getPreferences().isHubInvisibilityEnabled());
                            this.updateItem(2, 3, new GUIItem(Material.INK_SACK, "&3Hub Invisibility", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubInvisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubInvisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubInvisibilityEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setIgnoreHubKnockback(!player.getPreferences().isIgnoreHubKnockbackEnabled());
                            this.updateItem(2, 5, new GUIItem(Material.INK_SACK, "&3Ignore Hub Knockback", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isIgnoreHubKnockbackEnabled())?(short)10:(short)8)));
                        }
                        break;
                    case 4:
                        if (column == 3) {
                            player.getPreferences().setHubForcefield(!player.getPreferences().isHubForcefieldEnabled());
                            this.updateItem(4, 3, new GUIItem(Material.INK_SACK, "&3Hub Forcefield", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHubForcefieldEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHubForcefieldEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHubForcefieldEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setHideDisguiseName(!player.getPreferences().isHideDisguiseNameEnabled());
                            this.updateItem(4, 5, new GUIItem(Material.INK_SACK, "&3Hide Disguise Name", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isHideDisguiseNameEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isHideDisguiseNameEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isHideDisguiseNameEnabled())?(short)10:(short)8)));
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
