/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.ChatEmote;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.ChatFilter;
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

public class CommandEmotes extends Command {

    public String convert(@NotNull String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public CommandEmotes() {
        super("emotes", Collections.singletonList("emoticons"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        StringBuilder builder = new StringBuilder();
        builder.append("Your available emotes:\n");
        for (ChatEmote emote : ChatFilter.getEmotes().values()) {
            if (emote.hasUnlocked(player)) {
                builder.append("\n&b:");
                builder.append(emote.getDisplayName());
                builder.append(": &3&l» &");
                builder.append(emote.getColor().getChar());
                builder.append(emote.getDescription());
                builder.append("&r");
            }
        }
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Emotes", builder.toString().trim()));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
