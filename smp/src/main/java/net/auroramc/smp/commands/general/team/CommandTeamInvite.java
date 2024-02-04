/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.team;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeamInvite extends ServerCommand {


    public CommandTeamInvite() {
        super("invite", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            if (player.getSmpTeam().getLeader().getUuid().equals(player.getUniqueId())) {
                if (args.size() == 1) {
                    if (args.get(0).equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "You cannot invite yourself to the team."));
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
                        player.sendMessage(TextFormatter.pluginMessage("Teams", "You cannot invite yourself to the team."));
                        return;
                    }

                    target.setPendingInvite(player.getSmpTeam().getUuid());
                    target.sendMessage(TextFormatter.pluginMessage("Teams", "You have received a team invite from player **" + player.getByDisguiseName() + "**. Use **/team accept** to accept the team invite. This invite expires in 1 minute."));
                    player.sendMessage(TextFormatter.pluginMessage("Teams", "You sent a team invite to player **" + target.getByDisguiseName() + "**. The invite expires in 1 minute."));
                    AuroraMCServerPlayer finalTarget = target;
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if (finalTarget.isOnline() && finalTarget.getPendingInvite() != null) {
                                finalTarget.setPendingInvite(null);
                                finalTarget.sendMessage(TextFormatter.pluginMessage("Teams", "Your team invite from player **" + player.getByDisguiseName() + "** has expired."));
                                player.sendMessage(TextFormatter.pluginMessage("Teams", "Your team invite to player **" + finalTarget.getByDisguiseName() + "** has expired."));
                            }


                        }
                    }.runTaskLater(ServerAPI.getCore(), 1200);
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
