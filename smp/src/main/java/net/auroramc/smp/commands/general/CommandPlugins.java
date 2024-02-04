/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general;

import net.auroramc.api.permissions.Permission;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPlugins extends ServerCommand {

    public String convert(@NotNull String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public CommandPlugins() {
        super("plugins", Collections.singletonList("pl"), Collections.singletonList(Permission.PLAYER), false, "");
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        TextComponent textComponent = new TextComponent("");
        TextComponent prefix = new TextComponent("«HELP»");
        prefix.setColor(ChatColor.DARK_AQUA);
        prefix.setBold(true);

        textComponent.addExtra(prefix);

        textComponent.addExtra(convert(" AuroraMC is an advanced custom-coded network that utilises many custom built plugins" +
                " and services to allow our network to operate. As you seem to be a curious person, we thought we'd tell you the plugins we've built" +
                " and what they do! To view more information on each of these, hover over the names below.\n\n"));

        TextComponent arrow = new TextComponent(" ➤ ");
        arrow.setColor(ChatColor.DARK_AQUA);

        textComponent.addExtra(arrow);

        TextComponent component = new TextComponent("AuroraMC-Core\n");
        component.setColor(ChatColor.AQUA);
        ComponentBuilder componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Core\n"
                + "\n"
                + WordUtils.wrap("&fOur core contains all of the core systems required by the entirety of the network and our core API handling" +
                " things like player connections, chat, filter, punishments etc.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Proxy\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Proxy\n"
                + "\n"
                + WordUtils.wrap("&fSimilarly to our actual core, it contains all the core systems related to connection nodes." +
                " A significant portion of the core and proxy core codebase is similar if not the same.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Lobby\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Lobby\n"
                + "\n"
                + WordUtils.wrap("&fOur Lobby plugin contains all functionality needed for lobby servers only, such as loading in the lobby map, " +
                " lobby games, opening crates and more.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Game-Engine\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Game-Engine\n"
                + "\n"
                + WordUtils.wrap("&fThe game engine deals with how games and maps are loaded, how games are" +
                " started, and the core API for games to use.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Games\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Games\n"
                + "\n"
                + WordUtils.wrap("&fThis module contains the code for all of the games that are available to play" +
                " on AuroraMC (including previous/removed games).", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Duels\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Duels\n"
                + "\n"
                + WordUtils.wrap("&fOur Duels plugin hosts the Duels game mode, as this is a separate" +
                " type of server and is handled differently from our normal games.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Events\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Events\n"
                + "\n"
                + WordUtils.wrap("&fThe Event Core manages all of the commands, special games and more that are utilised by our Events Team.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent("AuroraMC-Build\n");
        component.setColor(ChatColor.AQUA);
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC-Build\n"
                + "\n"
                + WordUtils.wrap("&fThis is the core for our internal Build server which" +
                " manages how maps are pushed to the network, in-progress builds etc.", 40, "\n&r", false)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);

        component = new TextComponent("\nTo view more information on how the network functions, click here to visit our \"How AuroraMC Works\" thread!");
        component.setColor(ChatColor.WHITE);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/how-auroramc-works/"));
        textComponent.addExtra(component);

        player.sendMessage(textComponent);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
