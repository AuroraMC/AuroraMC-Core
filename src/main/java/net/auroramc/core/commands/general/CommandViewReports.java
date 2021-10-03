package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.gui.report.ViewReports;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandViewReports extends Command {
    public CommandViewReports() {
        super("viewreports", Collections.singletonList("vr"),Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Loading your reports, please wait..."));
        new BukkitRunnable(){
            @Override
            public void run() {
                List<PlayerReport> reports = AuroraMCAPI.getDbManager().getSubmittedReports(player.getId());
                ViewReports viewReports = new ViewReports(player, reports);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        viewReports.open(player);
                        AuroraMCAPI.openGUI(player, viewReports);
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
