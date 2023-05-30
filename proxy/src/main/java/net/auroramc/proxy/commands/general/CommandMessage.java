/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.player.Disguise;
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
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandMessage extends ProxyCommand {

    public CommandMessage() {
        super("message", Arrays.asList("msg", "tell", "m", "honk", "whisper", "w", "t"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (player.getActiveMutes().size() == 0) {
                AuroraMCProxyPlayer target = ProxyAPI.getDisguisedPlayer(args.get(0));
                if (target == null) {
                    target = ProxyAPI.getPlayer(args.get(0));
                }
                if (target != null) {
                    if (target.getActiveDisguise() != null) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", String.format("No match found for [**%s**]", args.get(0))));
                        return;
                    }
                    if (target.isIgnored(player.getId())) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", "This player has private messages disabled!"));
                        return;
                    }
                    switch (target.getPreferences().getPrivateMessageMode()) {
                        case FRIENDS_ONLY:
                            if (target.getFriendsList().getFriends().containsKey(player.getUniqueId())) {
                                break;
                            }
                            player.sendMessage(TextFormatter.pluginMessage("Message", "This player is only accepting private messages from friends!"));
                            return;
                        case DISABLED:
                            player.sendMessage(TextFormatter.pluginMessage("Message", "This player has private messages disabled!"));
                            return;
                    }
                    args.remove(0);
                    if (AuroraMCAPI.getFilter() == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                        return;
                    }
                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                    BaseComponent bmessage = AuroraMCAPI.getFilter().processEmotes(player, message);
                    BaseComponent formatted = TextFormatter.privateMessage(player, target, false, bmessage);
                    target.sendMessage(formatted);
                    ChatLogs.chatMessage(player.getId(), player.getName(), player.getRank(), message, false, player.getChannel(), target.getId(), target.getName(), null);
                    if (target.getPreferences().isPingOnPrivateMessageEnabled()) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("PlaySound");
                        out.writeUTF(target.getName());
                        out.writeUTF("NOTE_PLING");
                        out.writeInt(100);
                        out.writeInt(2);
                        target.sendPluginMessage(out.toByteArray());
                    }
                    formatted = TextFormatter.privateMessage(player, target, true, bmessage);
                    player.sendMessage(formatted);
                    player.setLastMessaged(target.getUniqueId());
                    if (target.getActiveMutes().size() > 0) {
                        switch (target.getPreferences().getMuteInformMode()) {
                            case MESSAGE_AND_MENTIONS:
                            case MESSAGE_ONLY:
                                BaseComponent msg = TextFormatter.privateMessage(target, player, true, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                player.sendMessage(msg);
                                msg = TextFormatter.privateMessage(target, player, false, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                target.sendMessage(msg);
                        }
                    }
                } else {
                    ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                        UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                        if (uuid == null) {
                            uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                        }
                        if (uuid != null) {
                            if (!AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                                if (!AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    player.sendMessage(TextFormatter.pluginMessage("Message", String.format("No match found for [**%s**]", args.get(0))));
                                    return;
                                }
                            }
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

                            args.remove(0);
                            if (AuroraMCAPI.getFilter() == null) {
                                player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                return;
                            }
                            String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                            BaseComponent bmessage = AuroraMCAPI.getFilter().processEmotes(player, message);

                            ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.MESSAGE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), uuid.toString(), player.getName(), ComponentSerializer.toString(bmessage));
                            CommunicationUtils.sendMessage(protocolMessage);

                            String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                            Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(uuid);
                            if (disguise != null) {
                                name = disguise.getName();
                            }
                            player.sendMessage(TextFormatter.privateMessage(player, name, bmessage));
                            player.setLastMessaged(uuid);
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Message", String.format("No match found for [**%s**]", args.get(0))));
                        }
                    });
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Message", "You cannot send messages while muted!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Message", "Invalid syntax. Correct syntax: **/message [player] [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
