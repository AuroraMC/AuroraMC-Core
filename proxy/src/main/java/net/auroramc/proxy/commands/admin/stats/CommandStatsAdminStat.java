/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.stats;


import net.auroramc.api.AuroraMCAPI;
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

public class CommandStatsAdminStat extends ProxyCommand {
    public CommandStatsAdminStat() {
        super("stat", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 5) {
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

                    int game;

                    try {
                        game = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    String stat = args.get(3).toLowerCase();

                    long amount;

                    try {
                        amount = Long.parseLong(args.get(4));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    if (amount < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        ProxyAPI.getPlayer(target).getStats().incrementStatistic(game, stat, amount, true);
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "increment_statistic", AuroraMCAPI.getInfo().getName(), uuid + "\n" + game + "\n" + stat + "\n" + amount);
                            CommunicationUtils.sendMessage(message);
                        } else {
                            AuroraMCAPI.getDbManager().statisticIncrement(uuid, game, stat, amount);
                        }

                    }
                    player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("You have added **%s** to that users **%s** statistic.", amount, stat)));
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

                    int game;

                    try {
                        game = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    String stat = args.get(3).toLowerCase();

                    long amount;

                    try {
                        amount = Long.parseLong(args.get(4));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    if (amount < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is an invalid amount."));
                        return;
                    }

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        ProxyAPI.getPlayer(target).getStats().incrementStatistic(game, stat, -amount, true);
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "increment_statistic", AuroraMCAPI.getInfo().getName(), uuid + "\n" + game + "\n" + stat + "\n" + (-amount));
                            CommunicationUtils.sendMessage(message);
                        } else {
                            AuroraMCAPI.getDbManager().statisticIncrement(uuid, game, stat, -amount);
                        }
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("You have added **%s** to that users **%s** statistic.", amount, stat)));
                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                        "**/statsadmin stat add <player> <game> <stat> <amount>** - Increment a statistic on a users statistics\n" +
                        "**/statsadmin stat remove <player> <game> <stat> <amount>** - Decrement a statistic on a users statistics\n"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                    "**/statsadmin stat add <player> <game> <stat> <amount>** - Increment a statistic on a users statistics\n" +
                    "**/statsadmin stat remove <player> <game> <stat> <amount>** - Decrement a statistic on a users statistics\n"));
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
