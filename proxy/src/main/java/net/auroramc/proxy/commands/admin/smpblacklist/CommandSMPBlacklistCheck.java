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
import java.util.UUID;

public class CommandSMPBlacklistCheck extends ProxyCommand {


    public CommandSMPBlacklistCheck() {
        super("add", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                if (uuid == null) {
                    try {
                        uuid = UUID.fromString(args.get(0));
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", "Please provide a valid username or UUID."));
                        return;
                    }
                }
                if (AuroraMCAPI.getDbManager().isSMPBlacklist(uuid.toString())) {
                    player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", String.format("User [**%s**] SMP blacklisted: §atrue", args.get(0))));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", String.format("User [**%s**] SMP blacklisted: §cfalse", args.get(0))));
                }
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("SMP Blacklist", "Invalid syntax. Correct syntax: **/smpblacklist check [name | uuid]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
