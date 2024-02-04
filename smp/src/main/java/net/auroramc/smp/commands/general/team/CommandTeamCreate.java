/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.team;

import net.auroramc.api.AuroraMCAPI;
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
import java.util.UUID;

public class CommandTeamCreate extends ServerCommand {


    public CommandTeamCreate() {
        super("create", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() == null) {
            if (args.size() >= 1) {
                String name = String.join(" ", args);
                String filtered = AuroraMCAPI.getFilter().filter(name);
                if (!filtered.equals(name)) {
                    player.sendMessage(TextFormatter.pluginMessage("Teams", "The name you chose would be filtered, so the creation of the team has been blocked."));
                    return;
                }

                SMPTeam team = new SMPTeam(UUID.randomUUID(), name, name, new SMPPlayer(player.getId(), player.getName(), player.getUuid(), player, player.getRank()));
                ServerAPI.getLoadedTeams().put(team.getUuid(), team);
                player.setSmpTeam(team);
                for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                    player1.updateNametag(player);
                }
                player.sendMessage(TextFormatter.pluginMessage("Teams", "Team successfully created! Use /team invite to invite players to your team!"));
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Teams", "Invalid syntax. Correct syntax: **/team create [name]**"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teams", "You must not be in a team to use this command."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
