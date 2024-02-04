/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.Disguise;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandStaffMessage extends ServerCommand {

    public CommandStaffMessage() {
        super("messagestaff", Arrays.asList("smsg", "messagestaff", "ms", "msgs"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (player.getActiveMutes().size() == 0) {
                AuroraMCServerPlayer player1 = ServerAPI.getDisguisedPlayer(args.get(0));
                if (player1 != null) {
                    args.remove(0);
                    if (AuroraMCAPI.getFilter() == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                        return;
                    }
                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                    BaseComponent formatted = TextFormatter.formatStaffMessage(player, player1, message);
                    player1.sendMessage(TextFormatter.formatStaffMessageFrom(player, message));
                    player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                    player.sendMessage(TextFormatter.formatStaffMessageTo(player1, message));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                    player.setLastAdminMessage(player1.getUniqueId());
                    for (AuroraMCServerPlayer p : ServerAPI.getPlayers()) {
                        if (p != player1 && p != player) {
                            if (p.hasPermission("moderation")) {
                                p.sendMessage(formatted);
                            }
                        }
                    }
                    return;
                }
                AuroraMCServerPlayer target = ServerAPI.getPlayer(args.get(0));
                if (target != null) {
                    if (target.getActiveDisguise() != null) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", String.format("No match found for [**%s**]", args.get(0))));
                        return;
                    }
                    args.remove(0);
                    if (AuroraMCAPI.getFilter() == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                        return;
                    }
                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                    BaseComponent formatted = TextFormatter.formatStaffMessage(player, target, message);
                    target.sendMessage(TextFormatter.formatStaffMessageFrom(player, message));
                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                    player.sendMessage(TextFormatter.formatStaffMessageTo(target, message));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                    player.setLastAdminMessage(target.getUniqueId());
                    for (AuroraMCServerPlayer p : ServerAPI.getPlayers()) {
                        if (p != target && p != player) {
                            if (p.hasPermission("moderation")) {
                                p.sendMessage(formatted);
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
                                        player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                        return;
                                    }
                                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));

                                    ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.STAFF_MESSAGE, AuroraMCAPI.getDbManager().getServer(uuid), uuid.toString(), player.getUniqueId().toString(), message);
                                    CommunicationUtils.sendMessage(protocolMessage);

                                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    Disguise activeDisguise = AuroraMCAPI.getDbManager().getDisguise(uuid.toString());
                                    if (activeDisguise != null) {
                                        rank = activeDisguise.getRank();
                                        name = activeDisguise.getName();
                                    }

                                    BaseComponent formatted = TextFormatter.formatStaffMessage(player, rank, name, message);
                                    player.sendMessage(TextFormatter.formatStaffMessageTo(rank, name, message));
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);

                                    for (AuroraMCServerPlayer p : ServerAPI.getPlayers()) {
                                        if (p != target && p != player) {
                                            if (p.hasPermission("moderation")) {
                                                p.sendMessage(formatted);
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
                                        player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                        return;
                                    }
                                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));

                                    ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.STAFF_MESSAGE, AuroraMCAPI.getDbManager().getServer(uuid), uuid.toString(), player.getUniqueId().toString(), message);
                                    CommunicationUtils.sendMessage(protocolMessage);

                                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    Disguise activeDisguise = AuroraMCAPI.getDbManager().getDisguise(uuid.toString());
                                    if (activeDisguise != null) {
                                        rank = activeDisguise.getRank();
                                        name = activeDisguise.getName();
                                    }

                                    BaseComponent formatted = TextFormatter.formatStaffMessage(player, rank, name, message);
                                    player.sendMessage(TextFormatter.formatStaffMessageTo(rank, name, message));
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);

                                    for (AuroraMCServerPlayer p : ServerAPI.getPlayers()) {
                                        if (p != player) {
                                            if (p.hasPermission("moderation")) {
                                                p.sendMessage(formatted);
                                            }
                                        }
                                    }
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Staff Message", String.format("No match found for [**%s**]", args.get(0))));
                                }
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Staff Message", String.format("No match found for [**%s**]", args.get(0))));
                            }
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Staff Message", "You cannot send messages while muted!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Staff Message", "Invalid syntax. Correct syntax: **/staffmessage [player] [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
