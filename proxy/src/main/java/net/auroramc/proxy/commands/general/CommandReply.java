/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.player.IgnoredPlayer;
import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandReply extends ProxyCommand {

    public CommandReply() {
        super("reply", Collections.singletonList("r"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (player.getActiveMutes().size() == 0) {
                if (player.getLastMessaged() != null) {
                    AuroraMCProxyPlayer atarget = ProxyAPI.getPlayer(player.getLastMessaged());
                    if (atarget != null) {
                        if (atarget.isIgnored(player.getId())) {
                            player.sendMessage(TextFormatter.pluginMessage("Message", "This player has private messages disabled!"));
                            return;
                        }
                        if (AuroraMCAPI.getFilter() == null) {
                            player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                            return;
                        }
                        String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                        BaseComponent bmessage = AuroraMCAPI.getFilter().processEmotes(player, message);
                        BaseComponent formatted = TextFormatter.privateMessage(player, atarget, false, bmessage);
                        ChatLogs.chatMessage(player.getId(), player.getName(), player.getRank(), message, false, player.getChannel(), atarget.getId(), atarget.getName(), null);
                        atarget.sendMessage(formatted);
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("PlaySound");
                        out.writeUTF("Message");
                        out.writeUTF(atarget.getName());
                        atarget.sendPluginMessage(out.toByteArray());
                        formatted = TextFormatter.privateMessage(player, atarget, true, bmessage);
                        player.sendMessage(formatted);
                    } else {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(player.getLastMessaged())) {
                            UUID uuid = player.getLastMessaged();
                            int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                            List<IgnoredPlayer> ignoredPlayers = AuroraMCAPI.getDbManager().getIgnoredPlayers(id);
                            for (IgnoredPlayer player1 : ignoredPlayers) {
                                if (player1.getId() == player.getId()) {
                                    player.sendMessage(TextFormatter.pluginMessage("Message", "This player has private messages disabled!"));
                                    return;
                                }
                            }

                            PlayerPreferences prefs = AuroraMCAPI.getDbManager().getPlayerPreferences(uuid);
                            switch (prefs.getPrivateMessageMode()) {
                                case FRIENDS_ONLY:
                                    FriendsList list = AuroraMCAPI.getDbManager().getFriendsList(id, uuid);
                                    if (list.getFriends().containsKey(player.getUniqueId())) {
                                        break;
                                    }
                                    player.sendMessage(TextFormatter.pluginMessage("Message", "This player is only accepting private messages from friends!"));
                                    return;
                                case DISABLED:
                                    player.sendMessage(TextFormatter.pluginMessage("Message", "This player has private messages disabled!"));
                                    return;
                            }

                            if (AuroraMCAPI.getFilter() == null) {
                                player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                return;
                            }
                            String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                            BaseComponent bmessage = AuroraMCAPI.getFilter().processEmotes(player, message);

                            ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.MESSAGE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), uuid.toString(), player.getName(), message);
                            CommunicationUtils.sendMessage(protocolMessage);

                            String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                            player.sendMessage(TextFormatter.privateMessage(player, name, bmessage));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Message", "The player you last messaged is no longer online!"));
                            player.setLastMessaged(null);
                        }
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Message", "You have not messaged anyone recently!"));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Message", "You cannot send messages while muted!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Message", "Invalid syntax. Correct syntax: **/reply [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
