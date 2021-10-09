package net.auroramc.core.commands.general;

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

        textComponent.addExtra(convert(String.format(" Click to be redirected to a help option.\n\n")));

        TextComponent Rules = new TextComponent(" **→** AuroraMC Rules\n");
        Rules.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://auroramc.net/rules")));
        Rules.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see our rules!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(Rules);

        TextComponent bugReport = new TextComponent(" **→** Report an issue with our systems\n");
        bugReport.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://auroramc.net/bug-report/")));
        bugReport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to report an issue with our systems!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(bugReport);

        TextComponent playerReport = new TextComponent(" **→** Report a Rule Breaker\n");
        playerReport.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://auroramc.net/threads/forum-report-information-thread.168/")));
        playerReport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see information on how to report rule breakers!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(playerReport);

        textComponent.addExtra(convert(String.format("\n\n**For further assistance, contact online staff using /s!**")));

        player.getPlayer().spigot().sendMessage(textComponent);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
