/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation.blocklog;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBlockLog extends ServerCommand {


    public CommandBlockLog() {
        super("blocklog", Collections.emptyList(), Collections.singletonList(Permission.MODERATION), false, null);
        this.registerSubcommand("block", Collections.emptyList(), new CommandBlockLogBlock());
        this.registerSubcommand("player", Collections.emptyList(), new CommandBlockLogPlayer());
        this.registerSubcommand("type", Collections.emptyList(), new CommandBlockLogType());
        this.registerSubcommand("setlimit", Collections.emptyList(), new CommandBlockLogSetLimit());
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Block Logs", """
                    Available commands:
                    **/blocklog block [type | ALL] [x] [y] [z]** - Search the blocklog based on co-ordinates.
                    **/blocklog player [type | ALL] [username]** - Search the blocklog based on player.
                    **/blocklog type [type | ALL] [block type]** - Search the blocklog based on a block type.
                    **/blocklog setlimit [amount]** - Set the amount of log events you see per command execution (applies server-wide)."""));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "block":
            case "player":
            case "type":
            case "setlimit":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("Block Logs", """
                    Available commands:
                    **/blocklog block [type | ALL] [x] [y] [z]** - Search the blocklog based on co-ordinates.
                    **/blocklog player [type | ALL] [username]** - Search the blocklog based on player.
                    **/blocklog type [type | ALL] [block type]** - Search the blocklog based on a block type.
                    **/blocklog setlimit [amount]** - Set the amount of log events you see per command execution (applies server-wide)."""));
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "block":
                case "player":
                case "type":
                case "setlimit":
                    aliasUsed = args.remove(0);
                    return ((ServerCommand)subcommands.get(aliasUsed)).onTabComplete(player, aliasUsed, args, ((args.size() >= 1)?args.get(0):""), numberArguments - 1);
                default:
                    return new ArrayList<>();
            }
        } else {
            List<String> completions = new ArrayList<>();
            for (String s : subcommands.keySet()){
                if (s.startsWith(lastToken)) {
                    completions.add(s);
                }
            }
            return completions;
        }
    }

}

