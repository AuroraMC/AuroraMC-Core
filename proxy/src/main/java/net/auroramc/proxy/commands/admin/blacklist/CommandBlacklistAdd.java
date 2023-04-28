/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.blacklist;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandBlacklistAdd extends ProxyCommand {


    public CommandBlacklistAdd() {
        super("add", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                AuroraMCAPI.getDbManager().addUsernameBan(args.get(0));
                player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Username added."));
                ProxiedPlayer pl = ProxyAPI.getCore().getProxy().getPlayer(args.get(0));
                if (pl != null) {
                    pl.disconnect(TextFormatter.pluginMessage("Username Manager", "This username is blacklisted.\n" +
                            "\n" +
                            "This username has been deemed inappropriate and is therefore\n" +
                            "blacklisted from use on our network!\n\n" +
                            "In order to join, simply change your name!"));
                } else {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "blacklist", AuroraMCAPI.getInfo().getName(), uuid.toString());
                        CommunicationUtils.sendMessage(message);
                    }
                }
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Username Blacklist", "Invalid syntax. Correct syntax: **/blacklist add [name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
