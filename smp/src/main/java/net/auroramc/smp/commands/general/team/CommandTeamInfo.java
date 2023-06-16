/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.general.team;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.player.SMPPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeamInfo extends ServerCommand {


    public CommandTeamInfo() {
        super("info", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            List<String> names = new ArrayList<>();
            for (SMPPlayer player2 : player.getSmpTeam().getMembers()) {
                names.add(player2.getName());
            }
            player.sendMessage(TextFormatter.pluginMessage("Teams", String.format("""
                    Information for team **%s**:
                    Name: **%s**
                    Leader: **%s**
                    Members: **%s**""", player.getSmpTeam().getUuid(), player.getSmpTeam().getName(), player.getSmpTeam().getLeader().getName(), String.join("**, **", names))));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teams", "You must be in a team to use this command."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
