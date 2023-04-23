/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLink extends ServerCommand {


    public CommandLink() {
        super("link", Arrays.asList("discordlink","discord"), Collections.singletonList(Permission.PLAYER), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getLinkedDiscord() == null) {
            if (!player.isDiscordCodeGenerated()) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if (AuroraMCAPI.getDbManager().getDiscord(player.getId()) == null) {
                            String code = RandomStringUtils.randomAlphanumeric(8);
                            while (AuroraMCAPI.getDbManager().codeExists(code)) {
                                code = RandomStringUtils.randomAlphanumeric(8);
                            }
                            AuroraMCAPI.getDbManager().newCode(code, player);
                            player.codeGenerated();
                            TextComponent component = new TextComponent("");
                            component.addExtra(TextFormatter.pluginMessage("Discord", "Code generated: "));
                            TextComponent codeComponent = new TextComponent(code);
                            codeComponent.setColor(ChatColor.AQUA);
                            codeComponent.setBold(true);
                            codeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code));
                            codeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to copy to clipboard.").color(ChatColor.GREEN).create()));
                            component.addExtra(codeComponent);
                            component.addExtra(TextFormatter.highlight(". In order to link your in-game account to your Discord, all you have to do is do **/link " + code + "**. This code only lasts 60 seconds!"));
                            player.sendMessage(component);
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Discord", "You are already linked with a Discord account! In order to prevent abuse, you cannot unlink your Discord and in-game accounts yourself. Please contact our customer support who can help you further."));
                        }
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Discord", "You already have a code active. Please use that code of wait till it expires and run this command again."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Discord", "You are already linked with a Discord account! In order to prevent abuse, you cannot unlink your Discord and in-game accounts yourself. Please contact our customer support who can help you further."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int amountArguments) {
        return new ArrayList<>();
    }
}
