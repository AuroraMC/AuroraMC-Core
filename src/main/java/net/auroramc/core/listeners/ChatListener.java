/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ChatLogs;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatChannel;
import net.auroramc.core.api.players.ChatSlowLength;
import net.auroramc.core.api.players.PlayerPreferences;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        if (AuroraMCAPI.getPlayer(e.getPlayer()) == null) {
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Chat Manager", "Your player profile is still loading, please wait to chat!"));
            return;
        } else {
            if (!AuroraMCAPI.getPlayer(e.getPlayer()).isLoaded()) {
                e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Chat Manager", "Your player profile is still loading, please wait to chat!"));
                return;
            }
        }
        if (AuroraMCAPI.getPlayer(e.getPlayer()).isVanished()) {
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Vanish", "You cannot talk while vanished!"));
            return;
        }
        switch (AuroraMCAPI.getPlayer(e.getPlayer()).getChannel()) {
            case STAFF: {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, e.getMessage()));
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCPlayer p = AuroraMCAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, e.getMessage()));
                                p.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                            }
                        }

                    }
                }
                break;
            }
            case ALL:
            case PARTY:
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                if (!player.getPreferences().isChatVisibilityEnabled()) {
                    e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "You currently have chat disabled! Please enable chat in order to send messages again."));
                    return;
                }
                if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                    if (!(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                        if (AuroraMCAPI.getChatSilenceEnd() != -1) {
                            ChatSlowLength length = new ChatSlowLength((AuroraMCAPI.getChatSilenceEnd() - System.currentTimeMillis())/1000d);
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", String.format("Chat is currently silenced. You may talk again in **%s**.", length.getFormatted())));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", "Chat is currently silenced."));
                        }
                        return;
                    }
                }
                if (AuroraMCAPI.getChatSlow() != -1) {
                    if (player.getLastMessageSent() != -1 && !(player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info"))) {
                        if (System.currentTimeMillis() - player.getLastMessageSent() < AuroraMCAPI.getChatSlow() * 1000) {
                            ChatSlowLength length = new ChatSlowLength(((AuroraMCAPI.getChatSlow() * 1000) - (System.currentTimeMillis() - player.getLastMessageSent())) / 1000d);
                            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", String.format("There is currently a chat slow active in this server. You may chat again in **%s**.", length.getFormatted())));
                            return;
                        }
                    }
                }
                player.messageSent();
                if (AuroraMCAPI.getFilter() == null) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                    return;
                }
                player.getStats().addProgress(AuroraMCAPI.getAchievement(6), 1, player.getStats().getAchievementsGained().getOrDefault(AuroraMCAPI.getAchievement(6), 0), true);
                e.setMessage(AuroraMCAPI.getFilter().filter(player, e.getMessage()));
                if (e.getMessage().contains("mod")) {
                    if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(14))) {
                        player.getStats().achievementGained(AuroraMCAPI.getAchievement(14), 1, true);
                    }
                }
                if (player.isDisguised()) {
                    if (player.getPreferences().isHideDisguiseNameEnabled()) {
                        player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().undisguisedChatMessage(player, AuroraMCAPI.getFilter().processMentions(e.getMessage())));
                    } else {
                        player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().chatMessage(player, AuroraMCAPI.getFilter().processMentions(e.getMessage())));
                    }
                } else {
                    player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().chatMessage(player, AuroraMCAPI.getFilter().processMentions(e.getMessage())));
                }
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (AuroraMCAPI.getPlayer(player2).getPreferences().isChatVisibilityEnabled()) {
                        if (!AuroraMCAPI.getPlayer(player2).isIgnored(player.getId()) || AuroraMCAPI.getPlayer(player2).hasPermission("moderation")) {
                            if (!player2.equals(player.getPlayer())) {
                                AuroraMCPlayer recipient = AuroraMCAPI.getPlayer(player2);
                                String mentionedMessage = AuroraMCAPI.getFilter().processMentions(player, recipient, e.getMessage());
                                player2.spigot().sendMessage(AuroraMCAPI.getFormatter().chatMessage(player, mentionedMessage));
                                if (!e.getMessage().equals(mentionedMessage)) {
                                    if (recipient.getActiveMutes().size() > 0 && recipient.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS) {
                                        String msg = AuroraMCAPI.getFormatter().privateMessage(recipient.getPlayer().getName(), player, "Hey! I'm currently muted and cannot message you right now.");
                                        recipient.getPlayer().sendMessage(msg);
                                        player.getPlayer().sendMessage(msg);
                                    }
                                    if (recipient.getPreferences().isPingOnChatMentionEnabled()) {
                                        recipient.getPlayer().playSound(recipient.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                                    }
                                }
                            }

                        }
                    }
                }
                ChatLogs.chatMessage(player.getId(), player.getName(), player.getRank(), e.getMessage(), player.isDead(), ChatChannel.ALL);
                break;
        }

    }
}
