/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
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

public class CommandHelp extends ServerCommand {

    public String convert(@NotNull String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public CommandHelp() {
        super("help", Arrays.asList("help", "helpme", "helpmeorsiawillcomeandmakeyouthegreatest", "helpmeorelseillkillyou", "helpwouldbeappreciated"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(13))) {
            player.getStats().achievementGained(AuroraMCAPI.getAchievement(13), 1, true);
        }
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
        + WordUtils.wrap("&fTo cater to those who are curious as well as stay as transparent as possible," +
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
                + WordUtils.wrap("&fOur rules are created to ensure that all players are able to have a good experience on the network." +
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
                + WordUtils.wrap("&fThe AuroraMC Store is the only place to buy AuroraMC products. From ranks, cosmetics," +
                " gift cards and more. It can all be found at store.auroramc.net", 40, "\n&r", false)
                + "\n\n&aClick here to visit our store."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" AuroraMC Discord\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.auroramc.net/"));
        componentHover = new ComponentBuilder(convert("&3&lAuroraMC Discord\n"
                + "\n"
                + WordUtils.wrap("&fThe AuroraMC Discord is the main communication platform that is used by," +
                " AuroraMC. If you want to interact with members of the community, don't hesitate to join the discord!", 40, "\n&r", false)
                + "\n\n&aClick here to join our discord."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Submit a Support Ticket\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/support-tickets/"));
        componentHover = new ComponentBuilder(convert("&3&lSubmit a Support Ticket\n"
                + "\n"
                + WordUtils.wrap("&fIf you are having issues with purchases or missing cosmetics, feel free to contact our" +
                " Customer Support Team and they will look into and assist you as much as they can.", 40, "\n&r", false)
                + "\n\n&aClick here to submit a support ticket."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);
        textComponent.addExtra(arrow);

        component = new TextComponent(" Submit an appeal\n");
        component.setColor(ChatColor.AQUA);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/appeal/"));
        componentHover = new ComponentBuilder(convert("&3&lSubmit an appeal\n"
                + "\n"
                + WordUtils.wrap("&fIf you believe you were unfairly punished" +
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
                + WordUtils.wrap("&fIf you believe you have found an issue with our systems," +
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
                + WordUtils.wrap("&fIf you have found somebody that is breaking the AuroraMC Rules, " +
                "and a Staff Member is not present to handle it, you are able to submit a report " +
                "and have it handled by our Reports Team! These documents go into details on how to use any " +
                "report systems we have in place to make the server a better place.", 40, "\n&r", false)
                + "\n\n&aClick here to report a Rule Breaker."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);

        component = new TextComponent(" \nIf you have a question that isn't addressed in the above links, please check our Knowledgebase!\n");
        component.setColor(ChatColor.WHITE);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/knowledgebase"));
        componentHover = new ComponentBuilder(convert("&3&lKnowledgebase\n"
                + "\n"
                + WordUtils.wrap("&fOur knowledgebase consists of answers to a majority of the most frequently" +
                " asked questions on the network. If you cannot find the answer" +
                " to your question using one of the above links, it may be in the knowledgebase!", 40, "\n&r", false)
                + "\n\n&aClick here to visit our knowledgebase."));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentHover.create()));
        textComponent.addExtra(component);

        component = new TextComponent("\nFor further assistance, contact staff in your lobby using /s!");
        component.setColor(ChatColor.AQUA);

        textComponent.addExtra(component);

        player.sendMessage(textComponent);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
