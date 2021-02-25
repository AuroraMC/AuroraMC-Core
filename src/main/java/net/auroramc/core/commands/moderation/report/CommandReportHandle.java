package net.auroramc.core.commands.moderation.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.report.HandleReportType;
import net.auroramc.core.api.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandReportHandle extends Command {


    public CommandReportHandle() {
        super("reporthandle", Arrays.asList("rh", "handlereport", "rhandle"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (player.getActiveReport() == null) {
            HandleReportType reportType = new HandleReportType(player);
            reportType.open(player);
            AuroraMCAPI.openGUI(player, reportType);
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "You are already handling a report. To abandon the report, use /reportclose then choose \"Abandon\"."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
