/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.gui.preferences;

import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ChatPreferences extends GUI {

    private final AuroraMCServerPlayer player;

    public ChatPreferences(AuroraMCServerPlayer player) {
        super("&3&lChat Preferences", 5, true);
        this.player = player;

        border("&3&lChat Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        this.setItem(1, 3, new GUIItem(Material.PAPER, "&3Chat Visibility", 1, ";&r&fDisabling this will prevent you from being;&r&fable to view other player's chat messages."));
        this.setItem(2, 3, new GUIItem(((player.getPreferences().isChatVisibilityEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Chat Visibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isChatVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isChatVisibilityEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(1, 5, new GUIItem(Material.MAP, "&3Private Messages", 1, ";&r&fToggle which players are able to;&r&fsend you private messages."));
        this.setItem(2, 5, new GUIItem(((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?Material.LIME_DYE:((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?Material.ORANGE_DYE:Material.GRAY_DYE)), "&3Private Messages", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&aAll":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&6Friends Only":"&cDisabled")), ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&6Friends Only":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&cDisabled":"&aAll")))));

        this.setItem(3, 2, new GUIItem(Material.NOTE_BLOCK, "&3Ping on Party Chat", 1, ";&r&fDisabling this will stop you from being pinged;&r&fwhen you recieve a message in party chat.")) ;
        this.setItem(4, 2, new GUIItem(((player.getPreferences().isPingOnPartyChatEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ping on Party Chat", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPingOnPartyChatEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPartyChatEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(3, 4, new GUIItem(Material.NOTE_BLOCK, "&3Ping on Private Message", 1, ";&r&fDisabling this will stop you from being pinged;&r&fwhen you recieve a PM.")) ;
        this.setItem(4, 4, new GUIItem(((player.getPreferences().isPingOnPrivateMessageEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ping on Private Message", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&cDisabled":"&aEnabled"))));

        this.setItem(3, 6, new GUIItem(Material.NOTE_BLOCK, "&3Ping on Chat Mention", 1, ";&r&fDisabling this will stop you from being pinged;&r&fwhen you recieve a chat mention.")) ;
        this.setItem(4, 6, new GUIItem(((player.getPreferences().isPingOnChatMentionEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ping on Chat Mention", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPingOnChatMentionEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnChatMentionEnabled())?"&cDisabled":"&aEnabled"))));

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case GRAY_DYE:
            case LIME_DYE:
            case ORANGE_DYE:
                switch (row) {
                    case 2:
                        if (column == 3) {
                            player.getPreferences().setChatVisibility(!player.getPreferences().isChatVisibilityEnabled(), true);
                            this.updateItem(2, 3, new GUIItem(((player.getPreferences().isChatVisibilityEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Chat Visibility", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isChatVisibilityEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isChatVisibilityEnabled())?"&cDisabled":"&aEnabled")), ((player.getPreferences().isChatVisibilityEnabled())?(short)10:(short)8)));
                        } else {
                            switch (player.getPreferences().getPrivateMessageMode()) {
                                case ALL:
                                    player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY, true);
                                    break;
                                case FRIENDS_ONLY:
                                    player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.DISABLED, true);
                                    break;
                                case DISABLED:
                                    player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.ALL, true);
                                    break;
                            }
                            this.updateItem(2, 5, new GUIItem(((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?Material.LIME_DYE:((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?Material.ORANGE_DYE:Material.GRAY_DYE)), "&3Private Messages", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&aAll":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&6Friends Only":"&cDisabled")), ((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.ALL)?"&6Friends Only":((player.getPreferences().getPrivateMessageMode() == PlayerPreferences.PrivateMessageMode.FRIENDS_ONLY)?"&cDisabled":"&aAll")))));
                        }
                        break;
                    case 4:
                        if (column == 2) {
                            player.getPreferences().setPingOnPartyChat(!player.getPreferences().isPingOnPartyChatEnabled(), true);
                            this.updateItem(4, 2, new GUIItem(((player.getPreferences().isPingOnPartyChatEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ping on Party Chat", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPingOnPartyChatEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPartyChatEnabled())?"&cDisabled":"&aEnabled"))));
                        } else if (column == 4) {
                            player.getPreferences().setPingOnPrivateMessage(!player.getPreferences().isPingOnPrivateMessageEnabled(), true);
                            this.updateItem(4, 4, new GUIItem(((player.getPreferences().isPingOnPrivateMessageEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ping on Private Message", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnPrivateMessageEnabled())?"&cDisabled":"&aEnabled"))));
                        } else {
                            player.getPreferences().setPingOnChatMention(!player.getPreferences().isPingOnChatMentionEnabled(), true);
                            this.updateItem(4, 6, new GUIItem(((player.getPreferences().isPingOnChatMentionEnabled())?Material.LIME_DYE:Material.GRAY_DYE), "&3Ping on Chat Mention", 1, String.format(";&r&fMode: %s;&r&fClick to change to: %s", ((player.getPreferences().isPingOnChatMentionEnabled())?"&aEnabled":"&cDisabled"), ((player.getPreferences().isPingOnChatMentionEnabled())?"&cDisabled":"&aEnabled"))));
                        }
                        break;
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
