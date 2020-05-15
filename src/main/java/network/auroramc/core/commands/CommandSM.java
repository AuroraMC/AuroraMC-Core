package network.auroramc.core.commands;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.Mentor;
import network.auroramc.core.gui.punish.staffmanagement.MenteeList;
import network.auroramc.core.gui.punish.staffmanagement.MentorList;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSM extends Command {


    public CommandSM() {
        super("sm", new ArrayList<>(Arrays.asList("approval","punishapproval","mentees","mentors","staffmanagement")), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("staffmanagement"),AuroraMCAPI.getPermissions().get("admin"))), false, null);
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
}
