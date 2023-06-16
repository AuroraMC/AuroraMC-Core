/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.general.team;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeamRename extends ServerCommand {


    public CommandTeamRename() {
        super("rename", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            if (player.getSmpTeam().getLeader().getUuid().equals(player.getUniqueId())) {
                if (args.size() >= 1) {
                    String name = String.join(" ", args);
                    String filtered = AuroraMCAPI.getFilter().filter(name);
                    if (!filtered.equals(name)) {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "The name you chose would be filtered, so the rename of the team has been blocked."));
                        return;
                    }

                    if (filtered.length() >= 16) {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "The prefix you chose is too long. Your prefix must be a maximum of 16 characters."));
                        return;
                    }

                    player.getSmpTeam().setName(name, true);
                    player.sendMessage(TextFormatter.pluginMessage("Teams", "Team successfully renamed!"));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Teams", "Invalid syntax. Correct syntax: **/team rename [name]**"));
                }
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
