/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general;


import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandChat extends ProxyCommand {


    public CommandChat() {
        super("chat", Arrays.asList("chatchannel", "channel"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ChatChannel channel;
            try {
                channel = ChatChannel.valueOf(args.get(0).toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(TextFormatter.pluginMessage("Chat", "That channel is unrecognised. Please try tab-completing the command."));
                return;
            }

            if (channel == ChatChannel.NETWORK) {
                if (!player.hasPermission("all")) {
                    player.sendMessage(TextFormatter.pluginMessage("Chat", "That channel is unrecognised. Please try tab-completing the command."));
                    return;
                }
            }

            if (player.getChannel() == channel) {
                player.sendMessage(TextFormatter.pluginMessage("Chat", "You already have that channel active."));
                return;
            }

            player.setChannel(channel, true);
            player.sendMessage(TextFormatter.pluginMessage("Chat", String.format("Chat channel set to: **%s**", channel.name())));
            if (channel == ChatChannel.NETWORK) {
                player.sendMessage(TextFormatter.pluginMessage("Chat", "**§lPLEASE NOTE:** There might be a slight delay between sending the message and the message sending in chat. This is just the delay of Mission Control distributing the message to all of the connection nodes."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Chat", "Invalid syntax. Correct syntax: **/chat [channel]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> modes = new ArrayList<>();
        if (numberArguments == 1) {
            for (ChatChannel mode : ChatChannel.values()) {
                if (mode.name().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    if (mode != ChatChannel.NETWORK || player.hasPermission("all")) {
                        modes.add(mode.name());
                    }
                }
            }
        }

        return modes;
    }
}
