package net.auroramc.core.gui.punish;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.*;
import net.auroramc.core.api.utils.UUIDUtil;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.punishments.*;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RuleListing extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final int type;
    private final String extraDetails;
    private final PunishmentHistory history;

    public RuleListing(AuroraMCPlayer player, String name, int id, int type, String extraDetails, List<Punishment> punishmentHistory) {
        super(String.format("&3&lPunish %s - %s", name, Type.TYPES[type - 1]), 5, true);

        this.player = player;
        this.name = name;
        this.id = id;
        this.type = type;
        this.extraDetails = extraDetails;
        this.history = new PunishmentHistory(id);

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lPunish %s", name), 1, "&rPlease choose a rule.", (short)3, false, name));

        for (Punishment punishment : punishmentHistory) {
            history.registerPunishment(punishment);
        }


        int row = 2;
        int column = 0;
        for (int i = 1;i <= 5;i++) {
            //For each weight
            row = 2;
            column = (i-1)*2;
            this.setItem(1, (i-1)*2, new GUIItem(Material.WOOL, Weight.WEIGHTS[i-1], 1, "&rPunishment Length: &b" + history.getType(type).generateLength(i).getFormatted(), Weight.WEIGHT_ICON_DATA[i-1]));
            for (Rule rule : AuroraMCAPI.getRules().getType(type).getWeight(i).getRules()) {
                if (rule.isActive()) {
                    this.setItem(row, column, new GUIItem(Material.BOOK, String.format("&3&l%s", rule.getRuleName()), 1, String.format("&r%s;;&rID: &b%s", WordUtils.wrap(rule.getRuleDescription(), 40, ";&r", false), rule.getRuleID())));
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
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        List<String> lore = item.getItemMeta().getLore();
        String sid = lore.get(lore.size() - 1);
        sid = ChatColor.stripColor(sid.split(" ")[1]);
        int ruleId = Integer.parseInt(sid);

        Rule rule = AuroraMCAPI.getRules().getRule(ruleId);

        player.getPlayer().closeInventory();


        //Run this bit on a seperate thread as it requires several pulls and pushes to the databases.
        new BukkitRunnable(){
            @SuppressWarnings("UnstableApiUsage")
            @Override
            public void run() {
                //Generate a random 8 digit punishment code.
                String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

                while (AuroraMCAPI.getDbManager().banCodeExists(code)) {
                    code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
                }

                //A unique code has now been generated, continue with the punishment.
                long issued = System.currentTimeMillis();

                //Process if it requires a warning or not.
                if (rule.requiresWarning()) {
                    //Anything that has a warning issued does not need checking by SM.
                    if (history.getType(type).getWeight(rule.getWeight()).issueWarning(rule)) {
                        //Issue a warning then return.
                        AuroraMCAPI.getDbManager().issuePunishment(code, id, rule.getRuleID(), extraDetails, player.getId(), issued, -1, 7, null);

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Message");
                        out.writeUTF(name);
                        out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been issued a warning.\n" +
                                "Reason: **%s - %s**\n" +
                                "Punishment Code: **%s**", rule.getRuleName(), extraDetails, code)));

                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have issued a warning to **%s**.\n" +
                                "Reason: **%s - %s**\n" +
                                "Punishment Code: **%s**", name, rule.getRuleName(), extraDetails, code)));

                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (!player1.equals(player.getPlayer())) {
                                if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                                    player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has issued a warning to **%s** for **%s - %s**.", player.getPlayer().getName(), name, rule.getRuleName(), extraDetails)));
                                }
                            }
                        }
                        return;
                    }

                    //Do not issue warning but do not send for SM approval.
                    //Generate expiry time;
                    issueNormalPunishment(code, issued, rule);
                    return;
                }


                //Do not issue a warning, issue a full punishment. If this is a trial moderator, SM approval needs to be given.

                if (player.hasPermission("approval.bypass")) {
                    //This is a mod, admin or owner. Do not send to SM.
                    issueNormalPunishment(code, issued, rule);
                    return;
                } else {
                    //This is a Trial Mod, send to SM for approval

                    //Generate a length
                    PunishmentLength length = history.getType(type).generateLength(rule.getWeight());
                    long expiry = Math.round(length.getMsValue());
                    if (history.getType(type).generateLength(rule.getWeight()).getMsValue() == -1) {
                        expiry = -1;
                    } else {
                        expiry += System.currentTimeMillis();
                    }

                    if (type == 1) {
                        AuroraMCAPI.getDbManager().issuePunishment(code, id, rule.getRuleID(), extraDetails, player.getId(), issued, expiry, 2, null);
                        //Mute
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Message");
                        out.writeUTF(name);
                        out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been muted for **%s**.\n" +
                                "Reason: **%s - %s [Awaiting Approval]**\n" +
                                "Punishment Code: **%s**\n\n" +
                                "Your punishment has been applied by a Junior Moderator, and is severe enough to need approval from a Staff Management member to ensure that the punishment applied was correct. When it is approved, the full punishment length will automatically apply to you. If this punishment is denied for being false, **it will automatically be removed**, and the punishment will automatically be removed from your Punishment History.", length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have muted **%s** for **%s**.\n" +
                                "Reason: **%s - %s [Awaiting Approval]**\n" +
                                "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (!player1.equals(player.getPlayer())) {
                                if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                                    player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has muted **%s** for **%s**. Reason: **%s - %s [Awaiting Approval]**.", player.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                                }
                            }
                        }

                        //Send mute to the bungee to enforce it.
                        out = ByteStreams.newDataOutput();
                        out.writeUTF("Mute");
                        out.writeUTF(code);
                        out.writeUTF(name);
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                        return;
                    } else {
                        //Ban
                        AuroraMCAPI.getDbManager().issuePunishment(code, id, rule.getRuleID(), extraDetails, player.getId(), issued, expiry, 2, UUIDUtil.getUUID(name).toString());
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("KickPlayer");
                        out.writeUTF(name);
                        out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                                "\n" +
                                "&rReason: **%s - %s [Awaiting Approval]**\n" +
                                "&rExpires:  **%s**\n" +
                                "\n" +
                                "&rPunishment Code: **%s**\n" +
                                "\n" +
                                "&rYour punishment has been applied by a Junior Moderator, and is severe enough to need approval\n" +
                                "&rfrom a Staff Management member to ensure that the punishment applied was correct. When it is\n" +
                                "&rapproved, the full punishment length will automatically apply to you. If this punishment is\n" +
                                "&rdenied for being false, **it will automatically be removed**, and the punishment will automatically\n" +
                                "&rbe removed from your Punishment History.\n" +
                                "\n" +
                                "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.network.", rule.getRuleName(), extraDetails, length.getFormatted(), code)));

                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have banned **%s** for **%s**.\n" +
                                "Reason: **%s - %s [Awaiting Approval]**\n" +
                                "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (!player1.equals(player.getPlayer())) {
                                if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                                    player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has banned **%s** for **%s**. Reason: **%s - %s [Awaiting Approval]**.", player.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                                }
                            }
                        }

                        //Send mute to the bungee to enforce it.
                        out = ByteStreams.newDataOutput();
                        out.writeUTF("ApprovalBan");
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        return;
                    }
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    private void issueNormalPunishment(String code, long issued, Rule rule) {
        PunishmentLength length = history.getType(type).generateLength(rule.getWeight());
        long expiry = Math.round(length.getMsValue());
        if (history.getType(type).generateLength(rule.getWeight()).getMsValue() == -1) {
            expiry = -1;
        } else {
            expiry += System.currentTimeMillis();
        }


        if (type == 1) {
            AuroraMCAPI.getDbManager().issuePunishment(code, id, rule.getRuleID(), extraDetails, player.getId(), issued, expiry, 1, null);
            //Mute
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Message");
            out.writeUTF(name);
            out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been muted for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have muted **%s** for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (!player1.equals(player.getPlayer())) {
                    if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                        player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has muted **%s** for **%s**. Reason: **%s - %s**.", player.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                    }
                }
            }

            //Send mute to the bungee to enforce it.
            out = ByteStreams.newDataOutput();
            out.writeUTF("Mute");
            out.writeUTF(code);
            out.writeUTF(name);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
            return;
        } else {
            //Ban
            AuroraMCAPI.getDbManager().issuePunishment(code, id, rule.getRuleID(), extraDetails, player.getId(), issued, expiry, 1, UUIDUtil.getUUID(name).toString());
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("KickPlayer");
            out.writeUTF(name);
            out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                    "\n" +
                    "&rReason: **%s - %s**\n" +
                    "&rExpires:  **%s**\n" +
                    "\n" +
                    "&rPunishment Code: **%s**\n" +
                    "\n" +
                    "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.network.", rule.getRuleName(), extraDetails, length.getFormatted(), code)));

            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have banned **%s** for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (!player1.equals(player.getPlayer())) {
                    if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                        player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has banned **%s** for **%s**. Reason: **%s - %s**.", player.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                    }
                }
            }
            return;
        }
    }
}
