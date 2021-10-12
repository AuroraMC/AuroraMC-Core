/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandHelp extends Command {

    public String convert(@NotNull String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public CommandHelp() {
        super("help", Arrays.asList("help", "helpme", "helpmeorsiawillcomeandmakeyouthegreatest", "helpmeorelseillkillyou", "helpwouldbeappreciated"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«HELP»");
        prefix.setColor(ChatColor.DARK_AQUA);
        prefix.setBold(true);

        textComponent.addExtra(prefix);

        textComponent.addExtra(convert(" Click to be redirected to a help option.\n\n"));

        TextComponent arrow = new TextComponent(" ➤");
        arrow.setColor(ChatColor.DARK_AQUA);

        textComponent.addExtra(arrow);

        TextComponent component = new TextComponent(" How AuroraMC works\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/how-auroramc-works/"));
        ComponentBuilder componentHover = new ComponentBuilder(convert("&3&lHow AuroraMC works\n"
        + "\n"
        + WordUtils.wrap("To cater to those who are curious as well as stay as transparent as possible," +
                " we've put this document together to give you an insight into how the network operates," +
                " how we create updates and the tools we use in order to run the network.", 40, "\n&r", false)
        + "\n\n&aClick here to view this document."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" AuroraMC Rules\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/rules"));
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC Rules\n"
                + "\n"
                + WordUtils.wrap("Our rules are created to ensure that all players are able to have a good experience on the network." +
                " Failure to follow these rules will result in punishments given at the discretion of our Moderation Team.", 40, "\n&r", false)
                + "\n\n&aClick here to view the rules."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" AuroraMC Store\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.net/"));
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC Store\n"
                + "\n"
                + WordUtils.wrap("", 40, "\n&r", false)
                + "\n\n&aClick here to visit our store."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Submit a Support Ticket\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/support/"));
        componentHover = new ComponentBuilder(convert("&3&lSubmit a Support Ticket\n"
                + "\n"
                + WordUtils.wrap("", 40, "\n&r", false)
                + "\n\n&aClick here to submit a support ticket."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Submit an appeal\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/appeal/"));
        componentHover = new ComponentBuilder(convert("&3&lSubmit an appeal\n"
                + "\n"
                + WordUtils.wrap("If you believe you were unfairly punished" +
                " or you would like a second chance, you can submit an appeal and" +
                " a member of our team will look into your punishment.", 40, "\n&r", false)
                + "\n\n&aClick here to submit an appeal."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Report an issue with our systems\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/bug-report/"));
        componentHover = new ComponentBuilder(convert("&3&lReport an issue with our systems\n"
                + "\n"
                + WordUtils.wrap("If you believe you have found an issue with our systems," +
                " you are able to send in a bug report and a member of" +
                " our Quality Assurance will look into it as soon as possible." +
                " If you are the first person to successfully report a game breaking bug," +
                " you will receive a special cosmetic from the Leadership Team.", 40, "\n&r", false)
                + "\n\n&aClick here to report an issue with our systems."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Report a Rule Breaker\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/report-info/"));
        componentHover = new ComponentBuilder(convert("&3&lReport a Rule Breaker\n"
                + "\n"
                + WordUtils.wrap("", 40, "\n&r", false)
                + "\n\n&aClick here to report a Rule Breaker."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);

        component = new TextComponent(" \nIf you have a question that isn't addressed in the above links, please check our Knowledgebase!\n");
        component.setColor(ChatColor.WHITE);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/knowledgebase"));
        componentHover = new ComponentBuilder(convert("&3&lKnowledgebase\n"
                + "\n"
                + WordUtils.wrap("Our knowledgebase consists of answers to a majority of the most frequently" +
                " asked questions on the network. If you cannot find the answer" +
                " to your question using one of the above links, it may be in the knowledgebase!", 40, "\n&r", false)
                + "\n\n&aClick here to visit our knowledgebase."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);

        component = new TextComponent("\nFor further assistance, contact staff in your lobby using /s!");
        component.setColor(ChatColor.AQUA);

        textComponent.addExtra(component);

        player.getPlayer().spigot().sendMessage(textComponent);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}