/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.player.Mentor;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.punish.staffmanagement.MenteeList;
import net.auroramc.core.gui.punish.staffmanagement.MentorList;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandSM extends ServerCommand {


    public CommandSM() {
        super("sm", Arrays.asList("approval","punishapproval","mentees","mentors","staffmanagement"), Arrays.asList(Permission.STAFF_MANAGEMENT, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Mentor> mentors = AuroraMCAPI.getDbManager().getMentors();
                for (Mentor mentor : mentors) {
                    if (mentor.getId() == player.getId()) {
                        MenteeList gui = new MenteeList(player, mentors, mentor);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                gui.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                        return;
                    }
                }

                //Open normal GUI
                MentorList gui = new MentorList(player, mentors);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gui.open(player);
                    }
                }.runTask(ServerAPI.getCore());
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
