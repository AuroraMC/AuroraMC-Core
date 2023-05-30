/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.friends;


import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.auroramc.api.player.friends.FriendsList.VisibilityMode.ALL;
import static net.auroramc.api.player.friends.FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY;

public class CommandFriendVisibility extends ProxyCommand {
    public CommandFriendVisibility() {
        super("visibility", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            FriendsList.VisibilityMode visibilityMode;
            try {
                visibilityMode = FriendsList.VisibilityMode.valueOf(args.get(0).toUpperCase());
            } catch (IllegalArgumentException ignored) {
                player.sendMessage(TextFormatter.pluginMessage("Friends", "That visibility mode is unrecognised. Please try tab-completing the command."));
                return;
            }

            player.getFriendsList().setVisibilityMode(visibilityMode, true);
            player.sendMessage(TextFormatter.pluginMessage("Friends", "You have updated your visibility mode to: " + ((visibilityMode == ALL) ? "&aAll" : ((visibilityMode == FAVOURITE_FRIENDS_ONLY) ? "&6Favourite Friends Only" : "&cNobody"))));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Friends", "Invalid syntax. Correct syntax: **/friend status [status]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> modes = new ArrayList<>();
        if (numberArguments == 1) {
            for (FriendsList.VisibilityMode mode : FriendsList.VisibilityMode.values()) {
                if (mode.name().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    modes.add(mode.name());
                }
            }
        }

        return modes;
    }
}
