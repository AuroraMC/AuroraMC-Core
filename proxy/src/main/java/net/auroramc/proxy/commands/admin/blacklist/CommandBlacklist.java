/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.blacklist;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandBlacklist extends ProxyCommand {


    public CommandBlacklist() {
        super("blacklist", Arrays.asList("usernameblacklist", "bl"), Collections.singletonList(Permission.ADMIN), false, null);
        this.registerSubcommand("add", Collections.emptyList(), new CommandBlacklistAdd());
        this.registerSubcommand("remove", Collections.emptyList(), new CommandBlacklistRemove());
        this.registerSubcommand("check", Collections.emptyList(), new CommandBlacklistCheck());
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Available commands:\n" +
                    "**/blacklist add [name]** - Add a username to the username blacklist.\n" +
                    "**/blacklist remove [name]** - Remove a username to the username blacklist.\n" +
                    "**/blacklist check [name]** - Check if a username is in the username blacklist."));
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
                player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Available commands:\n" +
                        "**/blacklist add [name]** - Add a username to the username blacklist.\n" +
                        "**/blacklist remove [name]** - Remove a username to the username blacklist.\n" +
                        "**/blacklist check [name]** - Check if a username is in the username blacklist."));
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
