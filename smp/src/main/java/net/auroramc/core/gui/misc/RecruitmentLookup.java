/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.misc;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentHistory;
import net.auroramc.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.TimeLength;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Date;

public class RecruitmentLookup extends GUI {


    public RecruitmentLookup(String name, PunishmentHistory history, PlayerStatistics statistics, PlayerProfile ipprofile) {
        super("&3&lRecruitment Lookup", 2, true);

        border(String.format("&3&l%s's Lookup Information", name), "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 0, false, name));

        this.setItem(1, 2, new GUIItem(Material.CLOCK, "&3&lIn-Game Time", 1, String.format(";&r&fRequirement met? %s;&r&fTotal In-game Time: **%s**", ((statistics.getLobbyTimeMs() + statistics.getGameTimeMs() >= 36000000)?"&a✔":"&c✘"), (new TimeLength((statistics.getGameTimeMs() + statistics.getLobbyTimeMs())/3600000d, false)))));
        long light = -1, medium = -1, heavy = -1, severe = -1, extreme = -1;
        boolean lightActive = false, mediumActive = false, heavyActive = false, severeActive = false, extremeActive = false;
        for (Punishment punishment : history.getPunishments()) {
            switch (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getWeight()) {
                case 1:
                    if (light == -1 && !lightActive) {
                        if (punishment.getRemover() != null) {
                            if ((punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                                light = punishment.getRemovalTimestamp();
                            }
                        } else {
                            if (punishment.getStatus() != 4) {
                                if (punishment.getStatus() < 4) {
                                    lightActive = true;
                                } else {
                                    if (punishment.getStatus() != 7) {
                                        light = punishment.getExpire();
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    if (medium == -1 && !mediumActive) {
                        if (punishment.getRemover() != null) {
                            if ((punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                                medium = punishment.getRemovalTimestamp();
                            }
                        } else {
                            if (punishment.getStatus() != 4) {
                                if (punishment.getStatus() < 4) {
                                    mediumActive = true;
                                } else {
                                    if (punishment.getStatus() != 7) {
                                        medium = punishment.getExpire();
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    if (heavy == -1 && !heavyActive) {
                        if (punishment.getRemover() != null) {
                            if ((punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                                heavy = punishment.getRemovalTimestamp();
                            }
                        } else {
                            if (punishment.getStatus() != 4) {
                                if (punishment.getStatus() < 4) {
                                    heavyActive = true;
                                } else {
                                    if (punishment.getStatus() != 7) {
                                        heavy = punishment.getExpire();
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if (severe == -1 && !severeActive) {
                        if (punishment.getRemover() != null) {
                            if ((punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                                severe = punishment.getRemovalTimestamp();
                            }
                        } else {
                            if (punishment.getStatus() != 4) {
                                if (punishment.getStatus() < 4) {
                                    severeActive = true;
                                } else {
                                    if (punishment.getStatus() != 7) {
                                        severe = punishment.getExpire();
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    if (extreme == -1 && !extremeActive) {
                        if (punishment.getRemover() != null) {
                            if ((punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                                extreme = punishment.getRemovalTimestamp();
                            }
                        } else {
                            if (punishment.getStatus() != 4) {
                                if (punishment.getStatus() < 4) {
                                    extremeActive = true;
                                }
                            }
                        }
                    }
                    break;
            }
        }
        boolean requirementMetPunishments = (((!lightActive) && (light == -1 || System.currentTimeMillis() > light + 2592000000L)) && ((!mediumActive) && (medium == -1 || System.currentTimeMillis() > medium + 2592000000L)) && ((!heavyActive) && (heavy == -1 || System.currentTimeMillis() > heavy + 7776000000L)) && ((!severeActive) && (light == -1 || System.currentTimeMillis() > light + 15552000000L)) && ((!extremeActive) && (extreme == -1 || System.currentTimeMillis() > extreme + 2592000000L)));
        GUIItem guiItem = new GUIItem(Material.DIAMOND_SWORD, "&3&lRecent Punishment", 1, String.format(";&r&fRequirement met? %s;&r&fLast Punishments:;&r&fLight: **%s**;&r&fMedium: **%s**;&r&fHeavy: **%s**;&r&fSevere: **%s**;&r&fExtreme: **%s**", ((requirementMetPunishments)?"&a✔":"&c✘"), ((lightActive)?"&lACTIVE":((light == -1)?"Never Received":new Date(light).toString())), ((mediumActive)?"&lACTIVE":((medium == -1)?"Never Received":new Date(medium).toString())), ((heavyActive)?"&lACTIVE":((heavy == -1)?"Never Received":new Date(heavy).toString())), ((severeActive)?"&lACTIVE":((severe == -1)?"Never Received":new Date(severe).toString())), ((extremeActive)?"&lACTIVE":((extreme == -1)?"Never Received":new Date(extreme).toString()))));
        ItemStack itemStack = guiItem.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        this.setItem(1, 4, new GUIItem(itemStack));
        this.setItem(1, 6, new GUIItem(Material.PAPER, "&3&lIP Profile", 1, String.format(";&r&fThis player has **%s** known alts.;&r&fOf those, **%s** are banned and **%s** are muted.", ipprofile.getSharedAccounts(), ipprofile.getBans(), ipprofile.getMutes())));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

    }
}
