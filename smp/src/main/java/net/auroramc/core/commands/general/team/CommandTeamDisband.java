/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.general.team;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeamDisband extends ServerCommand {


    public CommandTeamDisband() {
        super("disband", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            if (player.getSmpTeam().getLeader().getUuid().equals(player.getUniqueId())) {
                player.getSmpTeam().disband(true);
                player.setSmpTeam(null);
                player.sendMessage(TextFormatter.pluginMessage("Teams", "Your team has been disbanded."));
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Teams", "You must be the leader of your team to use this command."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teams", "You must be in a team to use this command."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
