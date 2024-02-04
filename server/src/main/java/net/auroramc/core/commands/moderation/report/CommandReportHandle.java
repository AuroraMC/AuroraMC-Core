/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation.report;

import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.report.HandleReportType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandReportHandle extends ServerCommand {


    public CommandReportHandle() {
        super("reporthandle", Arrays.asList("rh", "handlereport", "rhandle"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getActiveReport() == null) {
            HandleReportType reportType = new HandleReportType(player);
            reportType.open(player);
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Reports", "You are already handling a report. To abandon the report, use /reportclose then choose \"Abandon\"."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
