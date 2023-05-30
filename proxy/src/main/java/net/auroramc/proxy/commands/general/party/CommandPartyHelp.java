/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyHelp extends ProxyCommand {
    public CommandPartyHelp() {
        super("help", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        player.sendMessage(TextFormatter.pluginMessage("Party", "Available commands:\n" +
                "**/party <player>** - Alias for /party invite.\n" +
                "**/party chat <message>** - Send a chat message into party chat.\n" +
                "**/party leave** - Leave your current party.\n" +
                "**/party invite <player>** - Invite a player to your party.\n" +
                "**/party transfer <player>** - Transfer your party to someone else.\n" +
                "**/party accept <player>** - Accept a players party invite.\n" +
                "**/party deny <player>** - Deny a players party invite.\n" +
                "**/party cancel <player>** - Cancel an outgoing party invite.\n" +
                "**/party remove <player>** - Remove a player from your party.\n" +
                "**/party warp** - Warp all players to your current server.\n" +
                "**/party list** - List all players in your current party.\n" +
                "**/party disband** - Disband your party.\n" +
                "**/party help** - Displays this help menu."));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
