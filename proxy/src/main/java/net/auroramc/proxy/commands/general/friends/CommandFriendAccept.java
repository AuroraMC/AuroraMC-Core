/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.friends;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
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

public class CommandFriendAccept extends ProxyCommand {
    public CommandFriendAccept() {
        super("accept", Collections.emptyList(), Collections.emptyList(), false, null);
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

            int limit = 250;
            if (player.hasPermission("elite")) {
                limit += 50;
            }
            if (player.hasPermission("master")) {
                limit += 50;
            }
            if (player.hasPermission("plus")) {
                limit += 50;
            }
            if (player.getFriendsList().getFriends().values().size() + (int) player.getFriendsList().getPendingFriendRequests().values().stream().filter((friend1) -> friend1.getType() == Friend.FriendType.PENDING_OUTGOING).count() >= limit) {
                player.sendMessage(TextFormatter.pluginMessage("Friends", "You have already reached your Friends limit! Cancel pending outgoing friend requests or delete friends in order to add more! You can get more friend slots by purchasing a rank on our store!"));
                return;
            }

            UUID uuid = friend.getUuid();

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(30))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(30), 1, true);
            }
            if (target != null) {
                AuroraMCProxyPlayer auroraMCPlayer = ProxyAPI.getPlayer(target);
                FriendsList.VisibilityMode visibilityMode = auroraMCPlayer.getFriendsList().getVisibilityMode();
                player.getFriendsList().friendRequestAccepted(uuid, true, ((visibilityMode == FriendsList.VisibilityMode.ALL)?auroraMCPlayer.getServer().getName():null), auroraMCPlayer.getFriendsList().getCurrentStatus(),true);
                visibilityMode = player.getFriendsList().getVisibilityMode();
                auroraMCPlayer.getFriendsList().friendRequestAccepted(player.getUniqueId(), true, ((visibilityMode == FriendsList.VisibilityMode.ALL)?player.getServer().getName():null), player.getFriendsList().getCurrentStatus(),true);
                target.sendMessage(TextFormatter.pluginMessage("Friends", String.format("Your friend request to **%s** was accepted!", player.getName())));
            } else {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                        UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "accepted", player.getUniqueId().toString(), uuid.toString());
                        CommunicationUtils.sendMessage(message);
                        FriendsList.VisibilityMode visibilityMode = AuroraMCAPI.getDbManager().getVisibilityMode(uuid);
                        FriendStatus status = AuroraMCAPI.getDbManager().getFriendStatus(uuid);
                        String server = AuroraMCAPI.getDbManager().getCurrentServer(uuid);
                        player.getFriendsList().friendRequestAccepted(uuid, true, ((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?null:server):null), status, true);
                    } else {
                        player.getFriendsList().friendRequestAccepted(uuid, false, null, (FriendStatus) AuroraMCAPI.getCosmetics().get(101), true);
                    }
                });
            }

            player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You accepted a friend request from **%s**!", friend.getName())));

        } else {
            player.sendMessage(TextFormatter.pluginMessage("Friends", "Invalid syntax. Correct syntax: **/friend accept [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
