/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.ignore;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIgnoreRemove extends ServerCommand {

    public CommandIgnoreRemove() {
        super("remove", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getName().equalsIgnoreCase(args.get(0)) || player.getName().equalsIgnoreCase(args.get(0))) {
                player.sendMessage(TextFormatter.pluginMessage("Ignore", "You cannot ignore yourself, silly!"));
                return;
            }
            if (!player.isIgnored(args.get(0))) {
                player.sendMessage(TextFormatter.pluginMessage("Ignore", "You do not have that player ignored."));
                return;
            }
            player.removeIgnored(player.getIgnored(args.get(0)), true);
            player.sendMessage(TextFormatter.pluginMessage("Ignore", String.format("**%s** removed from your ignore list.", args.get(0))));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Ignore", "Invalid syntax. Correct syntax: **/ignore remove [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
