/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation.qualityassurance;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.utils.DiscordWebhook;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.logging.Level;

public class CommandAppeal extends ServerCommand {

    public CommandAppeal() {
        super("appeal", Collections.singletonList("acceptappeal"), Arrays.asList(Permission.DEBUG_INFO, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
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
                                    DiscordWebhook discordWebhook = new DiscordWebhook("PLACEHOLDER");
                                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Appeal Log").setDescription(String.format("**%s** accepted an appeal for punishment **%s** for reason: **Reprieve**.", player.getName(), code)).setColor(new Color(85, 255, 85)));
                                    try {
                                        discordWebhook.execute();
                                    } catch (Exception e) {
                                        AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                                    }
                                    player.sendMessage(TextFormatter.pluginMessage("Appeal", String.format("Reprieved Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        player.sendPluginMessage(out.toByteArray());
                                    }
                                } else if (args.get(0).equalsIgnoreCase("False")) {
                                    if (!AuroraMCAPI.isTestServer()) {
                                        AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "False", punishment, uuid, punishments);
                                    }
                                    DiscordWebhook discordWebhook = new DiscordWebhook("PLACEHOLDER");
                                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Appeal Log").setDescription(String.format("**%s** accepted an appeal for punishment **%s** for reason: **False**.", player.getName(), code)).setColor(new Color(255, 85, 85)));
                                    try {
                                        discordWebhook.execute();
                                    } catch (Exception e) {
                                        AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                                    }
                                    player.sendMessage(TextFormatter.pluginMessage("Appeal", String.format("False Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        player.sendPluginMessage(out.toByteArray());
                                    }
                                } else if (args.get(0).equalsIgnoreCase("Compromised")) {
                                    if (!AuroraMCAPI.isTestServer()) {
                                        AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "Compromised Account Recovered", punishment, uuid, punishments);
                                    }
                                    DiscordWebhook discordWebhook = new DiscordWebhook("PLACEHOLDER");
                                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Appeal Log").setDescription(String.format("**%s** accepted an appeal for punishment **%s** for reason: **Compromised**.", player.getName(), code)).setColor(new Color(255, 170, 0)));
                                    try {
                                        discordWebhook.execute();
                                    } catch (Exception e) {
                                        AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                                    }
                                    player.sendMessage(TextFormatter.pluginMessage("Appeal", String.format("Compromised Account Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        player.sendPluginMessage(out.toByteArray());
                                    }
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False | Compromised]"));
                                }
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Appeal", "This punishment currently cannot be removed using the appeals system."));
                            }
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Appeal", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False | Compromised]"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False | Compromised]"));
        }


    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
