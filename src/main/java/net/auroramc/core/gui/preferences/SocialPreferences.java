package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerPreferences;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SocialPreferences extends GUI {

    private AuroraMCPlayer player;

    public SocialPreferences(AuroraMCPlayer player) {
        super("&3&lSocial Preferences", 3, true);
        this.player = player;

        border("&3&lSocial Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 2, new GUIItem(Material.SKULL_ITEM, "&3Friend Requests", 1, ";&rDisabling this will prevent any player from;&rbeing able to send you friend requests.", (short)3));
        this.setItem(2, 2, new GUIItem(Material.INK_SACK, "&3Friend Requests", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isFriendRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isFriendRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isFriendRequestsEnabled())?(short)10:(short)8)));

        this.setItem(1, 4, new GUIItem(Material.FIREWORK, "&3Party Requests", 1, ";&rDisabling this will prevent any player from;&rbeing able to invite you to a party."));
        this.setItem(2, 4, new GUIItem(Material.INK_SACK, "&3Party Requests", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isPartyRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPartyRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPartyRequestsEnabled())?(short)10:(short)8)));

        this.setItem(1, 6, new GUIItem(Material.BARRIER, "&3Inform When Muted", 1, ";&rAutomatically notify players that you;&rare muted upon being messaged.;;&bThis preference only works while muted.")) ;
        this.setItem(2, 6, new GUIItem(Material.INK_SACK, "&3Inform When Muted", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&aMessages & Private Messages":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&6Private Messages Only":"&cDisabled")), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&6Private Messages Only":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&cDisabled":"&aMentions & Private Messages"))), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?(short)10:((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?(short)14:(short)8))));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                if (column == 2) {
                    player.getPreferences().setFriendRequests(!player.getPreferences().isFriendRequestsEnabled());
                    this.updateItem(2, 2, new GUIItem(Material.INK_SACK, "&3Friend Requests", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isFriendRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isFriendRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isFriendRequestsEnabled())?(short)10:(short)8)));
                } else if (column == 4) {
                    player.getPreferences().setPartyRequests(!player.getPreferences().isPartyRequestsEnabled());
                    this.updateItem(2, 4, new GUIItem(Material.INK_SACK, "&3Party Requests", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isPartyRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPartyRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPartyRequestsEnabled())?(short)10:(short)8)));
                } else {
                    switch (player.getPreferences().getMuteInformMode()) {
                        case MESSAGE_AND_MENTIONS:
                            player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.MESSAGE_ONLY);
                            break;
                        case MESSAGE_ONLY:
                            player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.DISABLED);
                            break;
                        case DISABLED:
                            player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS);
                            break;
                    }
                    this.updateItem(2, 6, new GUIItem(Material.INK_SACK, "&3Inform When Muted", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&aMessages & Private Messages":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&6Private Messages Only":"&cDisabled")), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&6Private Messages Only":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&cDisabled":"&aMentions & Private Messages"))), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?(short)10:((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?(short)14:(short)8))));
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
