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
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see how AuroraMC works!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" AuroraMC Rules\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/rules"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see our rules!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" AuroraMC Store\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.net/"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to visit our store!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Submit a Support Ticket\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/support/"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to submit a support ticket!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Submit an appeal\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/appeal/"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to submit an appeal!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Report an issue with our systems\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/bug-report/"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to report an issue with our systems!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Report a Rule Breaker\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/report-info/"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see information on how to report rule breakers!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);

        component = new TextComponent(" \nIf you have a question that isn't addressed in the above links, please check our Knowledgebase!\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/knowledgebase"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to go to our knowledgebase!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(component);

        component = new TextComponent("\nFor further assistance, contact online staff using /s!");
        component.setColor(ChatColor.AQUA);
        component.setBold(true);

        textComponent.addExtra(component);

        player.getPlayer().spigot().sendMessage(textComponent);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
