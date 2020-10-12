package network.auroramc.core.gui.preferences;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.PlayerPreferences;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ChatPreferences extends GUI {

    private final AuroraMCPlayer player;

    public ChatPreferences(AuroraMCPlayer player) {
        super("&3&lChat Preferences", 5, true);
        this.player = player;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChat Preferences", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChat Preferences", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChat Preferences", 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChat Preferences", 1, "", (short) 7));
        }
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.PAPER, "&3Chat Visibility", 1, ";&rDisabling this will prevent you from being;&rable to view other player's chat messages."));
        this.setItem(2, 3, new GUIItem(Material.INK_SACK, "&3Chat Visibility", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isChatVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isChatVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isChatVisibilityEnabled())?(short)10:(short)8)));

        this.setItem(1, 5, new GUIItem(Material.EMPTY_MAP, "&3Private Messages", 1, ";&rToggle which players are able to;&rsend you private messages."));
        this.setItem(2, 5, new GUIItem(Material.INK_SACK, "&3Private Messages", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&aAll":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&6Friends Only":"&cDisabled")), ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&6Friends Only":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&cDisabled":"&aAll"))), ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?(short)10:((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?(short)14:(short)8))));

        this.setItem(3, 3, new GUIItem(Material.NOTE_BLOCK, "&3Ping on Party Chat", 1, ";&rDisabling this will stop you from being pinged;&rwhen you recieve a message in party chat.")) ;
        this.setItem(4, 3, new GUIItem(Material.INK_SACK, "&3Ping on Party Chat", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isPingOnPartyChatEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPartyChatEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPingOnPartyChatEnabled())?(short)10:(short)8)));

        this.setItem(3, 5, new GUIItem(Material.NOTE_BLOCK, "&3Ping on Private Message", 1, ";&rDisabling this will stop you from being pinged;&rwhen you recieve a PM.")) ;
        this.setItem(4, 5, new GUIItem(Material.INK_SACK, "&3Ping on Private Message", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPingOnPrivateMessageEnabled())?(short)10:(short)8)));

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                switch (row) {
                    case 2:
                        if (column == 3) {
                            player.getPreferences().setChatVisibility(!player.getPreferences().isChatVisibilityEnabled());
                            this.updateItem(2, 3, new GUIItem(Material.INK_SACK, "&3Chat Visibility", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isChatVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isChatVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isChatVisibilityEnabled())?(short)10:(short)8)));
                        } else {
                            switch (player.getPreferences().getPrivateMessageMode()) {
                                case ALL:
                                    player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY);
                                    break;
                                case FRIENDS_ONLY:
                                    player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.DISABLED);
                                    break;
                                case DISABLED:
                                    player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.ALL);
                                    break;
                            }
                            this.updateItem(2, 5, new GUIItem(Material.INK_SACK, "&3Private Messages", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&aAll":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&6Friends Only":"&cDisabled")), ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&6Friends Only":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&cDisabled":"&aAll"))), ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?(short)10:((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?(short)14:(short)8))));
                        }
                        break;
                    case 4:
                        if (column == 3) {
                            player.getPreferences().setPingOnPartyChat(!player.getPreferences().isPingOnPartyChatEnabled());
                            this.updateItem(4, 3, new GUIItem(Material.INK_SACK, "&3Ping on Party Chat", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isPingOnPartyChatEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPartyChatEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPingOnPartyChatEnabled())?(short)10:(short)8)));
                        } else {
                            player.getPreferences().setPingOnPrivateMessage(!player.getPreferences().isPingOnPrivateMessageEnabled());
                            this.updateItem(4, 5, new GUIItem(Material.INK_SACK, "&3Ping on Private Message", 1, String.format(";&rMode: %s;&rClick to change to: %s", ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPingOnPrivateMessageEnabled())?(short)10:(short)8)));
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
