/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

public class CommandTeamAccept extends ServerCommand {


    public CommandTeamAccept() {
        super("accept", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getPendingInvite() != null) {
            if (player.getSmpTeam() != null) {
                player.getSmpTeam().removeMember(player.getUniqueId(), true);
            }
            SMPTeam team = ServerAPI.getLoadedTeams().get(player.getPendingInvite());
            team.addMember(new SMPPlayer(player.getId(), player.getName(), player.getUuid(), player, player.getRank()), true);
            player.setSmpTeam(team);
            player.setPendingInvite(null);
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teams", "You must have a pending invite to use this command."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
