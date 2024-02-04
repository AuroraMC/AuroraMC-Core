/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandBack extends ServerCommand {


    public CommandBack() {
        super("back", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getBackLocation() != null) {
            switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                case "OVERWORLD": {
                    if (player.getBackLocation().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                        SMPLocation location = player.getBackLocation();
                        player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                    } else {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                if (player.getBackLocation().getDimension() == SMPLocation.Dimension.END) {
                                    out.writeUTF("SMPEnd");
                                } else if (player.getBackLocation().getDimension() == SMPLocation.Dimension.NETHER) {
                                    out.writeUTF("SMPNether");
                                } else {
                                    return;
                                }
                                out.writeUTF(player.getUniqueId().toString());
                                player.saveData();
                                AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getBackLocation().getDimension(), player.getBackLocation().getX(), player.getBackLocation().getY(), player.getBackLocation().getZ(), player.getBackLocation().getPitch(), player.getBackLocation().getYaw(), SMPLocation.Reason.HOME));
                                player.setBackLocation(null);
                                player.sendPluginMessage(out.toByteArray());
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                    break;
                }
                case "NETHER": {
                    if (player.getBackLocation().getDimension() == SMPLocation.Dimension.NETHER) {
                        SMPLocation location = player.getBackLocation();
                        player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                    } else {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                if (player.getBackLocation().getDimension() == SMPLocation.Dimension.END) {
                                    out.writeUTF("SMPEnd");
                                } else if (player.getBackLocation().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                                    out.writeUTF("SMPOverworld");
                                } else {
                                    return;
                                }
                                out.writeUTF(player.getUniqueId().toString());
                                player.saveData();
                                AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getBackLocation().getDimension(), player.getBackLocation().getX(), player.getBackLocation().getY(), player.getBackLocation().getZ(), player.getBackLocation().getPitch(), player.getBackLocation().getYaw(), SMPLocation.Reason.HOME));
                                player.setBackLocation(null);
                                player.sendPluginMessage(out.toByteArray());
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                    break;
                }
                case "END": {
                    if (player.getBackLocation().getDimension() == SMPLocation.Dimension.END) {
                        SMPLocation location = player.getBackLocation();
                        player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                    } else {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                if (player.getBackLocation().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                                    out.writeUTF("SMPOverworld");
                                } else if (player.getBackLocation().getDimension() == SMPLocation.Dimension.NETHER) {
                                    out.writeUTF("SMPNether");
                                } else {
                                    return;
                                }
                                out.writeUTF(player.getUniqueId().toString());
                                player.saveData();
                                AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getBackLocation().getDimension(), player.getBackLocation().getX(), player.getBackLocation().getY(), player.getBackLocation().getZ(), player.getBackLocation().getPitch(), player.getBackLocation().getYaw(), SMPLocation.Reason.HOME));
                                player.setBackLocation(null);
                                player.sendPluginMessage(out.toByteArray());
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                    break;
                }
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teleport", "You do not have anywhere to teleport back to."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
