/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRestart extends ServerCommand {

    public CommandRestart() {
        super("restart", Collections.emptyList(), Arrays.asList(Permission.ADMIN, Permission.DEBUG_ACTION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    ServerInfo info = AuroraMCAPI.getDbManager().getServerDetailsByName(args.get(0), AuroraMCAPI.getInfo().getNetwork().name());
                    if (info != null) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.SHUTDOWN, "Mission Control", args.get(0), AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name());
                        CommunicationUtils.sendMessage(message);
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", "Server restart request sent."));
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", "No results found for server **" + args.get(0) + "**."));
                    }
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "Invalid syntax. Correct syntax: **/restart [server]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
