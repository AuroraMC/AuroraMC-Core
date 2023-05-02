/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general.team;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.commands.general.ignore.CommandIgnoreAdd;
import net.auroramc.core.commands.general.ignore.CommandIgnoreList;
import net.auroramc.core.commands.general.ignore.CommandIgnoreRemove;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeam extends ServerCommand {

    public CommandTeam() {
        super("team", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);

    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Team", """
                    Available commands:
                    **/team create [name]** - Creates a team. This is compatible with spaces and colour codes.
                    **/team invite [username]** - Invite a user to be part of your team.
                    **/team remove [username]** - Remove a user from your team.
                    **/team rename [name]** - Rename your team.
                    **/team delete** - Delete your team. §c§lWARNING:§r This will unlock all of your team chests."""));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "create":
            case "invite":
            case "remove":
            case "rename":
            case "delete":
            case "info":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("Team", """
                    Available commands:
                    **/team create [name]** - Creates a team. This is compatible with spaces and colour codes.
                    **/team invite [username]** - Invite a user to be part of your team.
                    **/team remove [username]** - Remove a user from your team.
                    **/team rename [name]** - Rename your team.
                    **/team info** - Displays information about your team.
                    **/team delete** - Delete your team. §c§lWARNING:§r This will unlock all of your team chests."""));
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "create":
                case "invite":
                case "remove":
                case "rename":
                case "delete":
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
