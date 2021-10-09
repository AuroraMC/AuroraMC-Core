/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStaffReply extends Command {

    public CommandStaffReply() {
        super("staffreply", Collections.singletonList("rs"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (player.getActiveMutes().size() == 0) {
                if (player.getLastAdminMessaged() != null) {
                    AuroraMCPlayer target = AuroraMCAPI.getPlayer(player.getLastAdminMessaged());
                    if (target != null) {
                        String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                        BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(player, target, message);
                        target.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageFrom(player.getRank(), player.getName(), message));
                        player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageTo(target.getRank(), target.getName(), message));
                        player.setLastAdminMessage(target.getLastAdminMessaged());
                        target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p != target.getPlayer() && p != player.getPlayer()) {
                                if (AuroraMCAPI.getPlayer(p) != null) {
                                    if (AuroraMCAPI.getPlayer(p).hasPermission("moderation")) {
                                        p.spigot().sendMessage(formatted);
                                    }
                                }
                            }
                        }
                    } else {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                UUID uuid = player.getLastAdminMessaged();
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        args.remove(0);
                                        String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));

                                        ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.STAFF_MESSAGE, AuroraMCAPI.getDbManager().getServer(uuid), uuid.toString(), player.getPlayer().getUniqueId().toString(), message);
                                        CommunicationUtils.sendMessage(protocolMessage);

                                        String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                                        Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);

                                        BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(player, rank, name, message);
                                        player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageTo(rank, name, message));
                                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);

                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (p != player.getPlayer()) {
                                                if (AuroraMCAPI.getPlayer(p) != null) {
                                                    if (AuroraMCAPI.getPlayer(p).hasPermission("moderation")) {
                                                        p.spigot().sendMessage(formatted);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "The player you last messaged is no longer online!"));
                                        player.setLastAdminMessage(null);
                                    }
                            }
                        }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    }
                } else {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "You have not messaged anyone recently!"));
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "You cannot send messages while muted!"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "Invalid syntax. Correct syntax: **/staffreply [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
