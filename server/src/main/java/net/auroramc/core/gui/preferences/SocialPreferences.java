/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.gui.preferences;

import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.utils.Pronoun;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SocialPreferences extends GUI {

    private AuroraMCServerPlayer player;

    public SocialPreferences(AuroraMCServerPlayer player) {
        super("&3&lSocial Preferences", 5, true);
        this.player = player;

        border("&3&lSocial Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.SKULL_ITEM, "&3Friend Requests", 1, ";&r&fDisabling this will prevent any player from;&r&fbeing able to send you friend requests.", (short)3));
        this.setItem(2, 3, new GUIItem(Material.INK_SACK, "&3Friend Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isFriendRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isFriendRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isFriendRequestsEnabled())?(short)10:(short)8)));

        this.setItem(1, 5, new GUIItem(Material.FIREWORK, "&3Party Requests", 1, ";&r&fDisabling this will prevent any player from;&r&fbeing able to invite you to a party."));
        this.setItem(2, 5, new GUIItem(Material.INK_SACK, "&3Party Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPartyRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPartyRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPartyRequestsEnabled())?(short)10:(short)8)));

        this.setItem(3, 3, new GUIItem(Material.BARRIER, "&3Inform When Muted", 1, ";&r&fAutomatically notify players that you;&r&fare muted upon being messaged.;;&bThis preference only works while muted.")) ;
        this.setItem(4, 3, new GUIItem(Material.INK_SACK, "&3Inform When Muted", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&aMentions & Private Messages":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&6Private Messages Only":"&cDisabled")), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&6Private Messages Only":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&cDisabled":"&aMentions & Private Messages"))), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?(short)10:((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?(short)14:(short)8))));

        this.setItem(3, 5, new GUIItem(Material.BOOK, "&3Preferred Pronouns", 1, ";&r&fDisplay your preferred pronouns;&r&fin tab and in chat.;;&bThis preference only works in tab when you;&bdo not have a Plus symbol active."));
        this.setItem(4, 5, new GUIItem(Material.INK_SACK, "&3Preferred Pronouns", 1, String.format(";&r&fCurrent: &b%s;&r&fClick to change to: &b%s", player.getPreferences().getPreferredPronouns().getDisplay(), Pronoun.values()[(player.getPreferences().getPreferredPronouns().ordinal()+1) % Pronoun.values().length].getDisplay()), (short)10));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:
                if (column == 3 && row == 2) {
                    player.getPreferences().setFriendRequests(!player.getPreferences().isFriendRequestsEnabled(), true);
                    this.updateItem(2, 3, new GUIItem(Material.INK_SACK, "&3Friend Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isFriendRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isFriendRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isFriendRequestsEnabled())?(short)10:(short)8)));
                } else if (column == 5 && row == 2) {
                    player.getPreferences().setPartyRequests(!player.getPreferences().isPartyRequestsEnabled(), true);
                    this.updateItem(2, 5, new GUIItem(Material.INK_SACK, "&3Party Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPartyRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPartyRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPartyRequestsEnabled())?(short)10:(short)8)));
                } else if (column == 3 && row == 4) {
                    switch (player.getPreferences().getMuteInformMode()) {
                        case MESSAGE_AND_MENTIONS:
                            player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.MESSAGE_ONLY, true);
                            break;
                        case MESSAGE_ONLY:
                            player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.DISABLED, true);
                            break;
                        case DISABLED:
                            player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS, true);
                            break;
                    }
                    this.updateItem(4, 3, new GUIItem(Material.INK_SACK, "&3Inform When Muted", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&aMentions & Private Messages":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&6Private Messages Only":"&cDisabled")), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&6Private Messages Only":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&cDisabled":"&aMentions & Private Messages"))), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?(short)10:((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?(short)14:(short)8))));

                } else {
                    player.getPreferences().setPreferredPronouns(Pronoun.values()[(player.getPreferences().getPreferredPronouns().ordinal()+1) % Pronoun.values().length], true);
                    for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                        player1.updateNametag(player);
                    }
                    this.updateItem(4, 5, new GUIItem(Material.INK_SACK, "&3Preferred Pronouns", 1, String.format(";&r&fCurrent: &b%s;&r&fClick to change to: &b%s", player.getPreferences().getPreferredPronouns().getDisplay(), Pronoun.values()[(player.getPreferences().getPreferredPronouns().ordinal()+1) % Pronoun.values().length].getDisplay()), (short)10));
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
