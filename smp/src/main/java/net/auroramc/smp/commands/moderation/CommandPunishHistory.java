/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.gui.punish.PunishmentHistoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandPunishHistory extends ServerCommand {


    public CommandPunishHistory() {
        super("punishhistory", Arrays.asList("ph","history","punishmenthistory"), Collections.singletonList(Permission.PLAYER), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            if (player.hasPermission("moderation")) {
                if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                            if (id < 1) {
                                player.sendMessage(TextFormatter.pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot have received a punishment.", args.get(0))));
                                return;
                            }

                            UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);

                            String name = args.remove(0);
                            PunishmentHistoryGUI gui = new PunishmentHistoryGUI(player, id, name, ((args.size() > 0)?String.join(" ", args):null), uuid);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    gui.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Punish","Invalid syntax. Correct syntax: **/punishhistory [player] [reason]**"));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Punishments","You cannot view the punishment history of another player."));
            }
        } else {
            new BukkitRunnable(){
                @Override
                public void run() {
                    PunishmentHistoryGUI gui = new PunishmentHistoryGUI(player, player.getId(), player.getName(), null, null);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            gui.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            if (player.hasPermission("moderation")) {
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    if (player1.getName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                        completions.add(player1.getName());
                    }
                }
            }
        }
        return completions;
    }
}
