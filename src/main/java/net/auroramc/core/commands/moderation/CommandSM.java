package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Mentor;
import net.auroramc.core.gui.punish.staffmanagement.MenteeList;
import net.auroramc.core.gui.punish.staffmanagement.MentorList;
import net.auroramc.core.api.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandSM extends Command {


    public CommandSM() {
        super("sm", Arrays.asList("approval","punishapproval","mentees","mentors","staffmanagement"), Arrays.asList(Permission.STAFF_MANAGEMENT, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
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
                                AuroraMCAPI.openGUI(player, gui);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                        return;
                    }
                }

                //Open normal GUI
                MentorList gui = new MentorList(player, mentors);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        gui.open(player);
                        AuroraMCAPI.openGUI(player, gui);
                    }
                }.runTask(AuroraMCAPI.getCore());
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
