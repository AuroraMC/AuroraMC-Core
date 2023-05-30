/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.economy;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.stats.PlayerBank;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandTicket extends ProxyCommand {
    public CommandTicket() {
        super("ticket", Collections.singletonList("tickets"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 3) {
            if (args.get(0).equalsIgnoreCase("add")) {
                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(1));

                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Economy", "That user does not exist."));
                        return;
                    }

                    int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                    if (amcId < 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Economy", "You cannot manage the bank of someone who has not joined the network."));
                        return;
                    }

                    int amount;

                    try {
                        amount = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Economy", "That is not a valid amount."));
                        return;
                    }

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                        aTarget.getBank().addTickets(amount, false, true);
                        target.sendMessage(TextFormatter.pluginMessage("Economy", String.format("You have received **%s Tickets** from an admin.", amount)));
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "addtickets", AuroraMCAPI.getInfo().getName(), uuid.toString() + "\n" + amount);
                            CommunicationUtils.sendMessage(message);
                        } else {
                            AuroraMCAPI.getDbManager().ticketsAdded(uuid, amount);
                        }

                    }
                    player.sendMessage(TextFormatter.pluginMessage("Economy", String.format("You have added **%s Tickets** to **%s's** bank.", amount, args.get(1))));

                });
            } else if (args.get(0).equalsIgnoreCase("remove")) {
                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(1));

                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Economy", "That user does not exist."));
                        return;
                    }

                    int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                    if (amcId < 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Economy", "You cannot manage the bank of someone who has not joined the network."));
                        return;
                    }

                    int amount;

                    try {
                        amount = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Economy", "That is not a valid amount."));
                        return;
                    }

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                        if (aTarget.getBank().getTickets() < amount) {
                            player.sendMessage(TextFormatter.pluginMessage("Economy", "That user does not have enough Tickets to remove."));
                            return;
                        }
                        aTarget.getBank().withdrawTickets(amount, false, true);
                        target.sendMessage(TextFormatter.pluginMessage("Economy", String.format("**%s Tickets** have been removed from your account by an admin.", amount)));
                    } else {
                        PlayerBank bank = AuroraMCAPI.getDbManager().getBank(uuid);
                        if (bank.getTickets() < amount) {
                            player.sendMessage(TextFormatter.pluginMessage("Economy", "That user does not have enough Tickets to remove."));
                            return;
                        }
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "removetickets", AuroraMCAPI.getInfo().getName(), uuid.toString() + "\n" + amount);
                            CommunicationUtils.sendMessage(message);
                        } else {
                            AuroraMCAPI.getDbManager().ticketsAdded(uuid, -amount);
                        }
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Economy", String.format("You have removed **%s Tickets** from **%s's** bank.", amount, args.get(1))));

                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Economy", "Invalid syntax. Correct syntax: **/ticket [add|remove] [player] [amount]**"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Economy", "Invalid syntax. Correct syntax: **/ticket [add|remove] [player] [amount]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
