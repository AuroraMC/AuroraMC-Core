/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils;

import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.IgnoredPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.md_5.bungee.api.chat.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextFormatter {

    private final String prefixFormat = "&3&l«%s» &r%s";
    private final String nonPrefixFormat = "&r%s";
    private final String chatPrefixFormat = "&%s«%s%s»";
    private final String chatLevelFormat = "&%s«%s»";
    private final String chatUltimateFormat = "&%s&l%s";
    private final String chatStaffMessageFormat = " &r&%s%s %s &l»&r ";
    private final String chatStaffMessageFormatFrom = " &cFrom: &r&%s%s %s &l»&r ";
    private final String chatStaffMessageFormatTo = " &cTo: &r&%s%s %s &l»&r ";
    private final String chatPrivateMessageFormat = "&b&l%s&r &3➜&r &b&l%s&r &3&l»&r %s";

    private final char normalColor = 'r';
    private final char highlightColor = 'b';

    public String highlight(@NotNull String message) {
        List<String> toHighlight = new ArrayList<>(Arrays.asList(message.split("[*]{2}")));
        StringBuilder highightedMessage = new StringBuilder();
        for (int i = 0;i<toHighlight.size();i++) {
            if (i % 2 == 1) {
                highightedMessage.append("&").append(highlightColor).append(toHighlight.get(i)).append("&").append(normalColor);
            } else {
                highightedMessage.append(toHighlight.get(i));
            }
        }

        return highightedMessage.toString();
    }

    public String convert(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String pluginMessage(@Nullable String prefix, @NotNull String message) {
        return convert((prefix == null || prefix.equals(""))?highlight(String.format(nonPrefixFormat, message)):highlight(String.format(prefixFormat, prefix.toUpperCase(), message)));
    }

    public String rankFormat(Rank rank, PlusSubscription subscription) {
        if (rank.getPrefixAppearance() != null) {
            return convert(String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase(), ((subscription != null)?"+":"")));
        } else {
            return "";
        }
    }

    public BaseComponent chatMessage(@NotNull AuroraMCPlayer player, @NotNull String message) {
        Rank rank = player.getRank();
        TextComponent chatMessage = new TextComponent("");

        TextComponent level = new TextComponent(convert(String.format(chatLevelFormat, ((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')):((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')), player.getStats().getLevel())));
        ComponentBuilder levelHover;
        if (player.getStats().getLevel() != 250) {
            String progress = "||||||||||||||||||||||||||||||";
            double percentage = (((double) player.getStats().getXpIntoLevel() / LevelUtils.xpForLevel(player.getStats().getLevel() + 1))*100);
            if (player.getStats().getLevel() != 250) {
                int amountToColour = (int) Math.floor(((percentage) / 100)*30);
                progress = ((progress.substring(0, amountToColour) + "&r&l" + progress.substring(amountToColour + 1)));
            } else {
                percentage = 100.0;
            }
            levelHover = new ComponentBuilder(convert(highlight(String.format("%s\n\nCurrent Level: **Level %s**\nTotal EXP Earned: **%s**\n\n &3&l«%s» &r&b&l%s&r &3&l«%s»\nProgress to Next Level: **%s%%**", String.format(chatLevelFormat, ((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')):((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')) + "&l", "LEVEL " + player.getStats().getLevel()), player.getStats().getLevel(), String.format("%,d", player.getStats().getTotalXpEarned()), player.getStats().getLevel() - ((player.getStats().getLevel() == 250)?1:0), progress, player.getStats().getLevel() + ((player.getStats().getLevel() != 250)?1:0), new DecimalFormat("##.#").format(percentage)))));
        } else {
            levelHover = new ComponentBuilder(convert(highlight(String.format("%s\n\nCurrent Level: **Level %s**\nTotal EXP Earned: **%s**\n\n&3&lMAX LEVEL", String.format(chatLevelFormat, ((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')):((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')) + "&l", "LEVEL " + player.getStats().getLevel()), player.getStats().getLevel(), String.format("%,d", player.getStats().getTotalXpEarned())))));
        }
        level.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, levelHover.create()));
        chatMessage.addExtra(level);

        chatMessage.addExtra(convert("&r "));

        //Adding rank prefix if it exists.
        if (rank.getPrefixAppearance() != null) {
            TextComponent prefix = new TextComponent(convert(String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase(), ((player.getActiveSubscription() != null)?String.format("&%s+&%s", player.getActiveSubscription().getColor(), rank.getPrefixColor()):""))));
            if (rank.getPrefixHoverText() != null) {
                ComponentBuilder hoverText = new ComponentBuilder(convert(rank.getPrefixHoverText()));
                if (player.getActiveSubscription() != null) {
                    if (rank == Rank.ELITE || rank == Rank.MASTER) {
                        hoverText.append(convert(String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                    } else if (rank.getPrefixHoverURL() == null) {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                        prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.net/"));
                    } else {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText().replace("\n\n&aClick to visit the store!", ""), player.getActiveSubscription().getColor())));
                    }
                } else if (rank == Rank.ELITE || rank == Rank.MASTER) {
                    hoverText.append(convert("&aClick to visit the store!"));
                }
                prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));
            }
            if (rank.getPrefixHoverURL() != null) {
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, rank.getPrefixHoverURL()));
            }

            chatMessage.addExtra(prefix);
            chatMessage.addExtra(" ");
        } else if (player.getActiveSubscription() != null) {
            TextComponent prefix = new TextComponent(convert(String.format("&%s+&r", player.getActiveSubscription().getColor())));

            ComponentBuilder hoverText = new ComponentBuilder(convert(String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));

            prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, player.getActiveSubscription().getClickURL()));

            chatMessage.addExtra(prefix);
            chatMessage.addExtra(" ");
        }


        String name;
        //Adding in name.
        name = player.getPlayer().getDisplayName();

        //Adding in name color.
        if (player.getTeam() != null) {
            chatMessage.addExtra(new TextComponent(convert("&" + player.getTeam().getTeamColor() + name)));
        } else {
            chatMessage.addExtra(new TextComponent(convert("&" + rank.getNameColor() + name)));
        }


        //Adding in spacer.
        chatMessage.addExtra(new TextComponent(convert(" &" + rank.getConnectorColor() + "» ")));

        //Adding in actual chat message. If disguised then they should still have access to colour chat, so override the disguise rank.
        if (player.getRank().canUseColorCodes()) {
            chatMessage.addExtra(convert(highlight("&" + rank.getDefaultChatColor() + message)));
        } else {
            chatMessage.addExtra(convert("&" + rank.getDefaultChatColor()) + message);
        }

        //Returns the final result.
        return chatMessage;
    }

    public BaseComponent undisguisedChatMessage(@NotNull AuroraMCPlayer player, @NotNull String message) {
        Rank rank = player.getRank();
        if (player.getActiveDisguise() != null) {
            rank = player.getActiveDisguise().getRank();
        }
        TextComponent chatMessage = new TextComponent("");

        TextComponent level = new TextComponent(convert(String.format(chatLevelFormat, ((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')):((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')), player.getStats().getLevel())));
        ComponentBuilder levelHover;
        if (player.getStats().getLevel() != 250) {
            String progress = "||||||||||||||||||||||||||||||";
            double percentage = (((double) player.getStats().getXpIntoLevel() / LevelUtils.xpForLevel(player.getStats().getLevel() + 1))*100);
            if (player.getStats().getLevel() != 250) {
                int amountToColour = (int) Math.floor(((percentage) / 100)*30);
                progress = ((progress.substring(0, amountToColour) + "&r&l" + progress.substring(amountToColour + 1)));
            } else {
                percentage = 100.0;
            }
            levelHover = new ComponentBuilder(convert(highlight(String.format("%s\n\nCurrent Level: **Level %s**\nTotal EXP Earned: **%s**\n\n &3&l«%s» &r&b&l%s&r &3&l«%s»\nProgress to Next Level: **%s%%**", String.format(chatLevelFormat, ((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')):((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')) + "&l", "LEVEL " + player.getStats().getLevel()), player.getStats().getLevel(), String.format("%,d", player.getStats().getTotalXpEarned()), player.getStats().getLevel() - ((player.getStats().getLevel() == 250)?1:0), progress, player.getStats().getLevel() + ((player.getStats().getLevel() != 250)?1:0), new DecimalFormat("##.#").format(percentage)))));
        } else {
            levelHover = new ComponentBuilder(convert(highlight(String.format("%s\n\nCurrent Level: **Level %s**\nTotal EXP Earned: **%s**\n\n&3&lMAX LEVEL", String.format(chatLevelFormat, ((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')):((rank.getPrefixColor() != null)?rank.getPrefixColor():'3')) + "&l", "LEVEL " + player.getStats().getLevel()), player.getStats().getLevel(), String.format("%,d", player.getStats().getTotalXpEarned())))));
        }
        level.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, levelHover.create()));
        chatMessage.addExtra(level);

        chatMessage.addExtra(convert("&r "));

        //Adding rank prefix if it exists.
        if (rank.getPrefixAppearance() != null) {
            TextComponent prefix = new TextComponent(convert(String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase(), ((player.getActiveSubscription() != null)?String.format("&%s+&%s", player.getActiveSubscription().getColor(), rank.getPrefixColor()):""))));
            if (rank.getPrefixHoverText() != null) {
                ComponentBuilder hoverText = new ComponentBuilder(convert(rank.getPrefixHoverText()));
                if (player.getActiveSubscription() != null) {
                    if (rank == Rank.ELITE || rank == Rank.MASTER) {
                        hoverText.append(convert(String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                    } else if (rank.getPrefixHoverURL() == null) {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                        prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.net/"));
                    } else {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText().replace("\n\n&aClick to visit the store!", ""), player.getActiveSubscription().getColor())));
                    }
                } else if (rank == Rank.ELITE || rank == Rank.MASTER) {
                    hoverText.append(convert("&aClick to visit the store!"));
                }
                prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));
            }
            if (rank.getPrefixHoverURL() != null) {
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, rank.getPrefixHoverURL()));
            }

            chatMessage.addExtra(prefix);
            chatMessage.addExtra(" ");
        } else if (player.getActiveSubscription() != null) {
            TextComponent prefix = new TextComponent(convert(String.format("&%s+&r", player.getActiveSubscription().getColor())));

            ComponentBuilder hoverText = new ComponentBuilder(convert(String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));

            prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, player.getActiveSubscription().getClickURL()));

            chatMessage.addExtra(prefix);
            chatMessage.addExtra(" ");
        }


        String name;
        //Adding in name.
        if (player.getActiveDisguise() != null) {
            name = player.getActiveDisguise().getName();
        } else {
            name = player.getPlayer().getDisplayName();
        }

        //Adding in name color.
        if (player.getTeam() != null) {
            chatMessage.addExtra(new TextComponent(convert("&" + player.getTeam().getTeamColor() + name)));
        } else {
            chatMessage.addExtra(new TextComponent(convert("&" + rank.getNameColor() + name)));
        }


        //Adding in spacer.
        chatMessage.addExtra(new TextComponent(convert(" &" + rank.getConnectorColor() + "» ")));

        //Adding in actual chat message. If disguised then they should still have access to colour chat, so override the disguise rank.
        if (player.getRank().canUseColorCodes()) {
            chatMessage.addExtra(convert(highlight("&" + rank.getDefaultChatColor() + message)));
        } else {
            chatMessage.addExtra(convert("&" + rank.getDefaultChatColor()) + message);
        }

        //Returns the final result.
        return chatMessage;
    }

    public String privateMessage(String from, AuroraMCPlayer to, String message) {
        return String.format(convert(chatPrivateMessageFormat), from, ((to.getActiveDisguise() != null)?to.getActiveDisguise().getName():to.getPlayer().getName()), message);
    }

    public BaseComponent formatStaffMessage(AuroraMCPlayer sender, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        Rank rank = ((sender.isDisguised())?sender.getActiveDisguise().getRank():sender.getRank());
        textComponent.addExtra(convert(String.format(chatStaffMessageFormat, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), sender.getPlayer().getName())) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessage(AuroraMCPlayer sender, AuroraMCPlayer receiver, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        Rank rank = ((sender.isDisguised())?sender.getActiveDisguise().getRank():sender.getRank());
        Rank rank2 = ((receiver.isDisguised())?receiver.getActiveDisguise().getRank():receiver.getRank());
        textComponent.addExtra(convert(String.format(chatStaffMessageFormat, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), ((sender.isDisguised())?sender.getActiveDisguise().getName():sender.getPlayer().getName()), ((rank2 == Rank.PLAYER)?'7':rank2.getPrefixColor()), ((rank2 == Rank.PLAYER)?"Player":rank2.getPrefixAppearance()), ((receiver.isDisguised())?receiver.getActiveDisguise().getName():receiver.getPlayer().getName()))) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessage(Rank rank, String name, AuroraMCPlayer receiver, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        Rank rank2 = receiver.getRank();
        textComponent.addExtra(convert(String.format(chatStaffMessageFormat, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), name, ((rank2 == Rank.PLAYER)?'7':rank2.getPrefixColor()), ((rank2 == Rank.PLAYER)?"Player":rank2.getPrefixAppearance()), receiver.getName())) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessage(AuroraMCPlayer sender, Rank rank2, String name, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        Rank rank = sender.getRank();
        textComponent.addExtra(convert(String.format(chatStaffMessageFormat, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), sender.getName(), ((rank2 == Rank.PLAYER)?'7':rank2.getPrefixColor()), ((rank2 == Rank.PLAYER)?"Player":rank2.getPrefixAppearance()), name)) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessageFrom(AuroraMCPlayer sender, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        Rank rank = ((sender.isDisguised())?sender.getActiveDisguise().getRank():sender.getRank());
        textComponent.addExtra(convert(String.format(chatStaffMessageFormatFrom, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), ((sender.isDisguised())?sender.getActiveDisguise().getName():sender.getPlayer().getName()))) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessageFrom(Rank rank, String name, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        textComponent.addExtra(convert(String.format(chatStaffMessageFormatFrom, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), name)) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessageTo(AuroraMCPlayer receiver, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        Rank rank = ((receiver.isDisguised())?receiver.getActiveDisguise().getRank():receiver.getRank());
        textComponent.addExtra(convert(String.format(chatStaffMessageFormatTo, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), ((receiver.isDisguised())?receiver.getActiveDisguise().getName():receiver.getPlayer().getName()))) + message);
        return textComponent;
    }

    public BaseComponent formatStaffMessageTo(Rank rank, String name, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&4&l«SC»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&4&l«STAFF CHAT»\n" +
                "\n" +
                "You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);

        textComponent.addExtra(convert(String.format(chatStaffMessageFormatTo, ((rank == Rank.PLAYER)?'7':rank.getPrefixColor()), ((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()), name)) + message);
        return textComponent;
    }

    public BaseComponent formatReportMessage(PlayerReport report) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«REPORTS»");
        prefix.setColor(ChatColor.DARK_AQUA.asBungee());
        prefix.setBold(true);

        textComponent.addExtra(prefix);


        textComponent.addExtra(convert(String.format(" %s Report #%s\n" +
                "\n" +
                "&b&lSUSPECT:&r %s\n" +
                "&b&lOFFENCE:&r %s", WordUtils.capitalizeFully(report.getType().name().replace("_", " ")), report.getId(), report.getSuspectName(), report.getReason().getName())));

        if (report.getChatReportUUID() != null) {
            textComponent.addExtra(convert("\n\n&b&lCHATLOG:&r "));
            TextComponent chatLog = new TextComponent("Click here to view chatlog");
            chatLog.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://chatlogs.auroramc.net/log?uuid=%s&id=%s", report.getChatReportUUID().toString(), report.getId())));
            chatLog.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to open the chatlog for this report").color(ChatColor.GREEN.asBungee()).create()));
            textComponent.addExtra(chatLog);
        }

        return textComponent;
    }

    public BaseComponent formatIgnoreList(AuroraMCPlayer player, int page) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«IGNORE»");
        prefix.setColor(ChatColor.DARK_AQUA.asBungee());
        prefix.setBold(true);

        textComponent.addExtra(prefix);

        textComponent.addExtra(convert(highlight(String.format("You have **%s** players ignored!\n\n", player.getIgnoredPlayers().size()))));

        if (player.getIgnoredPlayers().size() > 0) {
            for (int i = 0;i < 7;i++) {
                int item = ((page - 1) * 7) + i;
                if (item >= player.getIgnoredPlayers().size()) {
                    break;
                }
                IgnoredPlayer ignoredPlayer = player.getIgnoredPlayers().get(item);
                TextComponent unignore = new TextComponent("UNIGNORE");
                unignore.setColor(net.md_5.bungee.api.ChatColor.RED);
                unignore.setBold(true);
                unignore.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to unignore this player.").color(net.md_5.bungee.api.ChatColor.RED).create()));
                unignore.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/ignore remove %s", ignoredPlayer.getName())));

                textComponent.addExtra(unignore);
                textComponent.addExtra(" " + ignoredPlayer.getName() + "\n");
            }
        } else {
            textComponent.addExtra(convert(highlight("You currently do not have anyone ignored. If you want to ignore someone, use **/ignore add <player>**.\n")));
        }

        textComponent.addExtra("\n");

        TextComponent prev = new TextComponent("«");
        prev.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
        prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/ignore list %s", ((page == 1)?1:page-1))));
        prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to go to the previous page.").color(net.md_5.bungee.api.ChatColor.WHITE).create()));
        textComponent.addExtra(prev);

        int totalPages = 1;

        if (player.getIgnoredPlayers().size() > 0) {
            totalPages = (player.getIgnoredPlayers().size() / 7) + ((player.getIgnoredPlayers().size() % 7 > 0)?1:0);
        }

        textComponent.addExtra(convert(String.format(" &3Page &b&l%s&r&3/&b&l%s&r ", page, totalPages)));

        TextComponent next = new TextComponent("»");
        next.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/ignore list %s", ((page == totalPages)?page:page+1))));
        next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to go to the next page.").color(net.md_5.bungee.api.ChatColor.WHITE).create()));
        textComponent.addExtra(next);


        return textComponent;
    }

    public String rainbow(String message) {
        List<Character> codes = new ArrayList<>(Arrays.asList('4', 'c', '6', 'e', 'a', 'b', '3', '9', 'd'));
        // Collections.shuffle(codes);
        StringBuilder finalMessage = new StringBuilder();
        int i = 0;
        boolean forward = true;
        for (char c : message.toCharArray()) {
            if (c == ' ') {
                finalMessage.append(c);
                continue;
            }
            finalMessage.append("§").append(codes.get(i)).append(c);
            if (forward) {
                i++;
            } else {
                i--;
            }
            if (i == codes.size()) {
                forward = false;
                i-=2;
            } else if (i == -1) {
                forward = true;
                i+=2;
            }
        }
        return finalMessage.toString();
    }

}
