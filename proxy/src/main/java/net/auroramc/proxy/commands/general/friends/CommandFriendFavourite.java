/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.friends;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandFriendFavourite extends ProxyCommand {
    public CommandFriendFavourite() {
        super("favourite", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            Friend friend = null;
            for (Friend f : player.getFriendsList().getFriends().values()) {
                if (f.getName().equalsIgnoreCase(args.get(0))) {
                    friend = f;
                    break;
                }
            }
            if (friend == null) {
                player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You do not have a friend called **%s**.", args.get(0))));
                return;
            }

            if (friend.getType() == Friend.FriendType.FAVOURITE) {
                friend.unfavourited(true);
                player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You have unfavourited **%s**!", friend.getName())));
            } else {
                friend.favourited(true);
                player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You have favourited **%s**!", friend.getName())));
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(33))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(33), 1, true);
                }
            }

        } else {
            player.sendMessage(TextFormatter.pluginMessage("Friends", "Invalid syntax. Correct syntax: **/friend favourite [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
