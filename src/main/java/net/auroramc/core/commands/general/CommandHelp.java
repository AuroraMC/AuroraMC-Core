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

        TextComponent auroraMC = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ How AuroraMC works**\n")));
        auroraMC.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/how-auroramc-works/"));
        auroraMC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see how AuroraMC works!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(auroraMC);

        TextComponent rules = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ AuroraMC Rules**\n")));
        rules.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/rules"));
        rules.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see our rules!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(rules);

        TextComponent store = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ AuroraMC Store**\n")));
        store.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.auroramc.net/"));
        store.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to visit our store!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(store);

        TextComponent support = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ Submit a Support Ticket**\n")));
        support.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/support/"));
        support.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to submit a support ticket!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(support);

        TextComponent appeal = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ Submit an appeal**\n")));
        appeal.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/appeal/"));
        appeal.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to submit an appeal!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(appeal);

        TextComponent bugReport = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ Report an issue with our systems**\n")));
        bugReport.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/bug-report/"));
        bugReport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to report an issue with our systems!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(bugReport);

        TextComponent playerReport = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **➤ Report a Rule Breaker**\n")));
        playerReport.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/report-info/"));
        playerReport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to see information on how to report rule breakers!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(playerReport);

        TextComponent knowledgeBase = new TextComponent(convert(AuroraMCAPI.getFormatter().highlight(" **\nIf you have a question that isn't addressed in the above** **links, please check our Knowledgebase!**\n")));
        knowledgeBase.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/knowledgebase"));
        knowledgeBase.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to go to our knowledgebase!").color(ChatColor.LIGHT_PURPLE).create()));
        textComponent.addExtra(knowledgeBase);

        textComponent.addExtra(convert(AuroraMCAPI.getFormatter().highlight("**\nFor further assistance, contact online staff using /s!**")));

        player.getPlayer().spigot().sendMessage(textComponent);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
