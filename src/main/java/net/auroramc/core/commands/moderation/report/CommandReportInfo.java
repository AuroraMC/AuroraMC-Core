/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandReportInfo extends Command {


    public CommandReportInfo() {
        super("reporthandle", Collections.singletonList("ri"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            int id;
            try {
                id = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Invalid syntax. Correct syntax: **/reportinfo [report number]**"));
                return;
            }
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Loading report, please wait..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    PlayerReport report = AuroraMCAPI.getDbManager().getReport(id);
                    if (report == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "That report could not be found."));
                        return;
                    }

                    player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatReportMessage(report));
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "Invalid syntax. Correct syntax: **/reportinfo [report number]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
