/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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

public class CommandFriendRemove extends ProxyCommand {

    public CommandFriendRemove() {
        super("remove", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            Friend friend = null;
            for (Friend f : player.getFriendsList().getFriends().values()) {
                if (f.getName().equalsIgnoreCase(args.get(0))) {
                    friend = f;
                    break;
                }
            }
            if (friend == null) {
                player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You do not have a friend called **%s**.", args.get(0))));
                return;
            }

            UUID uuid = friend.getUuid();
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
            player.getFriendsList().friendRemoved(uuid, true, true);
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(31))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(31), 1, true);
            }
            if (target != null) {
                AuroraMCProxyPlayer auroraMCPlayer = ProxyAPI.getPlayer(target);
                auroraMCPlayer.getFriendsList().friendRemoved(player.getUniqueId(), true, false);
            } else {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                        UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "remove", player.getUniqueId().toString(), uuid.toString());
                        CommunicationUtils.sendMessage(message);
                    }
                });
            }
            player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You have removed **%s** as a friend!", friend.getName())));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Friends", "Invalid syntax. Correct syntax: **/friend remove [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
