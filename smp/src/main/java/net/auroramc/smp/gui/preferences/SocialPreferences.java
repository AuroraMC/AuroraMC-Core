/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.preferences;

import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.utils.Pronoun;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
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

        this.setItem(1, 3, new GUIItem(Material.PLAYER_HEAD, "&3Friend Requests", 1, ";&r&fDisabling this will prevent any player from;&r&fbeing able to send you friend requests."));
        this.setItem(2, 3, new GUIItem(((player.getPreferences().isFriendRequestsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Friend Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isFriendRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isFriendRequestsEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(1, 5, new GUIItem(Material.FIREWORK_ROCKET, "&3Party Requests", 1, ";&r&fDisabling this will prevent any player from;&r&fbeing able to invite you to a party."));
        this.setItem(2, 5, new GUIItem(((player.getPreferences().isPartyRequestsEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Party Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPartyRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPartyRequestsEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isPartyRequestsEnabled())?(short)10:(short)8)));

        this.setItem(3, 3, new GUIItem(Material.BARRIER, "&3Inform When Muted", 1, ";&r&fAutomatically notify players that you;&r&fare muted upon being messaged.;;&bThis preference only works while muted.")) ;
        this.setItem(4, 3, new GUIItem(((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?Material.LIME_DYE:((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?Material.ORANGE_DYE:Material.GRAY_DYE)), "&3Inform When Muted", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&aMentions & Private Messages":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&6Private Messages Only":"&cDisabled")), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&6Private Messages Only":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&cDisabled":"&aMentions & Private Messages")))));

        this.setItem(3, 5, new GUIItem(Material.BOOK, "&3Preferred Pronouns", 1, ";&r&fDisplay your preferred pronouns;&r&fin tab and in chat.;;&bThis preference only works in tab when you;&bdo not have a Plus symbol active."));
        this.setItem(4, 5, new GUIItem(Material.LIME_DYE, "&3Preferred Pronouns", 1, String.format(";&r&fCurrent: &b%s;&r&fClick to change to: &b%s", player.getPreferences().getPreferredPronouns().getDisplay(), Pronoun.values()[(player.getPreferences().getPreferredPronouns().ordinal()+1) % Pronoun.values().length].getDisplay())));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case GRAY_DYE:
            case ORANGE_DYE:
            case LIME_DYE:
                if (column == 3 && row == 2) {
                    player.getPreferences().setFriendRequests(!player.getPreferences().isFriendRequestsEnabled(), true);
                    this.updateItem(2, 3, new GUIItem(((player.getPreferences().isFriendRequestsEnabled())?Material.LIME_DYE:Material.LIGHT_GRAY_DYE), "&3Friend Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isFriendRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isFriendRequestsEnabled())?"&cDisabled":"&aEnabled"))));
                } else if (column == 5 && row == 2) {
                    player.getPreferences().setPartyRequests(!player.getPreferences().isPartyRequestsEnabled(), true);
                    this.updateItem(2, 5, new GUIItem(((player.getPreferences().isPartyRequestsEnabled())?Material.LIME_DYE:Material.LIGHT_GRAY_DYE), "&3Party Requests", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPartyRequestsEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPartyRequestsEnabled())?"&cDisabled":"&aEnabled"))));
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
                    this.updateItem(4, 3, new GUIItem(((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?Material.LIME_DYE:((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?Material.ORANGE_DYE:Material.LIGHT_GRAY_DYE)), "&3Inform When Muted", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&aMentions & Private Messages":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&6Private Messages Only":"&cDisabled")), ((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS)?"&6Private Messages Only":((player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_ONLY)?"&cDisabled":"&aMentions & Private Messages")))));

                } else {
                    player.getPreferences().setPreferredPronouns(Pronoun.values()[(player.getPreferences().getPreferredPronouns().ordinal()+1) % Pronoun.values().length], true);
                    for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                        player1.updateNametag(player);
                    }
                    this.updateItem(4, 5, new GUIItem(Material.LIME_DYE, "&3Preferred Pronouns", 1, String.format(";&r&fCurrent: &b%s;&r&fClick to change to: &b%s", player.getPreferences().getPreferredPronouns().getDisplay(), Pronoun.values()[(player.getPreferences().getPreferredPronouns().ordinal()+1) % Pronoun.values().length].getDisplay())));
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
