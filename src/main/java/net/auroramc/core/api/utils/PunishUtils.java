package net.auroramc.core.api.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentHistory;
import net.auroramc.core.api.punishments.PunishmentLength;
import net.auroramc.core.api.punishments.Rule;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class PunishUtils {

    public static void punishUser(int player, String name, AuroraMCPlayer issuer, Rule rule, String extraDetails) {
        //Run this bit on a seperate thread as it requires several pulls and pushes to the databases.
        new BukkitRunnable(){
            @SuppressWarnings("UnstableApiUsage")
            @Override
            public void run() {
                UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(player);
                //Generate a random 8 digit punishment code.
                PunishmentHistory history = new PunishmentHistory(player);
                List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(player);
                for (Punishment punishment : punishments) {
                    history.registerPunishment(punishment);
                }
                String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
                int type = rule.getType();

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
                        AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, -1, 7, null);

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Warning");
                        out.writeUTF(code);
                        issuer.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                        issuer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have issued a warning to **%s**.\n" +
                                "Reason: **%s - %s**\n" +
                                "Punishment Code: **%s**", name, rule.getRuleName(), extraDetails, code)));

                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (!player1.equals(issuer.getPlayer())) {
                                if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                                    player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has issued a warning to **%s** for **%s - %s**.", issuer.getPlayer().getName(), name, rule.getRuleName(), extraDetails)));
                                }
                            }
                        }
                        return;
                    }

                    //Do not issue warning but do not send for SM approval.
                    //Generate expiry time;
                    issueNormalPunishment(code, issued, player, name, issuer, rule, extraDetails, history);
                    return;
                }


                //Do not issue a warning, issue a full punishment. If this is a trial moderator, SM approval needs to be given.

                if (issuer.hasPermission("approval.bypass")) {
                    //This is a mod, admin or owner. Do not send to SM.
                    issueNormalPunishment(code, issued, player, name, issuer, rule, extraDetails, history);
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
                        AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 2, null);
                        //Mute
                        issuer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have muted **%s** for **%s**.\n" +
                                "Reason: **%s - %s [Awaiting Approval]**\n" +
                                "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (!player1.equals(issuer.getPlayer())) {
                                if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                                    player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has muted **%s** for **%s**. Reason: **%s - %s [Awaiting Approval]**.", issuer.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                                }
                            }
                        }

                        //Send mute to the bungee to enforce it.
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Mute");
                        out.writeUTF(code);
                        issuer.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                    } else {
                        //Ban
                        AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 2, uuid.toString());
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Ban");
                        out.writeUTF(code);
                        issuer.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                        issuer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have banned **%s** for **%s**.\n" +
                                "Reason: **%s - %s [Awaiting Approval]**\n" +
                                "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (!player1.equals(issuer.getPlayer())) {
                                if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                                    player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has banned **%s** for **%s**. Reason: **%s - %s [Awaiting Approval]**.", issuer.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                                }
                            }
                        }

                        //Send mute to the bungee to enforce it.
                        out = ByteStreams.newDataOutput();
                        out.writeUTF("ApprovalBan");
                        issuer.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    }
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    private static void issueNormalPunishment(String code, long issued, int player, String name, AuroraMCPlayer issuer, Rule rule, String extraDetails, PunishmentHistory history) {
        PunishmentLength length = history.getType(rule.getType()).generateLength(rule.getWeight());
        long expiry = Math.round(length.getMsValue());
        if (history.getType(rule.getType()).generateLength(rule.getWeight()).getMsValue() == -1) {
            expiry = -1;
        } else {
            expiry += System.currentTimeMillis();
        }


        if (rule.getType() == 1) {
            AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 1, null);
            //Mute
            issuer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have muted **%s** for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (!player1.equals(issuer.getPlayer())) {
                    if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                        player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has muted **%s** for **%s**. Reason: **%s - %s**.", issuer.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                    }
                }
            }

            //Send mute to the bungee to enforce it.
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Mute");
            out.writeUTF(code);
            issuer.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        } else {
            //Ban
            String uuid = AuroraMCAPI.getDbManager().getUUIDFromID(player).toString();
            AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 1, uuid);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Ban");
            out.writeUTF(code);
            issuer.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

            issuer.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("You have banned **%s** for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (!player1.equals(issuer.getPlayer())) {
                    if (AuroraMCAPI.getPlayer(player1).getRank().hasPermission("moderation")) {
                        player1.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish",String.format("**%s** has banned **%s** for **%s**. Reason: **%s - %s**.", issuer.getPlayer().getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)));
                    }
                }
            }
        }
    }

}
