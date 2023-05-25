/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.smpblacklist;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSMPBlacklistRemove extends ProxyCommand {


    public CommandSMPBlacklistRemove() {
        super("remove", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                AuroraMCAPI.getDbManager().removeUsernameBan(args.get(0));
                player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Username removed."));
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Invalid syntax. Correct syntax: **/blacklist remove [name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
