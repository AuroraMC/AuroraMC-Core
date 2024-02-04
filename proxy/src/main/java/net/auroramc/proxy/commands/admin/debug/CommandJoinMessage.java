/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.debug;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandJoinMessage extends ProxyCommand {


    public CommandJoinMessage() {
        super("joinmessage", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        TextComponent component = new TextComponent("");
        TextComponent welcome = new TextComponent("Welcome to the AuroraMC Network!");
        welcome.setBold(true);
        welcome.setColor(ChatColor.AQUA);
        welcome.setUnderlined(true);

        component.addExtra(welcome);

        TextComponent joinUs = new TextComponent(" We're glad you've decided to join us!\n\nBy playing on AuroraMC, you confirm you will comply with our ");
        joinUs.setUnderlined(false);
        joinUs.setBold(false);
        joinUs.setColor(ChatColor.RESET);
        component.addExtra(joinUs);


        TextComponent rules = new TextComponent("Rules");
        rules.setColor(ChatColor.AQUA);
        rules.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append("Click here to view our rules!").color(ChatColor.AQUA).bold(true).create()));
        rules.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/rules"));
        component.addExtra(rules);

        component.addExtra(", ");

        TextComponent terms = new TextComponent("Terms of Service");
        terms.setColor(ChatColor.AQUA);
        terms.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append("Click here to view our Terms of Service!").color(ChatColor.AQUA).bold(true).create()));
        terms.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/terms"));
        component.addExtra(terms);

        component.addExtra(", and ");

        TextComponent privacy = new TextComponent("Privacy Policies");
        privacy.setColor(ChatColor.AQUA);
        privacy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append("Click here to view our Privacy Policy!").color(ChatColor.AQUA).bold(true).create()));
        privacy.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/privacy"));
        component.addExtra(privacy);

        component.addExtra(". For more information on our policies, please click on the applicable text above.\n\n" +
                "To get started, either click on the compass and choose a game, or click on the Tutorial NPC!\n\n" +
                "Happy Gaming!\n");

        TextComponent leadership = new TextComponent("~The AuroraMC Leadership Team");
        leadership.setColor(ChatColor.RED);
        leadership.setBold(true);
        leadership.setUnderlined(true);
        component.addExtra(leadership);

        player.sendMessage(component);
        Title title = ProxyServer.getInstance().createTitle();
        title.fadeIn(10);
        title.stay(100);
        title.fadeOut(10);
        title.title(new ComponentBuilder("WELCOME TO AURORAMC!").bold(true).color(ChatColor.AQUA).create());
        title.subTitle(new TextComponent("We're glad you've decided to join us!"));
        player.sendTitle(title);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}