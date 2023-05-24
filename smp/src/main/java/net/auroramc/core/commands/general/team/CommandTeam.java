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
        this.registerSubcommand("create", Collections.emptyList(), new CommandTeamCreate());
        this.registerSubcommand("invite", Collections.emptyList(), new CommandTeamInvite());
        this.registerSubcommand("remove", Collections.emptyList(), new CommandTeamRemove());
        this.registerSubcommand("rename", Collections.emptyList(), new CommandTeamRename());
        this.registerSubcommand("prefix", Collections.emptyList(), new CommandTeamPrefix());
        this.registerSubcommand("info", Collections.emptyList(), new CommandTeamInfo());
        this.registerSubcommand("accept", Collections.emptyList(), new CommandTeamAccept());
        this.registerSubcommand("disband", Collections.emptyList(), new CommandTeamDisband());

    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Team", """
                    Available commands:
                    **/team create [name]** - Creates a team.
                    **/team invite [username]** - Invite a user to be part of your team.
                    **/team remove [username]** - Remove a user from your team.
                    **/team rename [name]** - Rename your team.
                    **/team prefix [prefix]** - Set your team prefix. This is compatible with spaces and colour codes.
                    **/team info** - Displays information about your team.
                    **/team accept** - Accept a team invite.
                    **/team disband** - Disband your team. §c§lWARNING:§r This will lock all of your team chests to their original owner and any other members added to the chest."""));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "create":
            case "invite":
            case "remove":
            case "rename":
            case "disband":
            case "info":
            case "accept":
            case "prefix":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("Team", """
                    Available commands:
                    **/team create [name]** - Creates a team.
                    **/team invite [username]** - Invite a user to be part of your team.
                    **/team remove [username]** - Remove a user from your team.
                    **/team rename [name]** - Rename your team.
                    **/team prefix [prefix]** - Set your team prefix. This is compatible with spaces and colour codes.
                    **/team info** - Displays information about your team.
                    **/team accept** - Accept a team invite.
                    **/team disband** - Disband your team. §c§lWARNING:§r This will lock all of your team chests to their original owner and any other members added to the chest."""));
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
                case "disband":
                case "info":
                case "accept":
                case "prefix":
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
