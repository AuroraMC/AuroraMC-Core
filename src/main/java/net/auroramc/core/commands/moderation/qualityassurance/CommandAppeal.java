/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation.qualityassurance;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.utils.DiscordWebhook;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CommandAppeal extends Command {

    public CommandAppeal() {
        super("appeal", Collections.singletonList("acceptappeal"), Arrays.asList(Permission.DEBUG_INFO, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            if(args.get(0).matches("[A-Z0-9]{8}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String code = args.remove(0);
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                        if (punishment != null) {
                            if(punishment.getStatus() == 1 || punishment.getStatus() == 3) {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(punishment.getPunished());
                                List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(punishment.getPunished());
                                if (args.get(0).equalsIgnoreCase("Reprieve")) {
                                    if (!AuroraMCAPI.isTestServer()) {
                                        AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "Reprieve", punishment, uuid, punishments);
                                    }
                                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928788070864654366/cpRwZiETmkD6XC-Ik1VC3FDuOUwbJwswzX-m_U0V9bVrgvRPS9aSiwGUNIoxj3qg4lXU");
                                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Appeal Log").setDescription(String.format("**%s** accepted an appeal for punishment **%s** for reason: **Reprieve**.", player.getName(), code)).setColor(new Color(85, 255, 85)));
                                    try {
                                        discordWebhook.execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("Reprieved Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                    }
                                } else if (args.get(0).equalsIgnoreCase("False")) {
                                    if (!AuroraMCAPI.isTestServer()) {
                                        AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "False", punishment, uuid, punishments);
                                    }
                                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928788070864654366/cpRwZiETmkD6XC-Ik1VC3FDuOUwbJwswzX-m_U0V9bVrgvRPS9aSiwGUNIoxj3qg4lXU");
                                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Appeal Log").setDescription(String.format("**%s** accepted an appeal for punishment **%s** for reason: **False**.", player.getName(), code)).setColor(new Color(255, 85, 85)));
                                    try {
                                        discordWebhook.execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("False Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                    }
                                } else if (args.get(0).equalsIgnoreCase("Compromised")) {
                                    if (!AuroraMCAPI.isTestServer()) {
                                        AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "Compromised Account Recovered", punishment, uuid, punishments);
                                    }
                                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928788070864654366/cpRwZiETmkD6XC-Ik1VC3FDuOUwbJwswzX-m_U0V9bVrgvRPS9aSiwGUNIoxj3qg4lXU");
                                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Appeal Log").setDescription(String.format("**%s** accepted an appeal for punishment **%s** for reason: **Compromised**.", player.getName(), code)).setColor(new Color(255, 170, 0)));
                                    try {
                                        discordWebhook.execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("Compromised Account Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                    }
                                } else {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False | Compromised]"));
                                }
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "This punishment currently cannot be removed using the appeals system."));
                            }
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False | Compromised]"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False | Compromised]"));
        }


    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
