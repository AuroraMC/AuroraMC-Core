/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.maintenance;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandMaintenance extends ProxyCommand {


    public CommandMaintenance() {
        super("maintenance", Arrays.asList("maintenancemode", "mm"), Collections.singletonList(Permission.ALL), false, null);
        this.registerSubcommand("toggle", new ArrayList<>(), new CommandToggle());
        this.registerSubcommand("mode", new ArrayList<>(), new CommandMode());
        this.registerSubcommand("motd", new ArrayList<>(), new CommandMotd());
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Maintenance", "Available commands:\n" +
                    "**/mm toggle [mode]** - Toggle Maintenance Mode on with the selected mode.\n" +
                    "**/mm mode [mode]** - Change the current maintenance mode active.\n" +
                    "**/mm motd [Message]** - Sets the MOTD of the current Maintenance Mode."));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "toggle":
            case "motd":
            case "mode":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.sendMessage(TextFormatter.pluginMessage("Maintenance", "Available commands:\n" +
                        "**/mm toggle [mode]** - Toggle Maintenance Mode on with the selected mode.\n" +
                        "**/mm mode [mode]** - Change the current maintenance mode active.\n" +
                        "**/mm motd [Message]** - Sets the MOTD of the current Maintenance Mode."));
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "toggle":
                case "motd":
                case "mode":
                    aliasUsed = args.remove(0);
                    return subcommands.get(aliasUsed).onTabComplete(player, aliasUsed, args, ((args.size() >= 1)?args.get(0):""), numberArguments - 1);
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
