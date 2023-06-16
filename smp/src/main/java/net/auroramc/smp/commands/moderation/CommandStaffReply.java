/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStaffReply extends ServerCommand {

    public CommandStaffReply() {
        super("staffreply", Collections.singletonList("rs"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (player.getActiveMutes().size() == 0) {
                if (player.getLastAdminMessaged() != null) {
                    AuroraMCServerPlayer target = ServerAPI.getPlayer(player.getLastAdminMessaged());
                    if (target != null) {
                        if (AuroraMCAPI.getFilter() == null) {
                            player.sendMessage(TextFormatter.pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                            return;
                        }
                        String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                        BaseComponent formatted = TextFormatter.formatStaffMessage(player, target, message);
                        Rank playerRank = player.getRank();
                        Rank targetRank = target.getRank();
                        String playerName = player.getName();
                        String targetName = target.getName();
                        if (player.getActiveDisguise() != null) {
                            playerName = player.getActiveDisguise().getName();
                            playerRank = player.getActiveDisguise().getRank();
                        }
                        if (target.getActiveDisguise() != null) {
                            targetName = target.getActiveDisguise().getName();
                            targetRank = target.getActiveDisguise().getRank();
                        }

                        target.sendMessage(TextFormatter.formatStaffMessageFrom(playerRank, playerName, message));
                        player.sendMessage(TextFormatter.formatStaffMessageTo(targetRank, targetName, message));
                        player.setLastAdminMessage(target.getUniqueId());
                        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
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
                                UUID uuid = player.getLastAdminMessaged();
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
                                        player.sendMessage(TextFormatter.pluginMessage("Staff Message", "The player you last messaged is no longer online!"));
                                        player.setLastAdminMessage(null);
                                    }
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Staff Message", "You have not messaged anyone recently!"));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Staff Message", "You cannot send messages while muted!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Staff Message", "Invalid syntax. Correct syntax: **/staffreply [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
