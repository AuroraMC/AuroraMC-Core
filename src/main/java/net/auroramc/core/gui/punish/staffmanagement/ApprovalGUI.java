/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.punish.staffmanagement;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Mentee;
import net.auroramc.core.api.players.Mentor;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentLength;
import net.auroramc.core.api.punishments.Rule;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ApprovalGUI extends GUI {

    private List<Punishment> punishments;
    private AuroraMCPlayer player;
    private List<Mentor> mentors;
    private Mentor clickedMentor;
    private Mentee clickedMentee;
    private Punishment clickedPunishment;

    public ApprovalGUI(AuroraMCPlayer player, List<Mentor> mentors, Mentor clickedMentor, Mentee clickedMentee, List<Punishment> punishments, Punishment clickedPunishment) {
        super(String.format("&3&lPunishment %s", clickedPunishment.getPunishmentCode()), 2, true);

        this.player = player;
        this.mentors = mentors;
        this.clickedMentee = clickedMentee;
        this.clickedMentor = clickedMentor;
        this.clickedPunishment = clickedPunishment;
        this.punishments = punishments;

        this.setItem(1, 1, new GUIItem(Material.STAINED_CLAY, "&a&lApprove this punishment", 1, "&rShift-Click this to approve the punishment.", (short) 5));
        this.setItem(1, 7, new GUIItem(Material.STAINED_CLAY, "&c&lDeny this punishment", 1, "&rShift-Click this to deny the punishment;&rand remove it from the user's punishment history.", (short) 14));

        Rule rule = AuroraMCAPI.getRules().getRule(clickedPunishment.getRuleID());
        String reason;
        switch (clickedPunishment.getStatus()) {
            case 2:
                reason = WordUtils.wrap(String.format("%s - %s [Awaiting Approval]", rule.getRuleName(), clickedPunishment.getExtraNotes()), 43, ";&b", false);
                break;
            case 6:
            case 3:
                reason = WordUtils.wrap(String.format("%s - %s [SM Approved]&r", rule.getRuleName(), clickedPunishment.getExtraNotes()), 43, ";&b", false);
                break;
            default:
                reason = WordUtils.wrap(String.format("&b%s - %s", rule.getRuleName(), clickedPunishment.getExtraNotes()), 43, ";&b", false);
        }

        PunishmentLength length;
        if (clickedPunishment.getExpire() == -1) {
            length = new PunishmentLength(-1);
        } else {
            length = new PunishmentLength((clickedPunishment.getExpire() - clickedPunishment.getIssued()) / 3600000d);
        }

        String[] weights = new String[]{"Light", "Medium", "Heavy", "Severe", "Extreme"};
        String lore = String.format("&rPunished: **%s**;&rPunishment Code: **%s**;&rReason:;**%s**;&rWeight: **%s**;&rIssued at: **%s**;&rLength: **%s**" + ((clickedMentee == null) ? ";&rIssued by: **%s**" : ""), clickedPunishment.getPunishedName(), clickedPunishment.getPunishmentCode(), reason, weights[rule.getWeight() - 1], (new Date(clickedPunishment.getIssued())).toString(), length.getFormatted(), clickedPunishment.getPunisherName());

        switch (rule.getType()) {
            case 1:
                this.setItem(1, 4, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Offence", 1, lore));
                break;
            case 2:
                GUIItem guiItem = new GUIItem(Material.IRON_SWORD, "&3&lGame Offence", 1, lore);
                ItemStack itemStack = guiItem.getItem();
                ItemMeta meta = itemStack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(meta);
                this.setItem(1, 4, new GUIItem(itemStack));
                break;
            case 3:
                this.setItem(1, 4, new GUIItem(Material.SIGN, "&3&lMisc Offence", 1, lore));
                break;
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() != Material.STAINED_CLAY) {
            if (clickedPunishment.getEvidence() != null) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("Evidence: **%s**", clickedPunishment.getEvidence())));
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","There is currently no evidence attached to this punishment."));
            }
            player.getPlayer().closeInventory();
            return;
        }

        if (clickType == ClickType.SHIFT_LEFT) {
            if (item.getDurability() == 5) {
                //It was accepted.
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","Punishment approved."));
                List<AuroraMCPlayer> players = AuroraMCAPI.getPlayers().stream().filter(auroraMCPlayer -> auroraMCPlayer.hasPermission("staffmanagement")).filter(auroraMCPlayer -> auroraMCPlayer.getPreferences().isApprovalProcessedNotificationsEnabled()).collect(Collectors.toList());
                for (AuroraMCPlayer pl : players) {
                    if (!pl.equals(player)) {
                        pl.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has approved punishment **%s** for user **%s** issued by **%s**.", player.getName(), clickedPunishment.getPunishmentCode(), clickedPunishment.getPunishedName(), clickedPunishment.getPunisherName())));
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AuroraMCAPI.getDbManager().approvePunishment(clickedPunishment);
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                if (AuroraMCAPI.getRules().getRule(clickedPunishment.getRuleID()).getType() == 1) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("SMApproval");
                    out.writeUTF(clickedPunishment.getPunishmentCode());
                    out.writeUTF(clickedPunishment.getPunishedName());
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                }
            } else {
                //It was denied.
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","Punishment denied."));
                List<AuroraMCPlayer> players = AuroraMCAPI.getPlayers().stream().filter(auroraMCPlayer -> auroraMCPlayer.hasPermission("staffmanagement")).filter(auroraMCPlayer -> auroraMCPlayer.getPreferences().isApprovalProcessedNotificationsEnabled()).collect(Collectors.toList());
                for (AuroraMCPlayer pl : players) {
                    if (!pl.equals(player)) {
                        pl.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has denied punishment **%s** for user **%s** issued by **%s**.", player.getName(), clickedPunishment.getPunishmentCode(), clickedPunishment.getPunishedName(), clickedPunishment.getPunisherName())));
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AuroraMCAPI.getDbManager().denyPunishment(clickedPunishment);
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                if (AuroraMCAPI.getRules().getRule(clickedPunishment.getRuleID()).getType() == 1) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("SMDenial");
                    out.writeUTF(clickedPunishment.getPunishmentCode());
                    out.writeUTF(clickedPunishment.getPunishedName());
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                }
            }

            punishments.remove(clickedPunishment);
            PunishmentList gui = new PunishmentList(player, mentors, clickedMentor, clickedMentee, punishments);
            gui.open(player);
            AuroraMCAPI.openGUI(player, gui);
        }
    }
}
