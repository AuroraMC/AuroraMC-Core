/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.GameLog;
import net.auroramc.core.gui.misc.ViewGames;
import net.auroramc.core.gui.report.ViewReports;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandViewGames extends Command {
    public CommandViewGames() {
        super("viewgames", Arrays.asList("vg", "mygames"),Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1 && player.hasPermission("moderation") && args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game Manager", "Loading games for user **" + args.get(0) + "**, please wait..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id < 1) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game Manager", String.format("User [**%s**] has never joined the network, so cannot have any games played.", args.get(0))));
                        return;
                    }
                    List<GameLog> games = AuroraMCAPI.getDbManager().getGames(id);
                    ViewGames viewGames = new ViewGames(player, games, args.get(0));
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            viewGames.open(player);
                            AuroraMCAPI.openGUI(player, viewGames);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Game Manager", "Loading your games, please wait..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<GameLog> games = AuroraMCAPI.getDbManager().getGames(player.getId());
                    ViewGames viewGames = new ViewGames(player, games, null);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            viewGames.open(player);
                            AuroraMCAPI.openGUI(player, viewGames);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }

    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}