/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.utils;

import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.IgnoredPlayer;
import net.auroramc.api.player.PlayerReport;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextFormatter {

    private static final String tabPrefixFormat = "%s&l«%s%s»";

    public static BaseComponent highlight(@NotNull String message) {
        List<String> toHighlight = new ArrayList<>(Arrays.asList(message.split("[*]{2}")));
        TextComponent component = new TextComponent("");
        component.setColor(ChatColor.WHITE);
        for (int i = 0;i<toHighlight.size();i++) {
            if (i % 2 == 1) {
                TextComponent highlighted = new TextComponent(toHighlight.get(i));
                highlighted.setColor(ChatColor.AQUA);
                highlighted.setBold(false);
                component.addExtra(highlighted);
            } else {
                TextComponent normal = new TextComponent(toHighlight.get(i));
                normal.setColor(ChatColor.WHITE);
                normal.setBold(false);
                component.addExtra(normal);
            }
        }

        return component;
    }

    public static String highlightRaw(@NotNull String message) {
        List<String> toHighlight = new ArrayList<>(Arrays.asList(message.split("[*]{2}")));
        StringBuilder highightedMessage = new StringBuilder();
        for (int i = 0;i<toHighlight.size();i++) {
            if (i % 2 == 1) {
                highightedMessage.append("&b").append(toHighlight.get(i)).append("&r");
            } else {
                highightedMessage.append(toHighlight.get(i));
            }
        }

        return highightedMessage.toString();
    }

    public static BaseComponent pluginMessage(@Nullable String prefix, @NotNull String message) {
        if (prefix == null || prefix.equals("")) {
            return highlight(message);
        } else {
            TextComponent component = new TextComponent("");
            component.setColor(ChatColor.WHITE);
            TextComponent pref = new TextComponent(String.format("«%s»", prefix.toUpperCase()));
            pref.setColor(ChatColor.DARK_AQUA);
            pref.setBold(true);

            component.addExtra(pref);
            component.addExtra(new TextComponent(" "));
            component.addExtra(highlight(message));
            return component;
        }
    }

    public static BaseComponent pluginMessage(@NotNull String prefix, @NotNull BaseComponent message) {
        TextComponent component = new TextComponent("");
        component.setColor(ChatColor.WHITE);
        TextComponent pref = new TextComponent(String.format("«%s»", prefix.toUpperCase()));
        pref.setColor(ChatColor.DARK_AQUA);
        pref.setBold(true);

        component.addExtra(pref);
        component.addExtra(" ");
        component.addExtra(message);
        return component;

    }

    public static String pluginMessageRaw(@Nullable String prefix, @NotNull String message) {
        return convert((prefix == null || prefix.equals(""))?highlightRaw(String.format("&r%s", message)):highlightRaw(String.format("&3&l«%s» &r%s", prefix.toUpperCase(), message)));
    }

    public static String convert(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String rankFormat(Rank rank, PlusSubscription subscription) {
        if (rank.getPrefixAppearance() != null) {
            return convert(String.format(tabPrefixFormat, rank.getPrefixColor(), rank.getPrefixAppearance().toUpperCase(), ((subscription != null && rank.getId() < 5)?"+":"")));
        } else {
            return ((subscription != null && rank.getId() < 5)?((rank.getPrefixColor()!=null)?rank.getPrefixColor():ChatColor.GRAY) + "+":"");
        }
    }

    public static BaseComponent chatMessage(@NotNull AuroraMCPlayer player, AuroraMCPlayer recipient, @NotNull BaseComponent message) {
        Rank rank = player.getRank();
        if (player.getActiveDisguise() != null && !(player.equals(recipient) && player.getPreferences().isHideDisguiseNameEnabled())) {
            rank = player.getActiveDisguise().getRank();
        }
        TextComponent chatMessage = new TextComponent("");

        TextComponent level = new TextComponent("«" + player.getStats().getLevel() + "»");
        level.setColor(((player.getActiveSubscription() != null)?((player.getActiveSubscription().getLevelColor() != null)?player.getActiveSubscription().getLevelColor():((rank.getPrefixColor() != null)?rank.getPrefixColor():ChatColor.DARK_AQUA)):((rank.getPrefixColor() != null)?rank.getPrefixColor():ChatColor.DARK_AQUA)));

        TextComponent levelHover;
        if (player.getStats().getLevel() != 250) {
            String progress = "||||||||||||||||||||||||||||||";
            TextComponent prog = new TextComponent("");
            prog.setColor(ChatColor.WHITE);
            double percentage = (((double) player.getStats().getXpIntoLevel() / LevelUtils.xpForLevel(player.getStats().getLevel() + 1))*100);
            if (player.getStats().getLevel() != 250) {
                int amountToColour = (int) Math.floor(((percentage) / 100)*30);
                TextComponent compl = new TextComponent(progress.substring(0, amountToColour));
                compl.setColor(ChatColor.AQUA);
                compl.setBold(true);
                prog.addExtra(compl);
                TextComponent togo = new TextComponent(progress.substring(amountToColour + 1));
                togo.setColor(ChatColor.WHITE);
                togo.setBold(true);
                prog.addExtra(togo);
            } else {
                percentage = 100.0;
            }

            levelHover = new TextComponent("");
            TextComponent lvl = new TextComponent("«LEVEL " + player.getStats().getLevel() + "»");
            lvl.setColor(level.getColor());
            lvl.setBold(true);
            levelHover.addExtra(lvl);
            levelHover.addExtra("\n \n");

            TextComponent cmp = new TextComponent("Current Level: ");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            levelHover.addExtra(cmp);

            cmp = new TextComponent("Level " + player.getStats().getLevel());
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(false);
            levelHover.addExtra(cmp);
            levelHover.addExtra("\n");

            cmp = new TextComponent("Total EXP Earned: ");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            levelHover.addExtra(cmp);

            cmp = new TextComponent(String.valueOf(player.getStats().getTotalXpEarned()));
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(false);
            levelHover.addExtra(cmp);
            levelHover.addExtra("\n \n");

            cmp = new TextComponent(String.format("«%s»", player.getStats().getLevel() - ((player.getStats().getLevel() == 250)?1:0)));
            cmp.setColor(ChatColor.DARK_AQUA);
            cmp.setBold(true);
            levelHover.addExtra(cmp);
            levelHover.addExtra(" ");

            levelHover.addExtra(prog);
            levelHover.addExtra(" ");

            cmp = new TextComponent(String.format("«%s»", player.getStats().getLevel() + ((player.getStats().getLevel() != 250)?1:0)));
            cmp.setColor(ChatColor.DARK_AQUA);
            cmp.setBold(true);
            levelHover.addExtra(cmp);
            levelHover.addExtra("\n");

            cmp = new TextComponent("Progress to Next Level: ");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            levelHover.addExtra(cmp);

            cmp = new TextComponent(new DecimalFormat("##.#").format(percentage) + "%");
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(false);
            levelHover.addExtra(cmp);
        } else {
            levelHover = new TextComponent("");
            TextComponent lvl = new TextComponent("«LEVEL " + player.getStats().getLevel() + "»");
            lvl.setColor(level.getColor());
            lvl.setBold(true);
            levelHover.addExtra(lvl);
            levelHover.addExtra("\n \n");

            TextComponent cmp = new TextComponent("Current Level: ");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            levelHover.addExtra(cmp);

            cmp = new TextComponent("Level " + player.getStats().getLevel());
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(false);
            levelHover.addExtra(cmp);
            levelHover.addExtra("\n");

            cmp = new TextComponent("Total EXP Earned: ");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            levelHover.addExtra(cmp);

            cmp = new TextComponent(String.valueOf(player.getStats().getTotalXpEarned()));
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(false);
            levelHover.addExtra(cmp);
            levelHover.addExtra("\n \n");

            cmp = new TextComponent(String.format("«%s»", player.getStats().getLevel() - ((player.getStats().getLevel() == 250)?1:0)));
            cmp.setColor(ChatColor.DARK_AQUA);
            cmp.setBold(true);
            levelHover.addExtra(cmp);
            levelHover.addExtra(" ");

            cmp = new TextComponent("||||||||||||||||||||||||||||||");
            cmp.setColor(ChatColor.AQUA);
            cmp.setBold(true);
            levelHover.addExtra(cmp);
            levelHover.addExtra(" ");

            cmp = new TextComponent(String.format("«%s»", player.getStats().getLevel() + ((player.getStats().getLevel() != 250)?1:0)));
            cmp.setColor(ChatColor.DARK_AQUA);
            cmp.setBold(true);
            levelHover.addExtra(cmp);
            levelHover.addExtra("\n");

            cmp = new TextComponent("MAX LEVEL");
            cmp.setColor(ChatColor.DARK_AQUA);
            cmp.setBold(true);
            levelHover.addExtra(cmp);

        }
        level.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{levelHover}));
        chatMessage.addExtra(level);

        chatMessage.addExtra(" ");

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

    public static BaseComponent chatMessage(@NotNull String name, @NotNull Rank rank, @NotNull BaseComponent message) {
        TextComponent chatMessage = new TextComponent("");

        TextComponent global = new TextComponent(convert("&4&l«GLOBAL»"));
        global.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("This is a global message sent across all servers on the network.").color(ChatColor.RED).create()));
        chatMessage.addExtra(global);

        chatMessage.addExtra(convert("&r "));

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

            cmp = new TextComponent("»");
            cmp.setColor(rank.getPrefixColor());
            cmp.setBold(true);
            prefix.addExtra(cmp);

            prefix.addExtra(" ");



            if (rank.getPrefixHoverText() != null) {
                ComponentBuilder hoverText = new ComponentBuilder(rank.getPrefixHoverText());
                prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));
            }
            if (rank.getPrefixHoverURL() != null) {
                prefix.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, rank.getPrefixHoverURL()));
            }

            chatMessage.addExtra(prefix);
        }

        TextComponent nameComponent = new TextComponent(name);
        nameComponent.setColor(rank.getNameColor());
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

    public static BaseComponent formatTeamChat(AuroraMCPlayer sender, AuroraMCPlayer receiver, BaseComponent message) {
        TextComponent textComponent = new TextComponent("");

        TextComponent prefix = new TextComponent("«TEAM»");
        prefix.setColor(ChatColor.DARK_PURPLE);
        prefix.setBold(true);


        TextComponent prefixHover = new TextComponent("");
        TextComponent cmp = new TextComponent("«TEAM CHAT»");
        cmp.setColor(ChatColor.DARK_PURPLE);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);

        cmp = new TextComponent("\n \nYou can use this chat to communicate\n" +
                "with people in your team!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);

        Rank rank = sender.getRank();

        if (sender.isDisguised() && !(sender.equals(receiver) && sender.getPreferences().isHideDisguiseNameEnabled())) {
            rank = sender.getActiveDisguise().getRank();
        }

        textComponent.addExtra(" ");

        cmp = new TextComponent("&r&%s%s ");
        cmp.setColor(((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor()));
        cmp.setBold(false);
        textComponent.addExtra(cmp);

        cmp = new TextComponent("» ");
        cmp.setColor(((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor()));
        cmp.setBold(true);
        textComponent.addExtra(cmp);


        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent privateMessage(AuroraMCPlayer from, AuroraMCPlayer to, boolean isSender, BaseComponent message) {
        TextComponent component = new TextComponent("");

        TextComponent cmp = new TextComponent(((!from.isDisguised() || (from.getPreferences().isHideDisguiseNameEnabled() && isSender))?from.getName():from.getActiveDisguise().getName()));
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent("➜");
        cmp.setColor(ChatColor.DARK_AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent(((!to.isDisguised() || (to.getPreferences().isHideDisguiseNameEnabled() && !isSender))?to.getName():to.getActiveDisguise().getName()));
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor(ChatColor.DARK_AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        component.addExtra(message);

        return component;
    }

    public static BaseComponent privateMessage(String from, AuroraMCPlayer to, BaseComponent message) {
        TextComponent component = new TextComponent("");

        TextComponent cmp = new TextComponent(from);
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent("➜");
        cmp.setColor(ChatColor.DARK_AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent(((!to.isDisguised() || to.getPreferences().isHideDisguiseNameEnabled())?to.getName():to.getActiveDisguise().getName()));
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor(ChatColor.DARK_AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        component.addExtra(message);

        return component;
    }

    public static BaseComponent privateMessage(AuroraMCPlayer from, String to, BaseComponent message) {
        TextComponent component = new TextComponent("");

        TextComponent cmp = new TextComponent(((!from.isDisguised() || from.getPreferences().isHideDisguiseNameEnabled())?from.getName():from.getActiveDisguise().getName()));
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent("➜");
        cmp.setColor(ChatColor.DARK_AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent(to);
        cmp.setColor(ChatColor.AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor(ChatColor.DARK_AQUA);
        cmp.setBold(true);
        component.addExtra(cmp);
        component.addExtra(" ");

        component.addExtra(message);

        return component;
    }

    public static BaseComponent formatStaffMessage(AuroraMCPlayer sender, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));

        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        Rank rank = ((sender.isDisguised() && !sender.getPreferences().isHideDisguiseNameEnabled())?sender.getActiveDisguise().getRank():sender.getRank());
        String name = ((sender.isDisguised() && !sender.getPreferences().isHideDisguiseNameEnabled())?sender.getActiveDisguise().getName():sender.getName());

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessage(AuroraMCPlayer sender, AuroraMCPlayer receiver, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        Rank rank = ((sender.isDisguised())?sender.getActiveDisguise().getRank():sender.getRank());
        String name = ((sender.isDisguised())?sender.getActiveDisguise().getName():sender.getName());

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("➜");
        cmp.setColor(ChatColor.DARK_RED );
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");



        Rank rank2 = ((receiver.isDisguised())?receiver.getActiveDisguise().getRank():receiver.getRank());
        String name2 = ((receiver.isDisguised())?receiver.getActiveDisguise().getName():receiver.getName());

        cmp = new TextComponent(((rank2 == Rank.PLAYER)?"Player":rank2.getPrefixAppearance()) + " " + name2);
        cmp.setColor((rank2 == Rank.PLAYER)?ChatColor.GRAY:rank2.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank2 == Rank.PLAYER)?ChatColor.GRAY:rank2.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");


        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessage(Rank rank, String name, AuroraMCPlayer receiver, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("➜");
        cmp.setColor(ChatColor.DARK_RED );
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");



        Rank rank2 = ((receiver.isDisguised() && !(receiver.getPreferences().isHideDisguiseNameEnabled()))?receiver.getActiveDisguise().getRank():receiver.getRank());
        String name2 = ((receiver.isDisguised() && !(receiver.getPreferences().isHideDisguiseNameEnabled()))?receiver.getActiveDisguise().getName():receiver.getName());

        cmp = new TextComponent(((rank2 == Rank.PLAYER)?"Player":rank2.getPrefixAppearance()) + " " + name2);
        cmp.setColor((rank2 == Rank.PLAYER)?ChatColor.GRAY:rank2.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank2 == Rank.PLAYER)?ChatColor.GRAY:rank2.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");


        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessage(AuroraMCPlayer sender, Rank rank2, String name2, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        Rank rank = ((sender.isDisguised() && !(sender.getPreferences().isHideDisguiseNameEnabled()))?sender.getActiveDisguise().getRank():sender.getRank());
        String name = ((sender.isDisguised() && !(sender.getPreferences().isHideDisguiseNameEnabled()))?sender.getActiveDisguise().getName():sender.getName());

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("➜");
        cmp.setColor(ChatColor.DARK_RED );
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");




        cmp = new TextComponent(((rank2 == Rank.PLAYER)?"Player":rank2.getPrefixAppearance()) + " " + name2);
        cmp.setColor((rank2 == Rank.PLAYER)?ChatColor.GRAY:rank2.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank2 == Rank.PLAYER)?ChatColor.GRAY:rank2.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");


        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessageFrom(AuroraMCPlayer sender, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        cmp = new TextComponent("From: ");
        cmp.setColor(ChatColor.RED);
        cmp.setBold(false);
        textComponent.addExtra(cmp);

        Rank rank = ((sender.isDisguised())?sender.getActiveDisguise().getRank():sender.getRank());
        String name = ((sender.isDisguised())?sender.getActiveDisguise().getName():sender.getName());

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessageFrom(Rank rank, String name, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        cmp = new TextComponent("From: ");
        cmp.setColor(ChatColor.RED);
        cmp.setBold(false);
        textComponent.addExtra(cmp);

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessageTo(AuroraMCPlayer receiver, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        cmp = new TextComponent("To: ");
        cmp.setColor(ChatColor.RED);
        cmp.setBold(false);
        textComponent.addExtra(cmp);

        Rank rank = ((receiver.isDisguised())?receiver.getActiveDisguise().getRank():receiver.getRank());
        String name = ((receiver.isDisguised())?receiver.getActiveDisguise().getName():receiver.getName());

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatStaffMessageTo(Rank rank, String name, String message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«SC»");
        prefix.setBold(true);
        prefix.setColor(ChatColor.DARK_RED);

        TextComponent prefixHover = new TextComponent("");

        TextComponent cmp = new TextComponent("«STAFF CHAT»");
        cmp.setColor(ChatColor.DARK_RED);
        cmp.setBold(true);
        prefixHover.addExtra(cmp);
        prefixHover.addExtra("\n \n");

        cmp = new TextComponent("You can use this chat to get help\n" +
                "from our Moderation staff! You can\n" +
                "ask questions, or request help with\n" +
                "rule breakers!");
        cmp.setColor(ChatColor.WHITE);
        cmp.setBold(false);
        prefixHover.addExtra(cmp);

        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prefixHover}));
        textComponent.addExtra(prefix);
        textComponent.addExtra(" ");

        cmp = new TextComponent("To: ");
        cmp.setColor(ChatColor.RED);
        cmp.setBold(false);
        textComponent.addExtra(cmp);

        cmp = new TextComponent(((rank == Rank.PLAYER)?"Player":rank.getPrefixAppearance()) + " " + name);
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(false);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        cmp = new TextComponent("»");
        cmp.setColor((rank == Rank.PLAYER)?ChatColor.GRAY:rank.getPrefixColor());
        cmp.setBold(true);
        textComponent.addExtra(cmp);
        textComponent.addExtra(" ");

        textComponent.addExtra(message);
        return textComponent;
    }

    public static BaseComponent formatReportMessage(PlayerReport report) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«REPORTS» ");
        prefix.setColor(ChatColor.DARK_AQUA);
        prefix.setBold(true);

        textComponent.addExtra(prefix);

        TextComponent component = new TextComponent(WordUtils.capitalizeFully(report.getType().name().replace("_", " ")) + " Report #" + report.getId() + "\n\n");
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);

        component = new TextComponent("SUSPECT:");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);
        textComponent.addExtra(component);

        component = new TextComponent(" " + report.getSuspectName() + "\n");
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);

        component = new TextComponent("OFFENCE:");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);
        textComponent.addExtra(component);

        component = new TextComponent(" " + report.getReason().getName());
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);



        if (report.getChatReportUUID() != null) {
            component = new TextComponent("\n\nCHATLOG: ");
            component.setColor(ChatColor.AQUA);
            component.setBold(true);
            textComponent.addExtra(component);
            TextComponent chatLog = new TextComponent("Click here to view chatlog");
            chatLog.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://chatlogs.auroramc.net/log?uuid=%s&id=%s", report.getChatReportUUID().toString(), report.getId())));
            chatLog.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to open the chatlog for this report").color(ChatColor.GREEN).create()));
            textComponent.addExtra(chatLog);
        }

        return textComponent;
    }

    public static BaseComponent formatReportInfoMessage(PlayerReport report) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«REPORTS» ");
        prefix.setColor(ChatColor.DARK_AQUA);
        prefix.setBold(true);

        textComponent.addExtra(prefix);

        TextComponent component = new TextComponent(WordUtils.capitalizeFully(report.getType().name().replace("_", " ")) + " Report #" + report.getId() + "\n\n");
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);

        component = new TextComponent("SUSPECT:");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);
        textComponent.addExtra(component);

        component = new TextComponent(" " + report.getSuspectName() + "\n");
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);

        component = new TextComponent("OFFENCE:");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);
        textComponent.addExtra(component);

        component = new TextComponent(" " + report.getReason().getName() + "\n \n");
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);

        component = new TextComponent("HANDLER:");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);
        textComponent.addExtra(component);

        component = new TextComponent(" " + ((report.getHandlerName() != null)?report.getHandlerName():"Unassigned") + "\n");
        component.setColor(ChatColor.WHITE);
        component.setBold(false);
        textComponent.addExtra(component);

        component = new TextComponent("OUTCOME:");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);
        textComponent.addExtra(component);

        component = new TextComponent(" " + ((report.getOutcome() == PlayerReport.ReportOutcome.PENDING)?"Pending":((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"Accepted":"Denied")));
        component.setColor(((report.getOutcome() == PlayerReport.ReportOutcome.PENDING)?ChatColor.GOLD:((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?ChatColor.GREEN:ChatColor.RED)));
        component.setBold(false);
        textComponent.addExtra(component);

        if (report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED && report.getType() != PlayerReport.ReportType.INAPPROPRIATE_NAME && report.getReason() != report.getReasonAccepted()) {
            component = new TextComponent("\nREASON ACCEPTED:");
            component.setColor(ChatColor.AQUA);
            component.setBold(true);
            textComponent.addExtra(component);

            component = new TextComponent(" " + report.getReasonAccepted().getName());
            component.setColor(ChatColor.WHITE);
            component.setBold(false);
            textComponent.addExtra(component);
        }



        if (report.getChatReportUUID() != null) {
            component = new TextComponent("\n\nCHATLOG: ");
            component.setColor(ChatColor.AQUA);
            component.setBold(true);
            textComponent.addExtra(component);
            TextComponent chatLog = new TextComponent("Click here to view chatlog");
            chatLog.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://chatlogs.auroramc.net/log?uuid=%s&id=%s", report.getChatReportUUID().toString(), report.getId())));
            chatLog.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to open the chatlog for this report").color(ChatColor.GREEN).create()));
            textComponent.addExtra(chatLog);
        }

        return textComponent;
    }

    public static BaseComponent formatIgnoreList(AuroraMCPlayer player, int page) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«IGNORE»");
        prefix.setColor(ChatColor.DARK_AQUA);
        prefix.setBold(true);

        textComponent.addExtra(prefix);

        textComponent.addExtra(highlight(String.format(" You have **%s** players ignored!\n\n", player.getIgnoredPlayers().size())));

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
            textComponent.addExtra(highlight("You currently do not have anyone ignored. If you want to ignore someone, use **/ignore add <player>**.\n"));
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

    public static BaseComponent rainbow(String message) {
        List<ChatColor> codes = new ArrayList<>(Arrays.asList(ChatColor.DARK_RED, ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.BLUE, ChatColor.LIGHT_PURPLE));
        // Collections.shuffle(codes);
        TextComponent finalMessage = new TextComponent("");
        int i = 0;
        boolean forward = true;
        for (char c : message.toCharArray()) {
            if (c == ' ') {
                finalMessage.addExtra(String.valueOf(c));
                continue;
            }
            TextComponent component = new TextComponent(String.valueOf(c));
            component.setColor(codes.get(i));
            finalMessage.addExtra(component);
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
        return finalMessage;
    }
    public static BaseComponent rainbowBold(String message) {
        List<ChatColor> codes = new ArrayList<>(Arrays.asList(ChatColor.DARK_RED, ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.BLUE, ChatColor.LIGHT_PURPLE));
        // Collections.shuffle(codes);
        TextComponent finalMessage = new TextComponent("");
        int i = 0;
        boolean forward = true;
        for (char c : message.toCharArray()) {
            if (c == ' ') {
                finalMessage.addExtra(String.valueOf(c));
                continue;
            }
            TextComponent component = new TextComponent(String.valueOf(c));
            component.setColor(codes.get(i));
            component.setBold(true);
            finalMessage.addExtra(component);
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
        return finalMessage;
    }

    public static BaseComponent formatPartyChat(Rank rank, String sender, BaseComponent message) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent(convert("&6&l«PARTY»"));
        ComponentBuilder prefixHover = new ComponentBuilder(convert("&6&l«PARTY CHAT»\n" +
                "\n" +
                "You can use this chat to communicate\n" +
                "with people in your party!"));
        prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHover.create()));

        textComponent.addExtra(prefix);
        textComponent.addExtra(convert(String.format(" %s%s &l»&r ", ((rank == Rank.PLAYER)?"§7":rank.getPrefixColor()), sender)));
        textComponent.addExtra(message);
        return textComponent;
    }

}
