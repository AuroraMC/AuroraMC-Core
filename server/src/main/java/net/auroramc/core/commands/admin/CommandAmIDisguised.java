/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandAmIDisguised extends ServerCommand {


    public CommandAmIDisguised() {
        super("amidisguised", Collections.singletonList("disguised?"), Collections.singletonList(Permission.DISGUISE), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.isDisguised()) {
            BaseComponent component = new ComponentBuilder("Yes.").bold(true).color(ChatColor.GREEN).create()[0];
            player.sendTitle(component, new TextComponent(""), 0, 100, 20);
            player.sendMessage(component);
        } else {
            BaseComponent component = new ComponentBuilder("No.").bold(true).color(ChatColor.RED).create()[0];
            player.sendTitle(component, new TextComponent(""), 0, 100, 20);
            player.sendMessage(component);
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
