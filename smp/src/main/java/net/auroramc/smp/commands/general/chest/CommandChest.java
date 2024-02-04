/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.chest;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandChest extends ServerCommand {


    public CommandChest() {
        super("chest", Collections.singletonList("chests"), Collections.singletonList(Permission.PLAYER), false, null);
        this.registerSubcommand("add", Collections.emptyList(), new CommandChestAdd());
        this.registerSubcommand("remove", Collections.emptyList(), new CommandChestRemove());
        this.registerSubcommand("info", Collections.emptyList(), new CommandChestInfo());

    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Team", """
                    Available commands:
                    **/chest add [username | team]** - Add a member/your team to your chest.
                    **/chest remove [username | team]** - Remove a member/your team from your chest.
                    **/chest info** - Show all info on your chest."""));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "add":
            case "remove":
            case "info":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("Team", """
                    Available commands:
                    **/chest add [username | team]** - Add a member/your team to your chest.
                    **/chest remove [username | team]** - Remove a member/your team from your chest.
                    **/chest info** - Show all info on your chest."""));
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "add":
                case "remove":
                case "info":
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

