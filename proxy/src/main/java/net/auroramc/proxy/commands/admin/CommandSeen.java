/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandSeen extends ProxyCommand {

    public CommandSeen() {
        super("seen", Collections.singletonList("lastseen"), Arrays.asList(Permission.ADMIN, Permission.STAFF_MANAGEMENT, Permission.SOCIAL_MEDIA, Permission.BUILD_TEAM_MANAGEMENT), false, null);
    }


    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                if (uuid != null) {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                    if (id > 0) {
                        long seen = AuroraMCAPI.getDbManager().getLastSeen(id);
                        if (seen != -1) {
                            player.sendMessage(TextFormatter.pluginMessage("Seen", "User **" + args.get(0) + "** last logged in at: **" + new Date(seen) + "**"));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Seen", "That user has never joined the network."));
                        }
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Seen", "Could not find a user by that name."));
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Seen", "Could not find a user by that name."));
                }
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Seen", "Invalid syntax. Correct syntax: **/seen [user]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
