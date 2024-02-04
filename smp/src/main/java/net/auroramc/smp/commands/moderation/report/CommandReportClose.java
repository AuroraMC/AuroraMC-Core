/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation.report;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.gui.report.CloseNameReport;
import net.auroramc.smp.gui.report.CloseReport;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandReportClose extends ServerCommand {


    public CommandReportClose() {
        super("reportclose", Arrays.asList("rc", "closereport", "rclose"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getActiveReport() != null) {
            if (player.getActiveReport().getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME) {
                CloseNameReport closeReport = new CloseNameReport(player);
                closeReport.open(player);
            } else {
                CloseReport closeReport = new CloseReport(player);
                closeReport.open(player);
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Reports", "You currently do not have an active report."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
