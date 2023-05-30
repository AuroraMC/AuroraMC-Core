/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.ChatFilter;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandEmotes extends ServerCommand {

    public String convert(@NotNull String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public CommandEmotes() {
        super("emotes", Collections.singletonList("emoticons"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        StringBuilder builder = new StringBuilder();
        builder.append("Your available emotes:\n");
        for (ChatEmote emote : ChatFilter.getEmotes().values()) {
            if (emote.hasUnlocked(player)) {
                builder.append("\n**:");
                builder.append(emote.getDisplayName());
                builder.append(":** §3§l» ");
                builder.append(emote.getColor());
                builder.append(emote.getDescription());
                builder.append("§r");
            }
        }
        player.sendMessage(TextFormatter.pluginMessage("Emotes", builder.toString().trim()));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
