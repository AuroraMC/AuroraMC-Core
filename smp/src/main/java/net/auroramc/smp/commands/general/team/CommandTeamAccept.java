/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.team;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.player.SMPPlayer;
import net.auroramc.smp.api.player.team.SMPTeam;
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
