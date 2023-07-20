/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.player.ChatSlowLength;
import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.utils.Pronoun;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.api.utils.TimeLength;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.smp.api.events.player.AsyncPlayerChatEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.ServerChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        switch (e.getPlayer().getChannel()) {
            case STAFF: {
                AuroraMCServerPlayer player = e.getPlayer();
                player.sendMessage(TextFormatter.formatStaffMessage(player, e.getMessage()));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCServerPlayer p = ServerAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.sendMessage(TextFormatter.formatStaffMessage(player, e.getMessage()));
                                p.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
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
                                            ((AuroraMCServerPlayer)recipient).playSound(((AuroraMCServerPlayer)recipient).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
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
                            BaseComponent chat = chatMessage(player, recipient, component);
                            recipient.sendMessage(chat);
                        }
                    }
                }
                BaseComponent chat = chatMessage(player, null, component);
                String extra = ComponentSerializer.toString(chat) + ";" + e.getMessage() + ";" + player.getId() + ";" + player.getByDisguiseName() + ";" + player.getRank().name();
                List<String> destinations = new ArrayList<>();
                String sender = "SMP-Overworld";
                switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                    case "OVERWORLD": {
                        destinations.add("SMP-Nether");
                        destinations.add("SMP-End");
                        break;
                    }
                    case "END": {
                        destinations.add("SMP-Nether");
                        destinations.add("SMP-Overworld");
                        sender = "SMP-End";
                        break;
                    }
                    case "NETHER": {
                        destinations.add("SMP-Overworld");
                        destinations.add("SMP-End");
                        sender = "SMP-Nether";
                        break;
                    }
                }
                for (String destination : destinations) {
                    ProtocolMessage message = new ProtocolMessage(Protocol.MESSAGE, destination, "chat", sender, extra);
                    CommunicationUtils.sendMessage(message);
                }
                ChatLogs.chatMessage(player.getId(), player.getName(), player.getRank(), e.getMessage(), false, ChatChannel.ALL, -1, null, null);
                break;
        }

    }

    public static BaseComponent chatMessage(@NotNull AuroraMCServerPlayer player, AuroraMCPlayer recipient, @NotNull BaseComponent message) {
        Rank rank = player.getRank();
        if (player.getActiveDisguise() != null && !(player.equals(recipient) && player.getPreferences().isHideDisguiseNameEnabled())) {
            rank = player.getActiveDisguise().getRank();
        }
        TextComponent chatMessage = new TextComponent("");

        if (player.getSmpTeam() != null) {
            int r = 170;
            int g = 0;
            int b = 170;
            int r2 = 255;
            int g2 = 85;
            int b2 = 255;

            int deltaR = r2 - r;
            int deltaG = g2 - g;
            int deltaB = b2 - b;
            char[] chars = player.getSmpTeam().getName().toCharArray();

            int i = 1;

            for (char c : chars) {
                int r3 = (int)Math.floor(r + ((i/(double)chars.length)*deltaR));
                int g3 = (int)Math.floor(g + ((i/(double)chars.length)*deltaG));
                int b3 = (int)Math.floor(b + ((i/(double)chars.length)*deltaB));

                TextComponent component = new TextComponent(c + "");
                component.setColor(ChatColor.of(new Color(r3, g3, b3)));

                chatMessage.addExtra(component);
                i++;
            }

            chatMessage.addExtra(" ");
        }

        //Adding rank prefix if it exists.
        if (rank.getPrefixAppearance() != null) {
            //String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase(), ((player.getActiveSubscription() != null && !rank.hasPermission("moderation") && !rank.hasPermission("build") && !rank.hasPermission("debug.info"))?String.format("&%s+&%s", player.getActiveSubscription().getColor(), rank.getPrefixColor()):""))
            TextComponent prefix = new TextComponent("");


            TextComponent cmp = new TextComponent("«");
            cmp.setColor(rank.getPrefixColor());
            cmp.setBold(true);
            prefix.addExtra(cmp);

            cmp = new TextComponent(rank.getPrefixAppearance().toUpperCase());
            cmp.setColor(rank.getPrefixColor());
            cmp.setBold(true);
            prefix.addExtra(cmp);

            if (player.getActiveSubscription() != null && !rank.hasPermission("moderation") && !rank.hasPermission("build") && !rank.hasPermission("debug.info")) {
                cmp = new TextComponent("+");
                cmp.setColor(player.getActiveSubscription().getColor());
                cmp.setBold(true);
                prefix.addExtra(cmp);
            }

            cmp = new TextComponent("»");
            cmp.setColor(rank.getPrefixColor());
            cmp.setBold(true);
            prefix.addExtra(cmp);

            prefix.addExtra(" ");



            if (rank.getPrefixHoverText() != null) {
                TextComponent hoverText = new TextComponent("");
                hoverText.addExtra(rank.getPrefixHoverText());
                if (player.getActiveSubscription() != null) {
                    if (rank != Rank.ELITE && rank != Rank.MASTER && rank.getPrefixHoverURL() == null) {
                        hoverText.addExtra("\n\n");
                        hoverText.addExtra(player.getActiveSubscription().getHoverText());
                        prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.net/"));
                    } else {
                        hoverText.addExtra(player.getActiveSubscription().getHoverText());
                    }
                } else if (rank == Rank.ELITE || rank == Rank.MASTER) {
                    TextComponent cmp2 = new TextComponent("Click to visit the store!");
                    cmp2.setBold(false);
                    cmp2.setColor(ChatColor.GREEN);
                    hoverText.addExtra(cmp2);
                }
                prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText}));
            }
            if (rank.getPrefixHoverURL() != null) {
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, rank.getPrefixHoverURL()));
            }

            chatMessage.addExtra(prefix);
        } else if (player.getActiveSubscription() != null) {
            TextComponent prefix = new TextComponent("+");
            prefix.setColor(player.getActiveSubscription().getColor());
            prefix.setBold(false);

            TextComponent hoverText = new TextComponent(player.getActiveSubscription().getHoverText());
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText}));

            prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, player.getActiveSubscription().getClickURL()));

            chatMessage.addExtra(prefix);
            chatMessage.addExtra(" ");
        }


        String name;
        //Adding in name.
        if (player.getActiveDisguise() != null && !(player.equals(recipient) && player.getPreferences().isHideDisguiseNameEnabled())) {
            name = player.getActiveDisguise().getName();
        } else {
            name = player.getName();
        }

        TextComponent nameComponent = new TextComponent(name);

        //Adding in name color.
        if (player.getTeam() != null) {
            nameComponent.setColor(player.getTeam().getTeamColor());
        } else {
            nameComponent.setColor(rank.getNameColor());
        }

        TextComponent componentBuilder = new TextComponent("");
        TextComponent cmp = new TextComponent(name);
        cmp.setColor(nameComponent.getColor());
        componentBuilder.addExtra(cmp);
        componentBuilder.addExtra("\n");


        if (player.getPreferences().getPreferredPronouns() != Pronoun.NONE) {
            cmp = new TextComponent(player.getPreferences().getPreferredPronouns().getFull());
            cmp.setColor(ChatColor.GRAY);
            cmp.setBold(false);
            componentBuilder.addExtra(cmp);
            componentBuilder.addExtra("\n");
        }

        componentBuilder.addExtra("\n");

        cmp = new TextComponent("Games Played: ");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        componentBuilder.addExtra(cmp);

        cmp = new TextComponent(String.valueOf(player.getStats().getGamesPlayed()));
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(false);
        componentBuilder.addExtra(cmp);
        componentBuilder.addExtra("\n");

        cmp = new TextComponent("In-Game Time: ");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        componentBuilder.addExtra(cmp);

        cmp = new TextComponent(new TimeLength(player.getStats().getGameTimeMs()/3600000d, false).getFormatted());
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(false);
        componentBuilder.addExtra(cmp);



        nameComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{componentBuilder}));

        chatMessage.addExtra(nameComponent);

        TextComponent connector = new TextComponent(" » ");
        connector.setColor(rank.getConnectorColor());
        connector.setBold(false);

        //Adding in spacer.
        chatMessage.addExtra(connector);

        //Adding in actual chat message. If disguised then they should still have access to colour chat, so override the disguise rank.
        chatMessage.addExtra(message);

        //Returns the final result.
        return chatMessage;
    }
}
