/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.UUIDUtil;
import net.auroramc.core.gui.stats.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStats extends Command {
    public CommandStats() {
        super("stats", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            Player target = Bukkit.getPlayer(args.get(0));
            if (target != null) {
                AuroraMCPlayer aTarget = AuroraMCAPI.getPlayer(target);
                if (aTarget != null) {
                    Stats achievements = new Stats(player, target.getName(), aTarget.getStats(), aTarget.getActiveSubscription(), aTarget.getId());
                    achievements.open(player);
                    AuroraMCAPI.openGUI(player, achievements);
                    return;
                }
            }

            if (player.hasPermission("moderation")) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Statistics", "Attempting offline stats lookup..."));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        UUID uuid = UUIDUtil.getUUID(args.get(0));
                        if (uuid == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Statistics", String.format("No matches for [**%s**]", args.get(0))));
                            return;
                        }

                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                        if (id < 1) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Statistics", String.format("User [**%s**] has never joined the network, so have no stats.", args.get(0))));
                            return;
                        }

                        PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid);
                        PlusSubscription subscription = new PlusSubscription(uuid);
                        Stats statsMenu = new Stats(player, args.get(0), stats, subscription, id);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                statsMenu.open(player);
                                AuroraMCAPI.openGUI(player, statsMenu);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                return;
            }

            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Statistics", "That players statistics are still loading, please wait to use this command!"));
        } else {
            if (player.getStats() != null) {
                Stats achievements = new Stats(player, player.getName(), player.getStats(), player.getActiveSubscription(), player.getId());
                achievements.open(player);
                AuroraMCAPI.openGUI(player, achievements);
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Statistics", "Your statistics are still loading, please wait to use this command!"));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
