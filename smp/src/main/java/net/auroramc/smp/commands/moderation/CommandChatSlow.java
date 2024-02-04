/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.ChatSlowLength;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandChatSlow extends ServerCommand {


    public CommandChatSlow() {
        super("chatslow", Collections.singletonList("slow"), Arrays.asList(Permission.ADMIN, Permission.EVENT_MANAGEMENT, Permission.SOCIAL_MEDIA, Permission.STAFF_MANAGEMENT), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            short amount;
            try {
                amount = Short.parseShort(args.get(0));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            if (amount < 1 || amount > 300) {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            AuroraMCAPI.setChatSlow(amount);
            ChatSlowLength length = new ChatSlowLength(amount);
            for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                if (player2.hasPermission("moderation") || player2.hasPermission("social") ||  player2.hasPermission("debug.info")) {
                    player2.sendMessage(TextFormatter.pluginMessage("ChatSlow", String.format("A chat slow of **%s** was enabled on this server. You are immune to Chat Slow due to your rank.", length.getFormatted())));
                } else {
                    player2.sendMessage(TextFormatter.pluginMessage("ChatSlow", String.format("A Moderator has enabled a chat slow on this server. You may now only chat every **%s**.", length.getFormatted())));
                }
            }
        } else {
            if (AuroraMCAPI.getChatSlow() != -1) {
                AuroraMCAPI.setChatSlow((short) -1);
                for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                    player2.sendMessage(TextFormatter.pluginMessage("ChatSlow", "The active chat slow in this server has been disabled."));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("ChatSlow", "There is currently no chat slow active. To set a chat slow, use **/chatslow [seconds]**."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
