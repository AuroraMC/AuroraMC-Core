/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.general.team;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.player.SMPPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandTeamRemove extends ServerCommand {


    public CommandTeamRemove() {
        super("remove", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            if (player.getSmpTeam().getLeader().getUuid().equals(player.getUniqueId())) {
                if (args.size() == 1) {
                    if (args.get(0).equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "You cannot remove yourself to the team."));
                        return;
                    }

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                            if (uuid == null) {
                                player.sendMessage(TextFormatter.pluginMessage("Teams", "That player was not found."));
                                return;
                            }
                            SMPPlayer target = player.getSmpTeam().getMember(uuid);

                            if (target == null) {
                                player.sendMessage(TextFormatter.pluginMessage("Teams", "That player is not in your team."));
                                return;
                            }

                            if (target.getUuid().equals(player.getUuid())) {
                                player.sendMessage(TextFormatter.pluginMessage("Teams", "You cannot remove yourself to the team."));
                                return;
                            }

                            player.getSmpTeam().removeMember(uuid, true);
                            player.sendMessage(TextFormatter.pluginMessage("Teams", "You removed player **" + target.getName() + "** from the team."));
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
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
