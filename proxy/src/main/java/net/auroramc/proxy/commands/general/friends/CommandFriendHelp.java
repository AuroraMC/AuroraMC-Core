/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.friends;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandFriendHelp extends ProxyCommand {
    public CommandFriendHelp() {
        super("help", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        player.sendMessage(TextFormatter.pluginMessage("Friends", "Available sub-commands:\n" +
                "**/friend** - Open the Friends GUI\n" +
                "**/friend [player]** - Send a friend request\n" +
                "**/friend help** - Shows this help menu.\n" +
                "**/friend accept [player]** - Accept a players friend request.\n" +
                "**/friend deny [player]** - Deny a players friend request.\n" +
                "**/friend cancel [player]** - Cancel an outgoing friend request.\n" +
                "**/friend favourite [player]** - Toggle a player as a favourite friend.\n" +
                "**/friend remove [player]** - Remove a player from your friends list.\n" +
                "**/friend status [status]** - Sets your current status.\n" +
                "**/friend visibility [visibility mode]** - Sets your current visibility mode."));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
