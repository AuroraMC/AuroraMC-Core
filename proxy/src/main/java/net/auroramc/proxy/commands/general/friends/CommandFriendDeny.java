/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.friends;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.friends.Friend;
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
import java.util.stream.Collectors;

public class CommandFriendDeny extends ProxyCommand {
    public CommandFriendDeny() {
        super("deny", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            Friend friend = null;
            for (Friend f : player.getFriendsList().getPendingFriendRequests().values().stream().filter((friend1 -> friend1.getType() == Friend.FriendType.PENDING_INCOMING)).collect(Collectors.toList())) {
                if (f.getName().equalsIgnoreCase(args.get(0))) {
                    friend = f;
                    break;
                }
            }
            if (friend == null) {
                player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You do not have an incoming friend request from **%s**.", args.get(0))));
                return;
            }

            UUID uuid = friend.getUuid();

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
            player.getFriendsList().friendRequestRemoved(uuid, false, true);
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(32))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(32), 1, true);
            }
            if (target != null) {
                AuroraMCProxyPlayer auroraMCPlayer = ProxyAPI.getPlayer(target);
                auroraMCPlayer.getFriendsList().friendRequestRemoved(player.getUniqueId(), true, false);
            } else {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                        UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "cancel", player.getUniqueId().toString(), uuid.toString());
                        CommunicationUtils.sendMessage(message);
                    }
                });
            }

            player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You denied a friend request from **%s**!", friend.getName())));

        } else {
            player.sendMessage(TextFormatter.pluginMessage("Friends", "Invalid syntax. Correct syntax: **/friend deny [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
