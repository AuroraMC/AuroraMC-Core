/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentHistory;
import net.auroramc.api.punishments.Rule;
import org.apache.commons.lang3.RandomStringUtils;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class PunishUtils {

    public static void punishUser(int player, String name, AuroraMCPlayer issuer, Rule rule, String extraDetails) {
        //Run this bit on a seperate thread as it requires several pulls and pushes to the databases.
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
                if (!AuroraMCAPI.isTestServer()) {
                    AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, -1, 7, null);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Warning");
                    out.writeUTF(code);
                    issuer.sendPluginMessage(out.toByteArray());
                }

                issuer.sendMessage(TextFormatter.pluginMessage("Punish", String.format("You have issued a warning to **%s**.\n" +
                        "Reason: **%s - %s**\n" +
                        "Punishment Code: **%s**", name, rule.getRuleName(), extraDetails, code)));

                AuroraMCAPI.getAbstractedMethods().broadcastModerationMessage(TextFormatter.pluginMessage("Punish", String.format("**%s** has issued a warning to **%s** for **%s - %s**.", issuer.getName(), name, rule.getRuleName(), extraDetails)), issuer);

                DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929781450469957724/pMl2uPh1ovkzJ-uxvamzpwjJxtKXrcJcJNXUJWfoWThRaoI-vGkZpGWm54OWpqXrkwCk");

                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Punishment Log").setDescription(String.format("**%s** has warned **%s** for reason **%s - %s**.", issuer.getName(), name, rule.getRuleName(), extraDetails)).setColor(new Color(issuer.getRank().getR(), issuer.getRank().getG(), issuer.getRank().getB())));
                try {
                    discordWebhook.execute();
                } catch (Exception e) {
                    e.printStackTrace();
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
            TimeLength length = history.getType(type).generateLength(rule.getWeight());
            long expiry = Math.round(length.getMsValue());
            if (history.getType(type).generateLength(rule.getWeight()).getMsValue() == -1) {
                expiry = -1;
            } else {
                expiry += System.currentTimeMillis();
            }

            if (type == 1) {
                if (!AuroraMCAPI.isTestServer()) {
                    AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 2, null);
                    //Send mute to the bungee to enforce it.
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Mute");
                    out.writeUTF(code);
                    issuer.sendPluginMessage(out.toByteArray());
                }
                //Mute
                issuer.sendMessage(TextFormatter.pluginMessage("Punish", String.format("You have muted **%s** for **%s**.\n" +
                        "Reason: **%s - %s [Awaiting Approval]**\n" +
                        "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                AuroraMCAPI.getAbstractedMethods().broadcastModerationMessage(TextFormatter.pluginMessage("Punish", String.format("**%s** has muted **%s** for **%s**. Reason: **%s - %s [Awaiting Approval]**.", issuer.getName(), name, length.getFormatted(), rule.getRuleName(), extraDetails)), issuer);

                DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929781450469957724/pMl2uPh1ovkzJ-uxvamzpwjJxtKXrcJcJNXUJWfoWThRaoI-vGkZpGWm54OWpqXrkwCk");

                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Punishment Log").setDescription(String.format("**%s** has muted **%s** for reason **%s - %s [Awaiting Approval]**.", issuer.getName(), name, rule.getRuleName(), extraDetails)).setColor(new Color(issuer.getRank().getR(), issuer.getRank().getG(), issuer.getRank().getB())));
                try {
                    discordWebhook.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //Ban
                if (!AuroraMCAPI.isTestServer()) {
                    AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 2, uuid.toString());
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Ban");
                    out.writeUTF(code);
                    issuer.sendPluginMessage(out.toByteArray());
                }

                issuer.sendMessage(TextFormatter.pluginMessage("Punish", String.format("You have banned **%s** for **%s**.\n" +
                        "Reason: **%s - %s [Awaiting Approval]**\n" +
                        "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

                AuroraMCAPI.getAbstractedMethods().broadcastModerationMessage(TextFormatter.pluginMessage("Punish", String.format("**%s** has banned **%s** for **%s**. Reason: **%s - %s [Awaiting Approval]**.", issuer.getName(), name, length.getFormatted(), rule.getRuleName(), extraDetails)), issuer);

                //Send mute to the bungee to enforce it.
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("ApprovalBan");
                issuer.sendPluginMessage(out.toByteArray());

                DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929781450469957724/pMl2uPh1ovkzJ-uxvamzpwjJxtKXrcJcJNXUJWfoWThRaoI-vGkZpGWm54OWpqXrkwCk");

                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Punishment Log").setDescription(String.format("**%s** has banned **%s** for reason **%s - %s [Awaiting Approval]**.", issuer.getName(), name, rule.getRuleName(), extraDetails)).setColor(new Color(issuer.getRank().getR(), issuer.getRank().getG(), issuer.getRank().getB())));
                try {
                    discordWebhook.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void issueNormalPunishment(String code, long issued, int player, String name, AuroraMCPlayer issuer, Rule rule, String extraDetails, PunishmentHistory history) {
        TimeLength length = history.getType(rule.getType()).generateLength(rule.getWeight());
        long expiry = Math.round(length.getMsValue());
        if (history.getType(rule.getType()).generateLength(rule.getWeight()).getMsValue() == -1) {
            expiry = -1;
        } else {
            expiry += System.currentTimeMillis();
        }


        if (rule.getType() == 1) {
            if (!AuroraMCAPI.isTestServer()) {
                AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 1, null);
                //Send mute to the bungee to enforce it.
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Mute");
                out.writeUTF(code);
                issuer.sendPluginMessage(out.toByteArray());
            }
            //Mute
            issuer.sendMessage(TextFormatter.pluginMessage("Punish", String.format("You have muted **%s** for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            AuroraMCAPI.getAbstractedMethods().broadcastModerationMessage(TextFormatter.pluginMessage("Punish",String.format("**%s** has muted **%s** for **%s**. Reason: **%s - %s**.", issuer.getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)), issuer);
            DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929781450469957724/pMl2uPh1ovkzJ-uxvamzpwjJxtKXrcJcJNXUJWfoWThRaoI-vGkZpGWm54OWpqXrkwCk");

            discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Punishment Log").setDescription(String.format("**%s** has muted **%s** for reason **%s - %s**.", issuer.getName(), name, rule.getRuleName(), extraDetails)).setColor(new Color(issuer.getRank().getR(), issuer.getRank().getG(), issuer.getRank().getB())));
            try {
                discordWebhook.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Ban
            String uuid = AuroraMCAPI.getDbManager().getUUIDFromID(player).toString();
            if (!AuroraMCAPI.isTestServer()) {
                AuroraMCAPI.getDbManager().issuePunishment(code, player, rule.getRuleID(), extraDetails, issuer.getId(), issued, expiry, 1, uuid);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Ban");
                out.writeUTF(code);
                issuer.sendPluginMessage(out.toByteArray());
            }

            issuer.sendMessage(TextFormatter.pluginMessage("Punish", String.format("You have banned **%s** for **%s**.\n" +
                    "Reason: **%s - %s**\n" +
                    "Punishment Code: **%s**", name, length.getFormatted(), rule.getRuleName(), extraDetails, code)));

            AuroraMCAPI.getAbstractedMethods().broadcastModerationMessage(TextFormatter.pluginMessage("Punish",String.format("**%s** has banned **%s** for **%s**. Reason: **%s - %s**.", issuer.getName(), name, length.getFormatted(),  rule.getRuleName(), extraDetails)), issuer);
            DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929781450469957724/pMl2uPh1ovkzJ-uxvamzpwjJxtKXrcJcJNXUJWfoWThRaoI-vGkZpGWm54OWpqXrkwCk");

            discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Punishment Log").setDescription(String.format("**%s** has banned **%s** for reason **%s - %s**.", issuer.getName(), name, rule.getRuleName(), extraDetails)).setColor(new Color(issuer.getRank().getR(), issuer.getRank().getG(), issuer.getRank().getB())));
            try {
                discordWebhook.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
