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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeamTransfer extends ServerCommand {


    public CommandTeamTransfer() {
        super("leave", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            if (player.getSmpTeam().getLeader().getUuid().equals(player.getUniqueId())) {
                if (args.size() == 1) {
                    if (args.get(0).equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "You cannot transfer the team to yourself."));
                        return;
                    }

                    AuroraMCServerPlayer target = ServerAPI.getDisguisedPlayer(args.get(0));

                    if (target == null) {
                        target = ServerAPI.getPlayer(args.get(0));
                        if (target == null || target.isDisguised()) {
                            player.sendMessage(TextFormatter.pluginMessage("Teams", "Player **" + args.get(0) + "** not found. Ensure they are in the same realm as you."));
                            return;
                        }
                    }

                    if (target.equals(player)) {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "You cannot transfer the team to yourself."));
                        return;
                    }

                    if (player.getSmpTeam().isMember(target)) {
                        player.getSmpTeam().setLeader(player.getSmpTeam().getMember(target.getUuid()), true);
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "Team transferred."));
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "You can only transfer your team to another member."));
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Teams", "Invalid syntax. Correct syntax: **/team invite [player]**"));
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
