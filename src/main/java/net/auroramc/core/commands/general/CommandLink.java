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
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLink extends Command {


    public CommandLink() {
        super("link", Arrays.asList("discordlink","discord"), Collections.singletonList(Permission.PLAYER), true, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
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
                            component.addExtra(AuroraMCAPI.getFormatter().pluginMessage("Discord", "Code generated: "));
                            TextComponent codeComponent = new TextComponent(code);
                            codeComponent.setColor(ChatColor.AQUA);
                            codeComponent.setBold(true);
                            codeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code));
                            codeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(AuroraMCAPI.getFormatter().convert("&aClick to copy to clipboard.")).create()));
                            component.addExtra(codeComponent);
                            component.addExtra(AuroraMCAPI.getFormatter().convert(". In order to link your in-game account to your Discord, all you have to do is do &b!link " + code + "&r. This code only lasts 60 seconds!"));
                            player.getPlayer().spigot().sendMessage(component);
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", "You are already linked with a Discord account! In order to prevent abuse, you cannot unlink your Discord and in-game accounts yourself. Please contact our customer support who can help you further."));
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", "You already have a code active. Please use that code of wait till it expires and run this command again."));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", "You are already linked with a Discord account! In order to prevent abuse, you cannot unlink your Discord and in-game accounts yourself. Please contact our customer support who can help you further."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int amountArguments) {
        return new ArrayList<>();
    }
}
