/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.api.utils.UUIDUtil;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandPreload extends ProxyCommand {


    public CommandPreload() {
        super("preload", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            String name = args.get(0);
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                UUID uuid = UUIDUtil.getUUID(name);
                if (uuid == null) {
                    player.sendMessage(TextFormatter.pluginMessage("Player Manager", "That player has not been found."));
                    return;
                }

                Rank rank;
                try {
                    rank = Rank.valueOf(args.get(1));
                } catch (IllegalArgumentException e) {
                    player.sendMessage(TextFormatter.pluginMessage("Player Manager", "Invalid syntax. Correct syntax: **/preload [name] [rank]**"));
                    return;
                }

                AuroraMCAPI.getDbManager().newUser(uuid, name);
                AuroraMCAPI.getDbManager().addPreloadMessage(uuid, rank);

                player.sendMessage(TextFormatter.pluginMessage("Player Manager", "User **" + name + "** has been pre-loaded with rank **" + rank.getName() + "**."));
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Player Manager", "Invalid syntax. Correct syntax: **/preload [name] [rank]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
