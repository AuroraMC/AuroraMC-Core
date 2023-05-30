/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.staff.management;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandJuniorModeratorList extends ProxyCommand {

    public CommandJuniorModeratorList() {
        super("jmlist", Arrays.asList("juniormodlist", "listjuniormod", "listjuniormoderator", "jlist", "juniormoderatorlist"), Collections.singletonList(Permission.STAFF_MANAGEMENT), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
            Set<String> names = AuroraMCAPI.getDbManager().getJrModsOnline();
            player.sendMessage(TextFormatter.pluginMessage("Staff Management", String.format("There are currently **%s** Junior Moderators online:\n" +
                    "**%s**", names.size(), String.join("**, **", names))));
        });
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

}
