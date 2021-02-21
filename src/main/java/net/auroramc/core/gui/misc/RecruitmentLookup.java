package net.auroramc.core.gui.misc;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.lookup.IPLookup;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentHistory;
import net.auroramc.core.api.punishments.PunishmentLength;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class RecruitmentLookup extends GUI {


    public RecruitmentLookup(String name, PunishmentHistory history, PlayerStatistics statistics, IPLookup ipprofile) {
        super("&3&lRecruitment Lookup", 2, true);

        border(String.format("&3&l%s's Lookup Information", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 3, false, name));

        this.setItem(1, 2, new GUIItem(Material.WATCH, "&3&lIn-Game Time", 1, String.format(";&rRequirement met? %s;&rTotal In-game Time: **%s**", ((statistics.getLobbyTimeMs() + statistics.getGameTimeMs() >= 36000000)?"&a✔":"&c✘"), (new PunishmentLength(statistics.getLobbyTimeMs() + statistics.getGameTimeMs() / 3600000d)).toString())));
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
        this.setItem(1, 4, new GUIItem(Material.DIAMOND_SWORD, "&3&lRecent Punishment", 1, String.format(";&rRequirement met? %s;&rLast Punishments:;&rLight: **%s**;&rMedium: **%s**;&rHeavy: **%s**;&rSevere: **%s**;&rExtreme: **%s**", ((requirementMetPunishments)?"&a✔":"&c✘"), ((lightActive)?"&lACTIVE":((light == -1)?"Never Received":new Date(light).toString())), ((mediumActive)?"&lACTIVE":((medium == -1)?"Never Received":new Date(medium).toString())), ((heavyActive)?"&lACTIVE":((heavy == -1)?"Never Received":new Date(heavy).toString())), ((severeActive)?"&lACTIVE":((severe == -1)?"Never Received":new Date(severe).toString())), ((extremeActive)?"&lACTIVE":((extreme == -1)?"Never Received":new Date(extreme).toString())))));
        this.setItem(1, 6, new GUIItem(Material.PAPER, "&3&lIP Profile", 1, String.format(";&rThis player has **%s** known alts.;&rOf those, **%s** have an active punishment.", ipprofile.getNames().size() - 1, ipprofile.getNames().stream().filter(lookupUser -> lookupUser.isBanned() || lookupUser.isMuted()).count())));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

    }
}
