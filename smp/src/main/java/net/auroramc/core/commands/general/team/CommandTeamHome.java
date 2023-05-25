/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general.team;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.player.SMPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandTeamHome extends ServerCommand {


    public CommandTeamHome() {
        super("home", Collections.emptyList(), Collections.emptyList(), true, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getSmpTeam() != null) {
            if (player.getSmpTeam().getHome() != null) {
                player.setBackLocation(new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw(), SMPLocation.Reason.HOME));
                switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                    case "OVERWORLD": {
                        if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                            SMPLocation location = player.getSmpTeam().getHome();
                            player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                        } else {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.END) {
                                        out.writeUTF("SMPEnd");
                                    } else if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.NETHER) {
                                        out.writeUTF("SMPNether");
                                    } else {
                                        return;
                                    }
                                    out.writeUTF(player.getUniqueId().toString());
                                    player.saveData();
                                    AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getSmpTeam().getHome().getDimension(), player.getSmpTeam().getHome().getX(), player.getSmpTeam().getHome().getY(), player.getSmpTeam().getHome().getZ(), player.getSmpTeam().getHome().getPitch(), player.getSmpTeam().getHome().getYaw(), SMPLocation.Reason.HOME));
                                    player.sendPluginMessage(out.toByteArray());
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                        break;
                    }
                    case "NETHER": {
                        if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.NETHER) {
                            SMPLocation location = player.getSmpTeam().getHome();
                            player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                        } else {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.END) {
                                        out.writeUTF("SMPEnd");
                                    } else if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                                        out.writeUTF("SMPOverworld");
                                    } else {
                                        return;
                                    }
                                    out.writeUTF(player.getUniqueId().toString());
                                    player.saveData();
                                    AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getSmpTeam().getHome().getDimension(), player.getSmpTeam().getHome().getX(), player.getSmpTeam().getHome().getY(), player.getSmpTeam().getHome().getZ(), player.getSmpTeam().getHome().getPitch(), player.getSmpTeam().getHome().getYaw(), SMPLocation.Reason.HOME));
                                    player.sendPluginMessage(out.toByteArray());
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                        break;
                    }
                    case "END": {
                        if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.END) {
                            SMPLocation location = player.getSmpTeam().getHome();
                            player.teleport(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                        } else {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                    if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.OVERWORLD) {
                                        out.writeUTF("SMPOverworld");
                                    } else if (player.getSmpTeam().getHome().getDimension() == SMPLocation.Dimension.NETHER) {
                                        out.writeUTF("SMPNether");
                                    } else {
                                        return;
                                    }
                                    out.writeUTF(player.getUniqueId().toString());
                                    player.saveData();
                                    AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(player.getSmpTeam().getHome().getDimension(), player.getSmpTeam().getHome().getX(), player.getSmpTeam().getHome().getY(), player.getSmpTeam().getHome().getZ(), player.getSmpTeam().getHome().getPitch(), player.getSmpTeam().getHome().getYaw(), SMPLocation.Reason.HOME));
                                    player.sendPluginMessage(out.toByteArray());
                                }
                            }.runTaskAsynchronously(ServerAPI.getCore());
                        }
                        break;
                    }
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Teams", "Your team does not have a home set, the leader of the team needs to use **/sethome** to set the team home."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teams", "You must be in a team to use this command."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
