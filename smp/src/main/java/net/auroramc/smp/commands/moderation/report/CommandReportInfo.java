/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation.report;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandReportInfo extends ServerCommand {


    public CommandReportInfo() {
        super("reportinfo", Collections.singletonList("ri"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            int id;
            try {
                id = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Reports", "Invalid syntax. Correct syntax: **/reportinfo [report number]**"));
                return;
            }
            player.sendMessage(TextFormatter.pluginMessage("Reports", "Loading report, please wait..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    PlayerReport report = AuroraMCAPI.getDbManager().getReport(id);
                    if (report == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Reports", "That report could not be found."));
                        return;
                    }

                    player.sendMessage(TextFormatter.formatReportInfoMessage(report));
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Reports", "Invalid syntax. Correct syntax: **/reportinfo [report number]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
