/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHome extends ServerCommand {


    public CommandHome() {
        super("home", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getHome() != null) {
            if (player.getSmpTeam() == null) {
                player.setBackLocation(new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw(), SMPLocation.Reason.HOME));
                switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                    case "OVERWORLD": {
                        if (player.getHome().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                            SMPLocation location = player.getHome();
                            player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                        } else {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    if (player.getHome().getDimension() == SMPLocation.Dimension.END) {
                                        out.writeUTF("SMPEnd");
                                    } else if (player.getHome().getDimension() == SMPLocation.Dimension.NETHER) {
                                        out.writeUTF("SMPNether");
                                    } else {
                                        return;
                                    }
                                    out.writeUTF(player.getUniqueId().toString());
                                    player.saveData();
                                    AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getHome().getDimension(), player.getHome().getX(), player.getHome().getY(), player.getHome().getZ(), player.getHome().getPitch(), player.getHome().getYaw(), SMPLocation.Reason.HOME));
                                    player.sendPluginMessage(out.toByteArray());
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                        break;
                    }
                    case "NETHER": {
                        if (player.getHome().getDimension() == SMPLocation.Dimension.NETHER) {
                            SMPLocation location = player.getHome();
                            player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                        } else {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    if (player.getHome().getDimension() == SMPLocation.Dimension.END) {
                                        out.writeUTF("SMPEnd");
                                    } else if (player.getHome().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                                        out.writeUTF("SMPOverworld");
                                    } else {
                                        return;
                                    }
                                    out.writeUTF(player.getUniqueId().toString());
                                    player.saveData();
                                    AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getHome().getDimension(), player.getHome().getX(), player.getHome().getY(), player.getHome().getZ(), player.getHome().getPitch(), player.getHome().getYaw(), SMPLocation.Reason.HOME));
                                    player.sendPluginMessage(out.toByteArray());
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                        break;
                    }
                    case "END": {
                        if (player.getHome().getDimension() == SMPLocation.Dimension.END) {
                            SMPLocation location = player.getHome();
                            player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                        } else {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    if (player.getHome().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                                        out.writeUTF("SMPOverworld");
                                    } else if (player.getHome().getDimension() == SMPLocation.Dimension.NETHER) {
                                        out.writeUTF("SMPNether");
                                    } else {
                                        return;
                                    }
                                    out.writeUTF(player.getUniqueId().toString());
                                    player.saveData();
                                    AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getHome().getDimension(), player.getHome().getX(), player.getHome().getY(), player.getHome().getZ(), player.getHome().getPitch(), player.getHome().getYaw(), SMPLocation.Reason.HOME));
                                    player.sendPluginMessage(out.toByteArray());
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                        break;
                    }
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Home", "You are currently in a team, so do not have a home. Use **/team home** to go to your team home."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Home", "You don't have a home set. Set a home using **/sethome**."));
        }
    }
    

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
