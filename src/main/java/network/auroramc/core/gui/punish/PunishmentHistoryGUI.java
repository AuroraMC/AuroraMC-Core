package network.auroramc.core.gui.punish;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.PunishmentLength;
import network.auroramc.core.api.punishments.Rule;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishmentHistoryGUI extends GUI {

    private List<Punishment> punishments;
    private AuroraMCPlayer player;
    private int currentPage;
    private String name;
    private String reason;
    private UUID uuid;

    public PunishmentHistoryGUI(AuroraMCPlayer player, int id, String name, String removalReason1, UUID uuid) {
        super(String.format("&3&l%s's Punishment History", name), 5, true);

        this.currentPage = 1;
        this.name = name;
        this.player = player;
        this.reason = removalReason1;
        this.uuid = uuid;

        int column = 1;
        int row = 1;
        this.punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(id).stream().filter(punishment -> punishment.getStatus() != 4).collect(Collectors.toList());

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Punishment History", name), 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Punishment History", name), 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Punishment History", name), 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Punishment History", name), 1, "", (short) 7));
        }
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Punishment History", name), 1, "&rPunishment History", (short) 3, false, name));
        if (punishments.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }
        //This is a staff member viewing someones punishment history.
        for (Punishment punishment : punishments) {
            if (punishment.getStatus() != 4) {
                //If not an SM denied punishment.
                Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                String reason;
                switch (punishment.getStatus()) {
                    case 2:
                        reason = WordUtils.wrap(String.format("%s - %s [Awaiting Approval]", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                        break;
                    case 6:
                    case 3:
                        reason = WordUtils.wrap(String.format("%s - %s [SM Approved]&r", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                        break;
                    default:
                        reason = WordUtils.wrap(String.format("&b%s - %s", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                }

                PunishmentLength length;
                if (punishment.getExpire() == -1) {
                    length = new PunishmentLength(-1);
                } else {
                    length = new PunishmentLength((punishment.getExpire() - punishment.getIssued()) / 3600000d);
                }

                String[] weights = new String[]{"Light", "Medium", "Heavy", "Severe", "Extreme"};
                String lore = String.format("&rPunishment Code: **%s**;&rReason:;**%s**;&rWeight: **%s**;&rIssued at: **%s**;&rLength: **%s**" + ((player.hasPermission("moderation"))?";&rIssued by: **%s**":""), punishment.getPunishmentCode(), reason, weights[rule.getWeight() - 1], (new Date(punishment.getIssued())).toString(), length.getFormatted(), punishment.getPunisherName());

                if (punishment.getRemover() != null) {
                    String removalReason = WordUtils.wrap(punishment.getRemovalReason(), 40, ";&b", false);
                    lore += String.format(";;&rRemoval Reason:;&b%s&r;&rRemoval Timestamp: **%s**" + ((player.hasPermission("moderation"))?";&rRemoved by: **%s**":""), removalReason, (new Date(punishment.getRemovalTimestamp())).toString(), punishment.getRemover());                }

                if (punishment.getStatus() == 7) {
                    //This is a warning.
                    this.setItem(row, column, new GUIItem(Material.PAPER, "&3&lWarning", 1, lore));
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                } else {
                    switch (rule.getType()) {
                        case 1:
                            this.setItem(row, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Offence", 1, lore, (short) 0, ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))));
                            break;
                        case 2:
                            this.setItem(row, column, new GUIItem(Material.IRON_SWORD, "&3&lGame Offence", 1, lore, (short) 0, ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))));
                            break;
                        case 3:
                            this.setItem(row, column, new GUIItem(Material.SIGN, "&3&lMisc Offence", 1, lore, (short) 0, ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))));
                            break;
                    }

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
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            //Go to next/previous page.
            if (column == 1) {
                //Prev page.
                currentPage--;
                this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                if (currentPage == 1) {
                    this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Punishment History", this.name), 1, "", (short) 7));
                }
            } else {
                //next page.
                currentPage++;
                if (punishments.size() < ((currentPage) * 28)) {
                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Punishment History", this.name), 1, "", (short) 7));
                }

                this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
            }
            column = 1;
            row = 1;
            for (int i = 0;i < 28;i++) {
                //show the prev 28 items.

                int pi = (((currentPage - 1) * 28) + i);
                if (punishments.size() <= pi) {
                    this.updateItem(row, column, null);
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                    continue;
                }

                Punishment punishment = punishments.get(pi);

                if (punishment.getStatus() != 4) {
                    //If not an SM denied punishment.
                    Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                    String reason;
                    switch (punishment.getStatus()) {
                        case 2:
                            reason = WordUtils.wrap(String.format("%s - %s [Awaiting Approval]", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                            break;
                        case 6:
                        case 3:
                            reason = WordUtils.wrap(String.format("%s - %s [SM Approved]&r", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                            break;
                        default:
                            reason = WordUtils.wrap(String.format("&b%s - %s", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                    }

                    PunishmentLength length;
                    if (punishment.getExpire() == -1) {
                        length = new PunishmentLength(-1);
                    } else {
                        length = new PunishmentLength((punishment.getExpire() - punishment.getIssued()) / 3600000d);
                    }

                    String[] weights = new String[]{"Light", "Medium", "Heavy", "Severe", "Extreme"};
                    String lore = String.format("&rPunishment Code: **%s**;&rReason:;**%s**;&rWeight: **%s**;&rIssued at: **%s**;&rLength: **%s**" + ((player.hasPermission("moderation"))?";&rIssued by: **%s**":""), punishment.getPunishmentCode(), reason, weights[rule.getWeight() - 1], (new Date(punishment.getIssued())).toString(), length.getFormatted(), punishment.getPunisherName());

                    if (punishment.getRemover() != null) {
                        String removalReason = WordUtils.wrap(punishment.getRemovalReason(), 40, ";&b", false);
                        lore += String.format(";;&rRemoval Reason:;&b%s&r;&rRemoval Timestamp: **%s**" + ((player.hasPermission("moderation"))?";&rRemoved by: **%s**":""), removalReason, (new Date(punishment.getRemovalTimestamp())).toString(), punishment.getRemover());
                    }

                    if (punishment.getStatus() == 7) {
                        //This is a warning.
                        this.updateItem(row, column, new GUIItem(Material.PAPER, "&3&lWarning", 1, lore));
                        column++;
                        if (column == 8) {
                            row++;
                            column = 1;
                            if (row == 5) {
                                break;
                            }
                        }
                    } else {
                        switch (rule.getType()) {
                            case 1:
                                this.updateItem(row, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Offence", 1, lore, (short) 0, ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))));
                                break;
                            case 2:
                                this.updateItem(row, column, new GUIItem(Material.IRON_SWORD, "&3&lGame Offence", 1, lore, (short) 0, ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))));
                                break;
                            case 3:
                                this.updateItem(row, column, new GUIItem(Material.SIGN, "&3&lMisc Offence", 1, lore, (short) 0, ((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))));
                                break;
                        }

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

            }
            return;
        } else if (item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        //Get the clicked punishment.
        Punishment punishment = punishments.get(((currentPage - 1) * 28) + ((row - 1) * 7) + (column - 1));

        switch (clickType) {
            case LEFT:
            case SHIFT_LEFT:
                //Show the evidence in chat.
                if (player.hasPermission("moderation")) {
                    player.getPlayer().closeInventory();
                    if (punishment.getEvidence() != null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("Evidence: **%s**", punishment.getEvidence())));
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","There is currently no evidence attached to this punishment."));
                    }
                }
                break;
            case SHIFT_RIGHT:
                player.getPlayer().closeInventory();
                //REMOVE THE PUNISHMENT
                if (player.hasPermission("moderation")) {
                    player.getPlayer().closeInventory();
                    if (((punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) && (punishment.getExpire() > System.currentTimeMillis() || punishment.getExpire() == -1))) {
                        if (this.reason != null) {
                            long timestamp = System.currentTimeMillis();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    AuroraMCAPI.getDbManager().removePunishment(player, timestamp, reason, punishment, uuid, punishments);
                                }
                            }.runTaskAsynchronously(AuroraMCAPI.getCore());
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","Punishment removed."));

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Unmute");
                            out.writeUTF(punishment.getPunishmentCode());
                            out.writeUTF(name);
                            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","You cannot remove a punishment if you did not specify a reason."));
                        }
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","You cannot remove a punishment that has already expired. If this is false, please replace the evidence with **/evidence False punishment**."));
                    }

                }
                break;
        }
    }
}