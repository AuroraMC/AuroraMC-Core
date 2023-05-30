/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.managers;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.player.ChatSlowLength;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentLength;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements Listener {

    @EventHandler
    public void onCommand(ChatEvent e) {
        if (e.isCommand()) {
            if (e.getSender() instanceof ProxiedPlayer) {
                ProxiedPlayer pp = (ProxiedPlayer) e.getSender();
                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(pp);
                ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getMessage().split(" ")));
                String commandLabel = args.remove(0).substring(1);
                if (onCommand(commandLabel, args, player)) {
                    e.setCancelled(true);
                } else if (e.isProxyCommand()) {
                    e.setCancelled(true);
                    player.sendMessage(TextFormatter.pluginMessage("Command Manager", "That command is unrecognised."));
                }
            }
        } else {
            if (e.getSender() instanceof ProxiedPlayer) {
                AuroraMCProxyPlayer player = ProxyAPI.getPlayer((ProxiedPlayer) e.getSender());
                if (player.getActiveMutes().size() > 0) {
                    //They have at least 1 active mute, disallow the chat message.
                    e.setCancelled(true);
                    Punishment punishment = player.getActiveMutes().get(0);
                    PunishmentLength length;
                    if (punishment.getExpire() == -1) {
                        length = new PunishmentLength(-1);
                    } else {
                        length = new PunishmentLength((punishment.getExpire() - System.currentTimeMillis())/3600000d);
                    }
                    switch (punishment.getStatus()) {
                        case 2:
                            player.sendMessage(TextFormatter.pluginMessage("Punishments", String.format("You are muted for **%s**.\n" +
                                    "Reason: **%s - %s [Awaiting Approval]**\n" +
                                    "Punishment Code: **%s**\n\n" +
                                    "Your punishment has been applied by a Trial Moderator, and is severe enough to need approval from a Staff Management member to ensure that the punishment applied was correct. When it is approved, the full punishment length will automatically apply to you. If this punishment is denied for being false, **it will automatically be removed**, and the punishment will automatically be removed from your Punishment History.", length.getFormatted(), (AuroraMCAPI.getRules().getRule(punishment.getRuleID())).getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                            break;
                        case 3:
                            player.sendMessage(TextFormatter.pluginMessage("Punishments", String.format("You are muted for **%s**.\n" +
                                    "Reason: **%s - %s [SM Approved]**\n" +
                                    "Punishment Code: **%s**", length.getFormatted(), (AuroraMCAPI.getRules().getRule(punishment.getRuleID())).getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                            break;
                        default:
                            player.sendMessage(TextFormatter.pluginMessage("Punishments", String.format("You are muted for **%s**.\n" +
                                    "Reason: **%s - %s**\n" +
                                    "Punishment Code: **%s**", length.getFormatted(), (AuroraMCAPI.getRules().getRule(punishment.getRuleID())).getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                            break;
                    }
                } else {
                    if (player.getChannel() == ChatChannel.PARTY) {
                        if (player.getParty() != null) {
                            e.setCancelled(true);
                            if (AuroraMCAPI.getFilter() == null) {
                                player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                return;
                            }
                            e.setMessage(AuroraMCAPI.getFilter().filter(e.getMessage()));
                            BaseComponent processed = AuroraMCAPI.getFilter().processEmotes(player, e.getMessage());
                            player.getParty().partyChat(player.getPartyPlayer(), processed, e.getMessage());
                        }
                    } else if (player.getChannel() == ChatChannel.NETWORK) {
                        e.setCancelled(true);
                        if (AuroraMCAPI.getFilter() == null) {
                            player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                            return;
                        }
                        e.setMessage(AuroraMCAPI.getFilter().filter(e.getMessage()));
                        BaseComponent processed = AuroraMCAPI.getFilter().processEmotes(player, e.getMessage());
                        ProtocolMessage message = new ProtocolMessage(Protocol.GLOBAL_MESSAGE, "Mission Control", ProxyAPI.getPlayer(((ProxiedPlayer) e.getSender())).getRank().name(), ((ProxiedPlayer) e.getSender()).getName(), ComponentSerializer.toString(processed) + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                        CommunicationUtils.sendMessage(message);
                    }

                    if (!e.isCancelled()) {
                        if (player.getChannel() != ChatChannel.STAFF) {
                            if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                                if (!(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                                    if (AuroraMCAPI.getChatSilenceEnd() != -1) {
                                        ChatSlowLength length = new ChatSlowLength((AuroraMCAPI.getChatSilenceEnd() - System.currentTimeMillis())/1000d);
                                        player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("Chat across the entire network is currently silenced. You may talk again in **%s**.", length.getFormatted())));
                                    } else {
                                        player.sendMessage(TextFormatter.pluginMessage("Silence", "Chat is currently silenced."));
                                    }
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                            if (AuroraMCAPI.getChatSlow() != -1) {
                                if (player.getLastMessageSent() != -1 && !(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                                    if (System.currentTimeMillis() - player.getLastMessageSent() < AuroraMCAPI.getChatSlow() * 1000) {
                                        ChatSlowLength length = new ChatSlowLength(((AuroraMCAPI.getChatSlow() * 1000) - (System.currentTimeMillis() - player.getLastMessageSent())) / 1000d);
                                        player.sendMessage(TextFormatter.pluginMessage("Message", String.format("There is currently a global chat slow active network-wide. You may chat again in **%s**.", length.getFormatted())));
                                        e.setCancelled(true);
                                    }
                                }
                            }
                            if (!e.isCancelled()) {
                                player.messageSent();
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean onCommand(String commandLabel, List<String> args, AuroraMCProxyPlayer player) {
        ProxyCommand command = (ProxyCommand) AuroraMCAPI.getCommand(commandLabel.toLowerCase());
        if (command != null) {
            for (Permission permission : command.getPermission()) {
                if (player.hasPermission(permission.getId())) {
                    try {
                        command.execute(player, commandLabel, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage(TextFormatter.pluginMessage("Command Manager", "An error occurred when executing this command. Please report this to the admins!"));
                    }
                    return true;
                }
            }

            if (command.showPermissionMessage()) {
                if (command.getCustomPermissionMessage() != null) {
                    player.sendMessage(TextFormatter.pluginMessage("Command Manager", command.getCustomPermissionMessage()));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Command Manager", "You do not have permission to use that command!"));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Command Manager", "That command is unrecognised."));
            }
            return true;
        } else {
            return false;
        }
    }

}
