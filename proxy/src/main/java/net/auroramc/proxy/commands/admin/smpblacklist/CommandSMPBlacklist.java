/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.smpblacklist;


import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSMPBlacklist extends ProxyCommand {


    public CommandSMPBlacklist() {
        super("smpblacklist", Arrays.asList("smpb", "jb"), Collections.singletonList(Permission.ADMIN), false, null);
        this.registerSubcommand("add", Collections.emptyList(), new CommandSMPBlacklistAdd());
        this.registerSubcommand("remove", Collections.emptyList(), new CommandSMPBlacklistRemove());
        this.registerSubcommand("check", Collections.emptyList(), new CommandSMPBlacklistCheck());
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", "Available commands:\n" +
                    "**/smpblacklist add [name | uuid]** - Add a username to the username blacklist.\n" +
                    "**/smpblacklist remove [name | uuid]** - Remove a username to the username blacklist.\n" +
                    "**/smpblacklist check [name | uuid]** - Check if a username is in the username blacklist."));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "add":
            case "remove":
            case "check":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", "Available commands:\n" +
                        "**/smpblacklist add [name | uuid]** - Add a username to the username blacklist.\n" +
                        "**/smpblacklist remove [name | uuid]** - Remove a username to the username blacklist.\n" +
                        "**/smpblacklist check [name | uuid]** - Check if a username is in the username blacklist."));
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        if (numberArguments >= 2) {
            return new ArrayList<>();
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
