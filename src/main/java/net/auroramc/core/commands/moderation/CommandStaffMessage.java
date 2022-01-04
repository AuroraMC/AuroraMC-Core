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
import net.auroramc.core.api.players.Disguise;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandStaffMessage extends Command {

    public CommandStaffMessage() {
        super("messagestaff", Arrays.asList("smsg", "messagestaff", "ms", "msgs"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (player.getActiveMutes().size() == 0) {
                AuroraMCPlayer player1 = AuroraMCAPI.getDisguisedPlayer(args.get(0));
                if (player1 != null) {
                    args.remove(0);
                    if (AuroraMCAPI.getFilter() == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                        return;
                    }
                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                    BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(player, player1, message);
                    player1.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageFrom(player, message));
                    player1.getPlayer().playSound(player1.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                    player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageTo(player1, message));
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                    player.setLastAdminMessage(player1.getPlayer().getUniqueId());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p != player1.getPlayer() && p != player.getPlayer()) {
                            if (AuroraMCAPI.getPlayer(p) != null) {
                                if (AuroraMCAPI.getPlayer(p).hasPermission("moderation")) {
                                    p.spigot().sendMessage(formatted);
                                }
                            }
                        }
                    }
                    return;
                }
                AuroraMCPlayer target = AuroraMCAPI.getPlayer(args.get(0));
                if (target != null) {
                    if (target.getActiveDisguise() != null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", String.format("No match found for [**%s**]", args.get(0))));
                        return;
                    }
                    args.remove(0);
                    if (AuroraMCAPI.getFilter() == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                        return;
                    }
                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                    BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(player, target, message);
                    target.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageFrom(player, message));
                    target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                    player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageTo(target, message));
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                    player.setLastAdminMessage(target.getPlayer().getUniqueId());
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
                            if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    args.remove(0);
                                    if (AuroraMCAPI.getFilter() == null) {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                        return;
                                    }
                                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));

                                    ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.STAFF_MESSAGE, AuroraMCAPI.getDbManager().getServer(uuid), uuid.toString(), player.getPlayer().getUniqueId().toString(), message);
                                    CommunicationUtils.sendMessage(protocolMessage);

                                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    Disguise activeDisguise = AuroraMCAPI.getDbManager().getDisguise(uuid.toString());
                                    if (activeDisguise != null) {
                                        rank = activeDisguise.getRank();
                                        name = activeDisguise.getName();
                                    }

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
                                }
                            }
                            UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                            if (uuid != null) {
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {

                                    args.remove(0);
                                    if (AuroraMCAPI.getFilter() == null) {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                        return;
                                    }
                                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));

                                    ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.STAFF_MESSAGE, AuroraMCAPI.getDbManager().getServer(uuid), uuid.toString(), player.getPlayer().getUniqueId().toString(), message);
                                    CommunicationUtils.sendMessage(protocolMessage);

                                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    Disguise activeDisguise = AuroraMCAPI.getDbManager().getDisguise(uuid.toString());
                                    if (activeDisguise != null) {
                                        rank = activeDisguise.getRank();
                                        name = activeDisguise.getName();
                                    }

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
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", String.format("No match found for [**%s**]", args.get(0))));
                                }
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", String.format("No match found for [**%s**]", args.get(0))));
                            }
                        }
                    }.runTaskAsynchronously(AuroraMCAPI.getCore());
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "You cannot send messages while muted!"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "Invalid syntax. Correct syntax: **/staffmessage [player] [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
