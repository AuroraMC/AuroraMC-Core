/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.stats;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStatsAdminXP extends ProxyCommand {
    public CommandStatsAdminXP() {
        super("xp", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 3) {
            if (args.get(0).equalsIgnoreCase("add")) {
                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(1));

                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user does not exist."));
                        return;
                    }

                    int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                    if (amcId < 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot manage statistics of someone who has not joined the network."));
                        return;
                    }

                    long amount;

                    try {
                        amount = Long.parseLong(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    if (amount < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid, amcId);
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        ProxyAPI.getPlayer(target).getStats().addXp(amount, true);
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_xp", AuroraMCAPI.getInfo().getName(), uuid + "\n" + amount);
                            CommunicationUtils.sendMessage(message);
                        } else {
                            stats.addXp(amount, false);
                            AuroraMCAPI.getDbManager().xpAdd(uuid, stats.getLevel(), stats.getXpIntoLevel(), stats.getTotalXpEarned());
                        }
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("You have added **%s** XP to **%s**.", amount, args.get(1))));

                });
            } else if (args.get(0).equalsIgnoreCase("remove")) {
                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(1));

                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user does not exist."));
                        return;
                    }

                    int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                    if (amcId < 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot manage statistics of someone who has not joined the network."));
                        return;
                    }

                    long amount;

                    try {
                        amount = Long.parseLong(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    if (amount < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid, amcId);
                    if (stats.getTotalXpEarned() - amount < 0) {
                        amount = (byte) (stats.getTotalXpEarned());
                    }

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        ProxyAPI.getPlayer(target).getStats().removeXp(amount, true);
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "remove_xp", AuroraMCAPI.getInfo().getName(), uuid + "\n" + amount);
                            CommunicationUtils.sendMessage(message);
                        } else {
                            stats.removeXp(amount, false);
                            AuroraMCAPI.getDbManager().xpAdd(uuid, stats.getLevel(), stats.getXpIntoLevel(), stats.getTotalXpEarned());
                        }
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("You have removed **%s** XP from **%s**.", amount, args.get(1))));
                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                        "**/statsadmin xp add <player> <amount>** - Add XP to a users profile.\n" +
                        "**/statsadmin xp remove <player> <amount>** - Remove XP from a users profile."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                    "**/statsadmin xp add <player> <amount>** - Add XP to a users profile.\n" +
                    "**/statsadmin xp remove <player> <amount>** - Remove XP from a users profile."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            if ("add".startsWith(lastToken)) completions.add("add");
            if ("remove".startsWith(lastToken)) completions.add("remove");
        }
        return completions;
    }
}
