/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.player.ChatSlowLength;
import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.player.AsyncPlayerChatEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.ServerChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        switch (e.getPlayer().getChannel()) {
            case STAFF: {
                AuroraMCServerPlayer player = e.getPlayer();
                player.sendMessage(TextFormatter.formatStaffMessage(player, e.getMessage()));
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCServerPlayer p = ServerAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.sendMessage(TextFormatter.formatStaffMessage(player, e.getMessage()));
                                p.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 2);
                            }
                        }

                    }
                }
                break;
            }
            case TEAM: {
                if (e.getPlayer().isVanished()) {
                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Vanish", "You cannot talk while vanished!"));
                    return;
                }
                AuroraMCServerPlayer player = e.getPlayer();
                if (player.getTeam() != null) {
                    if (!player.getPreferences().isChatVisibilityEnabled()) {
                        e.getPlayer().sendMessage(TextFormatter.pluginMessage("Message", "You currently have chat disabled! Please enable chat in order to send messages again."));
                        return;
                    }
                    if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                        if (!(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                            if (AuroraMCAPI.getChatSilenceEnd() != -1) {
                                ChatSlowLength length = new ChatSlowLength((AuroraMCAPI.getChatSilenceEnd() - System.currentTimeMillis())/1000d);
                                player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("Chat is currently silenced. You may talk again in **%s**.", length.getFormatted())));
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Silence", "Chat is currently silenced."));
                            }
                            return;
                        }
                    }
                    if (AuroraMCAPI.getChatSlow() != -1) {
                        if (player.getLastMessageSent() != -1 && !(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                            if (System.currentTimeMillis() - player.getLastMessageSent() < AuroraMCAPI.getChatSlow() * 1000) {
                                ChatSlowLength length = new ChatSlowLength(((AuroraMCAPI.getChatSlow() * 1000) - (System.currentTimeMillis() - player.getLastMessageSent())) / 1000d);
                                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Message", String.format("There is currently a chat slow active in this server. You may chat again in **%s**.", length.getFormatted())));
                                return;
                            }
                        }
                    }
                    player.messageSent();
                    if (AuroraMCAPI.getFilter() == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                        return;
                    }
                    player.getStats().addProgress(AuroraMCAPI.getAchievement(6), 1, player.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(6), 0), true);
                    if (e.getMessage().contains("hacks") || e.getMessage().contains("hax") || e.getMessage().contains("hacker") || e.getMessage().contains("haxxer") || e.getMessage().contains("haxer")) {
                        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(28))) {
                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(28), 1, true);
                        }
                    }
                    e.setMessage(AuroraMCAPI.getFilter().filter(player, e.getMessage()));
                    BaseComponent component = AuroraMCAPI.getFilter().processEmotes(player, e.getMessage());
                    for (AuroraMCPlayer recipient : player.getTeam().getPlayers()) {
                        if (recipient.getPreferences().isChatVisibilityEnabled()) {
                            if (!recipient.isIgnored(player.getId()) || recipient.hasPermission("moderation")) {
                                if (!recipient.equals(player)) {
                                    ServerChatUtils.MentionMessage mentionedMessage = ServerChatUtils.processMentions((AuroraMCServerPlayer) recipient, (TextComponent) component);

                                    recipient.sendMessage(TextFormatter.formatTeamChat(player, recipient, mentionedMessage.getFormattedText()));
                                    if (mentionedMessage.isMentionFound()) {
                                        if (recipient.getActiveMutes().size() > 0 && recipient.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS) {
                                            BaseComponent msg = TextFormatter.privateMessage(recipient, player, true, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                            recipient.sendMessage(msg);
                                            msg = TextFormatter.privateMessage(recipient, player, false, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                            player.sendMessage(msg);
                                        }
                                        if (recipient.getPreferences().isPingOnChatMentionEnabled()) {
                                            ((AuroraMCServerPlayer)recipient).playSound(((AuroraMCServerPlayer)recipient).getLocation(), Sound.NOTE_PLING, 100, 2);
                                        }
                                    }
                                } else {
                                    BaseComponent mentionedMessage = ServerChatUtils.processMentions((TextComponent) component);
                                    recipient.sendMessage(TextFormatter.formatTeamChat(player, recipient, mentionedMessage));
                                }

                            }
                        }
                    }
                    break;
                }
            }
            case ALL:
            case PARTY:
                if (e.getPlayer().isVanished()) {
                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Vanish", "You cannot talk while vanished!"));
                    return;
                }
                AuroraMCServerPlayer player = e.getPlayer();
                if (!player.getPreferences().isChatVisibilityEnabled()) {
                    e.getPlayer().sendMessage(TextFormatter.pluginMessage("Message", "You currently have chat disabled! Please enable chat in order to send messages again."));
                    return;
                }
                if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                    if (!(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                        if (AuroraMCAPI.getChatSilenceEnd() != -1) {
                            ChatSlowLength length = new ChatSlowLength((AuroraMCAPI.getChatSilenceEnd() - System.currentTimeMillis())/1000d);
                            player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("Chat is currently silenced. You may talk again in **%s**.", length.getFormatted())));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Silence", "Chat is currently silenced."));
                        }
                        return;
                    }
                }
                if (AuroraMCAPI.getChatSlow() != -1) {
                    if (player.getLastMessageSent() != -1 && !(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                        if (System.currentTimeMillis() - player.getLastMessageSent() < AuroraMCAPI.getChatSlow() * 1000) {
                            ChatSlowLength length = new ChatSlowLength(((AuroraMCAPI.getChatSlow() * 1000) - (System.currentTimeMillis() - player.getLastMessageSent())) / 1000d);
                            e.getPlayer().sendMessage(TextFormatter.pluginMessage("Message", String.format("There is currently a chat slow active in this server. You may chat again in **%s**.", length.getFormatted())));
                            return;
                        }
                    }
                }
                player.messageSent();
                if (AuroraMCAPI.getFilter() == null) {
                    player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                    return;
                }
                player.getStats().addProgress(AuroraMCAPI.getAchievement(6), 1, player.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(6), 0), true);
                if (e.getMessage().contains("hacks") || e.getMessage().contains("hax") || e.getMessage().contains("hacker") || e.getMessage().contains("haxxer") || e.getMessage().contains("haxer")) {
                    if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(28))) {
                        player.getStats().achievementGained(AuroraMCAPI.getAchievement(28), 1, true);
                    }
                }
                e.setMessage(AuroraMCAPI.getFilter().filter(player, e.getMessage()));
                BaseComponent component = AuroraMCAPI.getFilter().processEmotes(player, e.getMessage());
                if (e.getMessage().matches(".*\\bmod\\b.*")) {
                    if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(14))) {
                        player.getStats().achievementGained(AuroraMCAPI.getAchievement(14), 1, true);
                    }
                }
                for (AuroraMCServerPlayer recipient : ServerAPI.getPlayers()) {
                    if (recipient.getPreferences().isChatVisibilityEnabled()) {
                        if (!recipient.isIgnored(player.getId()) || recipient.hasPermission("moderation")) {
                            if (!recipient.equals(player)) {
                                ServerChatUtils.MentionMessage mentionedMessage = ServerChatUtils.processMentions(recipient, (TextComponent) component);

                                if (ServerAPI.isEventMode()) {
                                    recipient.sendMessage(TextFormatter.eventMessage(player, recipient, mentionedMessage.getFormattedText()));
                                } else {
                                    recipient.sendMessage(TextFormatter.chatMessage(player, recipient, mentionedMessage.getFormattedText()));
                                }
                                if (mentionedMessage.isMentionFound()) {
                                    if (recipient.getActiveMutes().size() > 0 && recipient.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS) {
                                        BaseComponent msg = TextFormatter.privateMessage(recipient, player, true, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                        recipient.sendMessage(msg);
                                        msg = TextFormatter.privateMessage(recipient, player, false, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                        player.sendMessage(msg);
                                    }
                                    if (recipient.getPreferences().isPingOnChatMentionEnabled()) {
                                        recipient.playSound(recipient.getLocation(), Sound.NOTE_PLING, 100, 2);
                                    }
                                }
                            } else {
                                BaseComponent mentionedMessage = ServerChatUtils.processMentions((TextComponent) component);
                                if (ServerAPI.isEventMode()) {
                                    recipient.sendMessage(TextFormatter.eventMessage(player, recipient, mentionedMessage));
                                } else {
                                    recipient.sendMessage(TextFormatter.chatMessage(player, recipient, mentionedMessage));
                                }
                            }

                        }
                    }
                }
                ChatLogs.chatMessage(player.getId(), player.getName(), player.getRank(), e.getMessage(), player.isDead(), ChatChannel.ALL, -1, null, null);
                break;
        }

    }
}
