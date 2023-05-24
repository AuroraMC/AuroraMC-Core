/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general.team;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.player.SMPPlayer;
import net.auroramc.core.api.player.team.SMPTeam;
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
                    Prefix: **%s**
                    Leader: **%s**
                    Members: **%s**""", player.getSmpTeam().getUuid(), player.getSmpTeam().getName(), TextFormatter.convert(player.getSmpTeam().getPrefix()), player.getSmpTeam().getLeader().getName(), String.join("**, **", names))));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teams", "You must be in a team to use this command."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
