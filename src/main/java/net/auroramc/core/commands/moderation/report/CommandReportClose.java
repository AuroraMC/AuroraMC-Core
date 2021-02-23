package net.auroramc.core.commands.moderation.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.gui.report.CloseNameReport;
import net.auroramc.core.gui.report.CloseReport;
import net.auroramc.core.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandReportClose extends Command {


    public CommandReportClose() {
        super("reportclose", Arrays.asList("rc", "closereport", "rclose"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (player.getActiveReport() != null) {
            if (player.getActiveReport().getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME) {
                CloseNameReport closeReport = new CloseNameReport(player);
                closeReport.open(player);
                AuroraMCAPI.openGUI(player, closeReport);
            } else {
                CloseReport closeReport = new CloseReport(player);
                closeReport.open(player);
                AuroraMCAPI.openGUI(player, closeReport);
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You currently do not have an active report."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
