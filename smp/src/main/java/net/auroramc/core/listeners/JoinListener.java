/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.Info;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.punishments.Ban;
import net.auroramc.api.punishments.Rule;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.api.utils.TimeLength;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.TabCompleteInjector;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Optional;

public class JoinListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        Ban ban = AuroraMCAPI.getDbManager().getBan(e.getUniqueId().toString());
        if (ban != null) {
            TimeLength length = new TimeLength((ban.getExpire() - System.currentTimeMillis())/3600000d);
            Rule rule = AuroraMCAPI.getRules().getRule(ban.getRuleID());

            switch (ban.getStatus()) {
                case 2:
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, TextFormatter.pluginMessageRaw("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s [Awaiting Approval]**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rYour punishment has been applied by a Junior Moderator, and is severe enough to need approval\n" +
                            "&rfrom a Staff Management member to ensure that the punishment applied was correct. When it is\n" +
                            "&rapproved, the full punishment length will automatically apply to you. If this punishment is\n" +
                            "&rdenied for being false, **it will automatically be removed**, and the punishment will automatically\n" +
                            "&rbe removed from your Punishment History.\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
                case 3:
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, TextFormatter.pluginMessageRaw("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s [SM Approved]**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
                default:
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, TextFormatter.pluginMessageRaw("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
            }
        }

        Info serverInfo = AuroraMCAPI.getInfo();
        if (serverInfo == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, TextFormatter.pluginMessageRaw("Server Manager", "I don't know what server I am! Please forward this to an admin!"));
            return;
        }

        e.allow();
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        ServerAPI.newPlayer(e.getPlayer(), new AuroraMCServerPlayer(e.getPlayer()));
        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "join", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name() + "\n" + ((ServerInfo)AuroraMCAPI.getInfo()).getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        TabCompleteInjector.onJoin(e.getPlayer());
        e.setJoinMessage(null);
        AuroraMCServerPlayer serverPlayer = ServerAPI.getPlayer(e.getPlayer());
        serverPlayer.loadPlayerState();
    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent e) {
        AuroraMCServerPlayer player = ServerAPI.getPlayer(e.getPlayer());
        SMPLocation location = player.getStartLocation();
        if (location == null) {
            if (((ServerInfo)AuroraMCAPI.getInfo()).getServerType().getString("smp_type").equals("OVERWORLD")) {
                e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), 0.5, 63, 0.5));
                player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "Welcome to §5§lNuttersSMP§r, brought to you by §b§lThe AuroraMC Network§r! There are 4 basic rules, these are:\n" +
                        " - No Griefing\n" +
                        " - No Stealing\n" +
                        " - No Cheating\n" +
                        " - Be respectful.\n\nWe hope you enjoy your time here! If you're playing with friends, use **/team** to create a team!"));
            } else {
                player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "Sorry, an error occurred while trying to join this dimension, connecting you a Lobby..."));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Lobby");
                out.writeUTF(player.getUniqueId().toString());
                player.sendPluginMessage(out.toByteArray());
            }
        } else {
            if (((ServerInfo)AuroraMCAPI.getInfo()).getServerType().getString("smp_type").equals("OVERWORLD")) {
                if (location.getDimension() == SMPLocation.Dimension.END) {
                    if (location.getReason() == SMPLocation.Reason.HOME) {
                        e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                    } else if (player.getBedSpawnLocation() != null) {
                        e.setSpawnLocation(player.getBedSpawnLocation());
                    } else {
                        e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), 0.5, 63, 0.5));
                    }
                } else if (location.getDimension() == SMPLocation.Dimension.NETHER) {
                    if (location.getReason() == SMPLocation.Reason.DEATH) {
                        if (player.getBedSpawnLocation() != null) {
                            e.setSpawnLocation(player.getBedSpawnLocation());
                        } else {
                            e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), 0.5, 63, 0.5));
                        }
                    } else if (location.getReason() == SMPLocation.Reason.HOME) {
                        e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                    } else {
                        WorldServer world = ((CraftWorld)Bukkit.getWorld("smp")).getHandle();
                        Location location1 = new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                        AuroraMCAPI.getLogger().info("Finding portal at: " + (location1.getBlockX()*8) + " " + location1.getBlockY() + " " + (location1.getBlockZ()*8));
                        Optional<BlockUtil.Rectangle> opt = world.o().findPortalAround(new BlockPosition(location1.getBlockX()*8, location1.getBlockY(), location1.getBlockZ()*8), world.p_(), 128);
                        if (opt.isEmpty()) {
                            AuroraMCAPI.getLogger().info("No portal found, creating portal...");
                            EnumDirection.EnumAxis axis = EnumDirection.EnumAxis.a;
                            if (Math.abs(location.getYaw()) >= 80) {
                                axis = EnumDirection.EnumAxis.c;
                            }
                            opt = world.o().createPortal(new BlockPosition(location1.getBlockX()*8, location1.getBlockY(), location1.getBlockZ()*8), axis, player.getCraft().getHandle(), 16);
                        }
                        if (opt.isPresent()) {
                            BlockUtil.Rectangle rectangle = opt.get();
                            AuroraMCAPI.getLogger().info("Portal found at: " + rectangle.a.u() + " " + rectangle.a.v() + " " + rectangle.a.w());
                            e.setSpawnLocation(new Location(world.getWorld(), rectangle.a.u(), rectangle.a.v(), rectangle.a.w(), location.getYaw(), location.getPitch()));
                        } else {
                            if (player.getBedSpawnLocation() != null) {
                                e.setSpawnLocation(player.getBedSpawnLocation());
                            } else {
                                e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), 0.5, 63, 0.5));
                            }
                            player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "An error occurred trying to generate a nether portal. You were teleported to your spawnpoint."));
                        }
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "Welcome back to §5§lNuttersSMP§r, brought to you by §b§lThe AuroraMC Network§r! There are 4 basic rules, these are:\n" +
                            " - No Griefing\n" +
                            " - No Stealing\n" +
                            " - No Cheating\n" +
                            " - Be respectful.\n\nWe hope you enjoy your time here! If you're playing with friends, use **/team** to create a team!"));
                    e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                }
            } else if (((ServerInfo)AuroraMCAPI.getInfo()).getServerType().getString("smp_type").equals("NETHER")) {
                if (location.getDimension() == SMPLocation.Dimension.NETHER || location.getReason() == SMPLocation.Reason.HOME) {
                    e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                } else {
                    WorldServer world = ((CraftWorld)Bukkit.getWorld("smp")).getHandle();
                    Location location1 = new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                    Optional<BlockUtil.Rectangle> opt = world.o().findPortalAround(new BlockPosition(location1.getBlockX()/8, location1.getBlockY(), location1.getBlockZ()/8), world.p_(), 16);
                    EnumDirection.EnumAxis axis = EnumDirection.EnumAxis.a;
                    if (Math.abs(location.getYaw()) >= 80) {
                        axis = EnumDirection.EnumAxis.c;
                    }
                    if (opt.isEmpty()) {
                        opt = world.o().createPortal(new BlockPosition(location1.getBlockX()/8, location1.getBlockY(), location1.getBlockZ()/8), axis, player.getCraft().getHandle(), 16);
                    }
                    if (opt.isPresent()) {
                        BlockUtil.Rectangle rectangle = opt.get();
                        e.setSpawnLocation(new Location(world.getWorld(), rectangle.a.u(), rectangle.a.v(), rectangle.a.w(), location.getYaw(), location.getPitch()));
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "An error occurred trying to generate a nether portal, connecting you to a Lobby..."));
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Lobby");
                        out.writeUTF(player.getUniqueId().toString());
                        player.sendPluginMessage(out.toByteArray());
                    }
                }
            } else {
                if (location.getDimension() == SMPLocation.Dimension.END || location.getReason() == SMPLocation.Reason.HOME) {
                    e.setSpawnLocation(new Location(Bukkit.getWorld("smp"), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                } else {
                    e.setSpawnLocation(Bukkit.getWorld("smp").getSpawnLocation());
                }
            }
        }
    }

}
