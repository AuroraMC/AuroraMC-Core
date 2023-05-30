/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.report.ViewReports;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandViewReports extends ServerCommand {
    public CommandViewReports() {
        super("viewreports", Arrays.asList("vr", "myreports"),Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1 && player.hasPermission("moderation") && args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
            player.sendMessage(TextFormatter.pluginMessage("Reports", "Loading reports for user **" + args.get(0) + "**, please wait..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("User [**%s**] has never joined the network, so cannot have any reports.", args.get(0))));
                        return;
                    }
                    List<PlayerReport> reports = AuroraMCAPI.getDbManager().getSubmittedReports(id);
                    ViewReports viewReports = new ViewReports(player, reports, args.get(0));
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            viewReports.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Reports", "Loading your reports, please wait..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<PlayerReport> reports = AuroraMCAPI.getDbManager().getSubmittedReports(player.getId());
                    ViewReports viewReports = new ViewReports(player, reports, player.getName());
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            viewReports.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
