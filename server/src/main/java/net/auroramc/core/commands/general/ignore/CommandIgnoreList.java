/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general.ignore;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIgnoreList extends ServerCommand {

    public CommandIgnoreList() {
        super("list", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            int page;
            try {
                page = Integer.parseInt(args.get(0));
            } catch (NumberFormatException ignored) {
                player.sendMessage(TextFormatter.pluginMessage("Ignore", "Invalid syntax. Correct syntax: **/ignore list <page>**"));
                return;
            }

            if (page < 1) {
                page = 1;
            }

            int totalPages = (player.getIgnoredPlayers().size() / 7) + ((player.getIgnoredPlayers().size() % 7 > 0)?1:0);
            if (page > totalPages) {
                page = totalPages;
            }

            player.sendMessage(TextFormatter.formatIgnoreList(player, page));
        } else {
            player.sendMessage(TextFormatter.formatIgnoreList(player, 1));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
