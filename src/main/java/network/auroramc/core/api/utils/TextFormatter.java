package network.auroramc.core.api.utils;

import net.md_5.bungee.api.chat.*;
import network.auroramc.core.api.permissions.PlusSubscription;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.permissions.ranks.Elite;
import network.auroramc.core.permissions.ranks.Master;
import network.auroramc.core.permissions.ranks.Player;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextFormatter {

    private final String prefixFormat = "&3&l«%s» &r%s";
    private final String nonPrefixFormat = "&r%s";
    private final String chatPrefixFormat = "&%s«%s%s»";
    private final String chatLevelFormat = "&%s«%s»";
    private final String chatUltimateFormat = "&%s&l%s";
    private final String chatStaffMessageFormat = " &r&%s%s %s &l»&r ";

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
                    if (rank instanceof Elite || rank instanceof Master) {
                        hoverText.append(convert(String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                    } else if (rank.getPrefixHoverURL() == null) {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                        prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.block2block.me/"));
                    } else {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText().replace("\n\n&aClick to visit the store!", ""), player.getActiveSubscription().getColor())));
                    }
                } else if (rank instanceof Elite || rank instanceof Master) {
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

    public BaseComponent undisguisedChatMessage(@NotNull AuroraMCPlayer player, @NotNull String message) {
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
                    if (rank instanceof Elite || rank instanceof Master) {
                        hoverText.append(convert(String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                    } else if (rank.getPrefixHoverURL() == null) {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText(), player.getActiveSubscription().getColor())));
                        prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.block2block.me/"));
                    } else {
                        hoverText.append(convert("\n\n" + String.format(player.getActiveSubscription().getHoverText().replace("\n\n&aClick to visit the store!", ""), player.getActiveSubscription().getColor())));
                    }
                } else if (rank instanceof Elite || rank instanceof Master) {
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


        String name = player.getName();

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
        textComponent.addExtra(convert(String.format(chatStaffMessageFormat, ((rank instanceof Player)?'7':rank.getPrefixColor()), ((rank instanceof Player)?"Player":rank.getPrefixAppearance()), sender.getPlayer().getName())) + message);
        return textComponent;
    }

}
