/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.punish;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.punishments.*;
import net.auroramc.api.utils.PunishUtils;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RuleListing extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final int id;
    private final int type;
    private final String extraDetails;
    private final PunishmentHistory history;

    public RuleListing(AuroraMCServerPlayer player, String name, int id, int type, String extraDetails, List<Punishment> punishmentHistory) {
        super(String.format("&3&lPunish %s - %s", name, Type.TYPES[type - 1]), 5, true);

        this.player = player;
        this.name = name;
        this.id = id;
        this.type = type;
        this.extraDetails = extraDetails;
        this.history = new PunishmentHistory(id);

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&lPunish %s", name), 1, "&r&fPlease choose a rule.", (short)0, false, name));

        for (Punishment punishment : punishmentHistory) {
            history.registerPunishment(punishment);
        }


        int row = 2;
        int column = 0;
        for (int i = 1;i <= 5;i++) {
            //For each weight
            row = 2;
            column = (i-1)*2;
            this.setItem(1, (i-1)*2, new GUIItem(Material.valueOf(Weight.WEIGHT_ICON_MATERIAL[i-1]), Weight.WEIGHTS[i-1], 1, "&r&fPunishment Length: &b" + history.getType(type).generateLength(i).getFormatted()));
            for (Rule rule : AuroraMCAPI.getRules().getType(type).getWeight(i).getRules()) {
                if (rule.isActive()) {
                    this.setItem(row, column, new GUIItem(Material.BOOK, String.format("&3&l%s", rule.getRuleName()), 1, String.format("&r&f%s;;&r&fID: &b%s", WordUtils.wrap(rule.getRuleDescription(), 40, ";&r&f", false), rule.getRuleID())));
                    row++;
                    if (row > 5) {
                        column++;
                        row = 2;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() != Material.BOOK) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }

        List<String> lore = item.getItemMeta().getLore();
        String sid = lore.get(lore.size() - 1);
        sid = ChatColor.stripColor(sid.split(" ")[1]);
        int ruleId = Integer.parseInt(sid);

        Rule rule = AuroraMCAPI.getRules().getRule(ruleId);

        player.closeInventory();

        PunishUtils.punishUser(id, name, player, rule, extraDetails);
    }
}
