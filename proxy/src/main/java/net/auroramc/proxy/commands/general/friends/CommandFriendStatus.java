/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.friends;


import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandFriendStatus extends ProxyCommand {
    public CommandFriendStatus() {
        super("status", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            FriendStatus status;
            status = FriendStatus.getByCode(args.get(0).toUpperCase());

            if (status == null) {
                player.sendMessage(TextFormatter.pluginMessage("Friends", "That status is unrecognised. Please try tab-completing the command."));
                return;
            }

            if (!status.hasUnlocked(player)) {
                if (!status.showIfNotUnlocked()) {
                    player.sendMessage(TextFormatter.pluginMessage("Friends", "That status is unrecognised. Please try tab-completing the command."));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Friends", ((status.getUnlockMessage().equals("")?"That status is unrecognised. Please try tab-completing the command.": status.getUnlockMessage()))));
                }
                return;
            }

            player.getFriendsList().setCurrentStatus(status, true);
            player.sendMessage(TextFormatter.pluginMessage("Friends", "You have updated your status to: " + String.format("&%s%s", status.getColour(), status.getName())));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Friends", "Invalid syntax. Correct syntax: **/friend status [status]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> modes = new ArrayList<>();
        if (numberArguments == 1) {
            for (FriendStatus mode : FriendStatus.getFriendStatuses().values()) {
                if (!mode.hasUnlocked(player)) {
                    continue;
                }
                if (mode.getDisplayName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    modes.add(mode.getDisplayName());
                }
            }
        }

        return modes;
    }
}
