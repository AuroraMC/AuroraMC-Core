/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.misc;

import net.auroramc.api.utils.GameLog;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;

public class ViewGames extends GUI {

    private List<GameLog> logs;
    private AuroraMCServerPlayer player;

    public ViewGames(AuroraMCServerPlayer player, List<GameLog> logs, String name) {
        super("&3&l" + ((name != null)?name + "'s ":"") + "Recent Games", 5, true);
        border("&3&l" + ((name != null)?name + "'s ":"") + "Recent Games", null);

        this.player = player;
        this.logs = logs;

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, "&3&l" + ((name != null)?name + "'s ":"") + "Recent Games", 1, ";&r&fView all recent games " + ((name != null)?name + " has":"you've") +" played.", (short)0, false, ((name != null)?name:player.getName())));

        int row = 1;
        int column = 1;
        for (GameLog log : logs) {
            this.setItem(row, column, new GUIItem(Material.valueOf(log.getGame().getItem1_19()), log.getGame().getName(), 1, ((log.getData().getBoolean("void"))?"&r&4&lVOID GAME;":"") + ";&r&fGame UUID: **" + log.getUuid() + "**;&r&fGame Started: **" + new Date(log.getData().getLong("start")) + "**;&r&fGame Ended:** " + new Date(log.getData().getLong("end")) + "**;;&r&aClick to get the link to the game log!"));

            column++;
            if (column == 8) {
                row++;
                column = 1;
                if (row == 5) {
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
         if (item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.PLAYER_HEAD) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }
        //Get the clicked punishment.
        GameLog log = logs.get(((row - 1) * 7) + (column - 1));

        TextComponent click = new TextComponent("Click here to open the game log!");
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to open game log!").color(ChatColor.GREEN).create()));
        click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://gamelogs.auroramc.net/log?uuid=" + log.getUuid()));
        click.setColor(ChatColor.GREEN);
        click.setBold(true);

         player.sendMessage(click);
         player.closeInventory();
    }
}
