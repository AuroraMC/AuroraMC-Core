/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.blacklist;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBlacklistCheck extends ProxyCommand {


    public CommandBlacklistCheck() {
        super("add", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                if (AuroraMCAPI.getDbManager().isUsernameBanned(args.get(0).toLowerCase())) {
                    player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", String.format("Username [**%s**] blacklisted: §atrue", args.get(0))));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", String.format("Username [**%s**] blacklisted: §cfalse", args.get(0))));
                }
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Invalid syntax. Correct syntax: **/blacklist check [name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
