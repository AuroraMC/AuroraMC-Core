/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.iplookup;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandIPLookup extends Command {

    public CommandIPLookup() {
        super("lookup", Arrays.asList("iplookup", "ip", "ipprofiles", "profiles"), Arrays.asList(Permission.ALL, Permission.ADMIN), false, null);
        this.registerSubcommand("profile", Collections.emptyList(), new CommandIPLookupProfile());
        this.registerSubcommand("compare", Collections.emptyList(), new CommandIPLookupCompare());
        this.registerSubcommand("player", Collections.emptyList(), new CommandIPLookupPlayer());
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            switch (args.get(0).toLowerCase()) {
                case "profile":
                case "player":
                case "compare":
                    aliasUsed = args.remove(0).toLowerCase();
                    subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                    break;
                default:
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Available subcommands are:\n" +
                            "**/lookup profile [profile ID]** - View information about a specific profile.\n" +
                            "**/lookup player [player name]** - View information about a specific player.\n" +
                            "**/lookup compare [player 1] [player 2]** - Compare the IP profiles of 2 users."));
                    break;
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Available subcommands are:\n" +
                    "**/lookup profile [profile ID]** - View information about a specific profile.\n" +
                    "**/lookup player [player name]** - View information about a specific player.\n" +
                    "**/lookup compare [player 1] [player 2]** - Compare the IP profiles of 2 users."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
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
