/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandFind extends ProxyCommand {


    public CommandFind() {
        super("find", new ArrayList<>(), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("Searching the network for **%s**...", args.get(0))));
            if (ProxyServer.getInstance().getPlayer(args.get(0)) != null) {
                AuroraMCProxyPlayer playerTarget = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(args.get(0)));
                player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("**%s** has been found at server: **%s**", playerTarget.getName(), playerTarget.getServer().getName())));
            } else {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                        UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("**%s** has been found at server: **%s**", args.get(0), AuroraMCAPI.getDbManager().getCurrentServer(uuid))));
                            return;
                        }
                    }
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (uuid != null) {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("**%s** has been found at server: **%s**", args.get(0), AuroraMCAPI.getDbManager().getCurrentServer(uuid))));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for player [**%s**]", args.get(0))));
                        }
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for player [**%s**]", args.get(0))));
                    }
                });
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "Invalid syntax. Correct syntax: **/find [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
