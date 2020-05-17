package network.auroramc.core.api.utils;

import net.md_5.bungee.api.chat.*;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.UltimateSubscription;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextFormatter {

    private final String prefixFormat = "&3&l«%s» &r%s";
    private final String nonPrefixFormat = "&r%s";
    private final String chatPrefixFormat = "&%s«%s»";
    private final String chatUltimateFormat = "&%s&l%s";

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

    public String rankFormat(Rank rank) {
        if (rank.getPrefixAppearance() != null) {
            return convert(String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase()));
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

        //Adding ultimate if they have an active subscription.
        if (player.getActiveSubscription() != null || rank.hasPermission("all")) {
            UltimateSubscription subscription = player.getActiveSubscription();
            TextComponent ultimateFormatting = new TextComponent(convert(String.format(chatUltimateFormat, player.getActiveSubscription().getColor(), player.getActiveSubscription().getUltimateIcon())));
            ultimateFormatting.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(convert(String.format(subscription.getHoverText(), player.getActiveSubscription().getColor()))).create()));
            ultimateFormatting.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, subscription.getClickURL()));
            ultimateFormatting.addExtra(" ");
            chatMessage.addExtra(ultimateFormatting);
        }

        //Adding rank prefix if it exists.
        if (rank.getPrefixAppearance() != null) {
            TextComponent prefix = new TextComponent(convert(String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase())));
            if (rank.getPrefixHoverText() != null) {
                ComponentBuilder hoverText = new ComponentBuilder(convert(rank.getPrefixHoverText()));
                prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));
            }
            if (rank.getPrefixHoverURL() != null) {
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, rank.getPrefixHoverURL()));
            }

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
        TextComponent chatMessage = new TextComponent();

        //Adding ultimate if they have an active subscription.
        if (player.getActiveSubscription() != null || rank.hasPermission("all")) {
            UltimateSubscription subscription = player.getActiveSubscription();
            TextComponent component = new TextComponent(convert(String.format(chatUltimateFormat, player.getActiveSubscription().getColor(), player.getActiveSubscription().getUltimateIcon())));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(convert(String.format(subscription.getHoverText(), player.getActiveSubscription().getColor()))).create()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, subscription.getClickURL()));
            component.addExtra(" ");
            chatMessage.addExtra(component);
        }

        //Adding rank prefix if it exists.
        if (rank.getPrefixAppearance() != null) {
            TextComponent prefix = new TextComponent(convert(String.format(chatPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase())));
            if (rank.getPrefixHoverText() != null) {
                ComponentBuilder hoverText = new ComponentBuilder(convert(rank.getPrefixHoverText()));
                prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));
            }
            if (rank.getPrefixHoverURL() != null) {
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, rank.getPrefixHoverURL()));
            }

            chatMessage.addExtra(prefix);
            chatMessage.addExtra(" ");
        }

        //Adding in name color.
        if (player.getTeam() != null) {
            chatMessage.addExtra(new TextComponent(convert("&" + player.getTeam().getTeamColor())));
        } else {
            chatMessage.addExtra(new TextComponent(convert("&" + rank.getNameColor())));
        }

        //Adding in name.
        chatMessage.addExtra(player.getPlayer().getDisplayName());


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

}
