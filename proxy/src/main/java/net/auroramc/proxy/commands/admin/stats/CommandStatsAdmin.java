/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.stats;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandStatsAdmin extends ProxyCommand {

    public CommandStatsAdmin() {
        super("statsadmin", Arrays.asList("sa", "statadmin", "stat"), Collections.singletonList(Permission.ADMIN), false, null);
        this.registerSubcommand("achievement", new ArrayList<>(), new CommandStatsAdminAchievement());
        this.registerSubcommand("stat", new ArrayList<>(), new CommandStatsAdminStat());
        this.registerSubcommand("xp", new ArrayList<>(), new CommandStatsAdminXP());
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                    "**/statsadmin achievement add <player> <achievement ID> [tier]** - Add an achievement to a player.\n" +
                    "**/statsadmin achievement remove <player> <achievement ID> [tier]** - Remove an achievement from a player\n" +
                    "**/statsadmin stat add <player> <game> <stat> <amount>** - Increment a statistic on a users statistics\n" +
                    "**/statsadmin stat remove <player> <game> <stat> <amount>** - Decrement a statistic on a users statistics\n" +
                    "**/statsadmin xp add <player> <amount>** - Add XP to a users profile.\n" +
                    "**/statsadmin xp remove <player> <amount>** - Remove XP from a users profile."));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "achievement":
            case "stat":
            case "xp":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                        "**/statsadmin achievement add <player> <achievement ID> [tier]** - Add an achievement to a player.\n" +
                        "**/statsadmin achievement remove <player> <achievement ID> [tier]** - Remove an achievement from a player\n" +
                        "**/statsadmin stat add <player> <game> <stat> <amount>** - Increment a statistic on a users statistics\n" +
                        "**/statsadmin stat remove <player> <game> <stat> <amount>** - Decrement a statistic on a users statistics\n" +
                        "**/statsadmin xp add <player> <amount>** - Add XP to a users profile.\n" +
                        "**/statsadmin xp remove <player> <amount>** - Remove XP from a users profile."));
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> types = new ArrayList<>(Arrays.asList("achievement", "stat", "xp", "level"));
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (String type : types) {
                if (type.toLowerCase().startsWith(lastToken.toLowerCase())) {
                    completions.add(type);
                }
            }
        } else if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "achievement":
                case "stat":
                case "xp":
                    aliasUsed = args.remove(0);
                    return subcommands.get(aliasUsed).onTabComplete(player, aliasUsed, args, ((args.size() >= 1)?args.get(0):""), numberArguments - 1);
                default:
                    return new ArrayList<>();
            }
        }

        return completions;
    }
}
