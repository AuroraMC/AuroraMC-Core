/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.api.utils.UUIDUtil;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.stats.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStats extends ServerCommand {
    public CommandStats() {
        super("stats", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            Player target = Bukkit.getPlayer(args.get(0));
            if (target != null) {
                AuroraMCServerPlayer aTarget = ServerAPI.getPlayer(target);
                if (aTarget != null) {
                    Stats achievements = new Stats(player, target.getName(), aTarget.getStats(), aTarget.getActiveSubscription(), aTarget.getId());
                    achievements.open(player);
                    return;
                }
            }

            if (player.hasPermission("moderation")) {
                player.sendMessage(TextFormatter.pluginMessage("Statistics", "Attempting offline stats lookup..."));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        UUID uuid = UUIDUtil.getUUID(args.get(0));
                        if (uuid == null) {
                            player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("No matches for [**%s**]", args.get(0))));
                            return;
                        }

                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                        if (id < 1) {
                            player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("User [**%s**] has never joined the network, so have no stats.", args.get(0))));
                            return;
                        }

                        PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid, id);
                        PlusSubscription subscription = new PlusSubscription(uuid);
                        Stats statsMenu = new Stats(player, args.get(0), stats, subscription, id);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                statsMenu.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                return;
            }

            player.sendMessage(TextFormatter.pluginMessage("Statistics", "That players statistics are still loading, please wait to use this command!"));
        } else {
            if (player.getStats() != null) {
                Stats achievements = new Stats(player, player.getName(), player.getStats(), player.getActiveSubscription(), player.getId());
                achievements.open(player);
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Statistics", "Your statistics are still loading, please wait to use this command!"));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
