/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.debug;


import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBBTest extends ProxyCommand {


    public CommandBBTest() {
        super("bbtest", Collections.emptyList(), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        throw new RuntimeException("This is a test exception");
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}