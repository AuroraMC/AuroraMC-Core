/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.admin.iplookup;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandIPLookup extends ServerCommand {

    public CommandIPLookup() {
        super("lookup", Arrays.asList("iplookup", "ip", "ipprofiles", "profiles"), Arrays.asList(Permission.ALL, Permission.ADMIN), false, null);
        this.registerSubcommand("profile", Collections.emptyList(), new CommandIPLookupProfile());
        this.registerSubcommand("compare", Collections.emptyList(), new CommandIPLookupCompare());
        this.registerSubcommand("player", Collections.emptyList(), new CommandIPLookupPlayer());
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            switch (args.get(0).toLowerCase()) {
                case "profile":
                case "player":
                case "compare":
                    aliasUsed = args.remove(0).toLowerCase();
                    subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                    break;
                default:
                    player.sendMessage(TextFormatter.pluginMessage("IP Lookup", "Available subcommands are:\n" +
                            "**/lookup profile [profile ID]** - View information about a specific profile.\n" +
                            "**/lookup player [player name]** - View information about a specific player.\n" +
                            "**/lookup compare [player 1] [player 2]** - Compare the IP profiles of 2 users."));
                    break;
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("IP Lookup", "Available subcommands are:\n" +
                    "**/lookup profile [profile ID]** - View information about a specific profile.\n" +
                    "**/lookup player [player name]** - View information about a specific player.\n" +
                    "**/lookup compare [player 1] [player 2]** - Compare the IP profiles of 2 users."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        ArrayList<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (String s : subcommands.keySet()){
                if (s.startsWith(lastToken)) {
                    completions.add(s);
                }
            }
        }
        return completions;
    }


}
