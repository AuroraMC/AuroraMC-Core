/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
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

import java.util.*;

public class CommandAddPlusDays extends ProxyCommand {


    public CommandAddPlusDays() {
        super("addplusdays", new ArrayList<>(Arrays.asList("adddays", "addplus")), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                if (uuid == null) {
                    player.sendMessage(TextFormatter.pluginMessage("Plus", "That user does not exist."));
                    return;
                }

                int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                if (amcId < 0) {
                    player.sendMessage(TextFormatter.pluginMessage("Plus", "You cannot add plus to someone who has not joined the network."));
                    return;
                }

                Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);

                if (rank.hasPermission("all")) {
                    player.sendMessage(TextFormatter.pluginMessage("Plus", "You cannot add plus to an Owner."));
                    return;
                }

                int amount;

                try {
                    amount = Integer.parseInt(args.get(1));
                } catch (NumberFormatException e) {
                    player.sendMessage(TextFormatter.pluginMessage("Plus", "That is not a valid number of days."));
                    return;
                }

                if (amount > 365) {
                    player.sendMessage(TextFormatter.pluginMessage("Plus", "You cannot add more than a years worth of plus days at once."));
                    return;
                }

                long endTimestamp = AuroraMCAPI.getDbManager().getExpire(uuid);
                if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
                    //They already have an active subscription
                    AuroraMCAPI.getDbManager().extend(uuid, amount);
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                        aTarget.getActiveSubscription().extend(amount);
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "extend_subscription", AuroraMCAPI.getInfo().getName(), uuid + "\n" + amount);
                            CommunicationUtils.sendMessage(message);
                        }
                    }
                } else {
                    AuroraMCAPI.getDbManager().newSubscription(uuid, amount);
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                    if (target != null) {
                        AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                        aTarget.newSubscription();
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "new_subscription", AuroraMCAPI.getInfo().getName(), uuid.toString());
                            CommunicationUtils.sendMessage(message);
                        }
                    }
                }

                player.sendMessage(TextFormatter.pluginMessage("Plus", String.format("**%s** Plus days added for **%s**.", amount, args.get(0))));

            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Plus", "Invalid syntax. Correct syntax: **/addplusdays [player] [amount]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
