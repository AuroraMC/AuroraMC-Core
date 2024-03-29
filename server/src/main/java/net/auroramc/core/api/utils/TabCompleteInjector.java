/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.utils;

import io.netty.channel.*;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.command.Command;
import net.auroramc.api.permissions.Permission;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.events.player.PlayerFakePlayerAttackEvent;
import net.auroramc.core.api.events.player.PlayerFakePlayerInteractEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class TabCompleteInjector {

    public static void onJoin(Player pl) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "PACKET READ: " + ChatColor.RED + packet.toString());

                if (packet instanceof PacketPlayInTabComplete) {
                    PacketPlayInTabComplete tabComplete = (PacketPlayInTabComplete) packet;
                    String message = tabComplete.a();
                    if (message.startsWith("/")) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                //This is a command, tab complete it.
                                AuroraMCServerPlayer player = ServerAPI.getPlayer(pl);
                                if (player == null) {
                                    //Player has not yet loaded fully.
                                    return;
                                }
                                if (!player.isLoaded()) {
                                    //Player has not yet loaded fully.
                                    return;
                                }
                                if (message.contains(" ")) {
                                    String commandLabel = message.split(" ")[0].replace("/","");
                                    ServerCommand command = (ServerCommand) AuroraMCAPI.getCommand(commandLabel);
                                    if (command != null) {
                                        //This is a command that is recognised and they are tab completing a subcommand, check if they have permissions to use the command.
                                        for (Permission permission : command.getPermission()) {
                                            if (player.hasPermission(permission.getId())) {
                                                ArrayList<String> args = new ArrayList<>(Arrays.asList(message.split(" ")));
                                                args.remove(0);
                                                List<String> finalCompletions = command.onTabComplete(player, commandLabel, args, ((message.endsWith(" ")) ? "" : args.get(args.size() - 1)), ((message.endsWith(" ")) ? args.size() + 1 : args.size()));
                                                Collections.sort(finalCompletions);
                                                String[] complete = new String[finalCompletions.size()];
                                                finalCompletions.toArray(complete);
                                                PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(complete);
                                                player.getCraft().getHandle().playerConnection.sendPacket(packetPlayOutTabComplete);
                                                break;
                                            }
                                        }
                                    } else {
                                        String[] complete = new String[0];
                                        PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(complete);
                                        player.getCraft().getHandle().playerConnection.sendPacket(packetPlayOutTabComplete);
                                    }
                                } else {
                                    List<String> completions = AuroraMCAPI.getCommands().stream().filter((command) -> command.startsWith(message.split(" ")[0].replace("/","").toLowerCase())).collect(Collectors.toList());
                                    List<String> finalCompletions = new ArrayList<>();
                                    completionLoop:
                                    for (String commandLabel : completions) {
                                        ServerCommand command = (ServerCommand) AuroraMCAPI.getCommand(commandLabel);
                                        for (Permission permission : command.getPermission()) {
                                            if (player.hasPermission(permission.getId())) {
                                                finalCompletions.add("/" + commandLabel);
                                                continue completionLoop;
                                            }
                                        }
                                    }
                                    Collections.sort(finalCompletions);
                                    String[] complete = new String[finalCompletions.size()];
                                    finalCompletions.toArray(complete);
                                    PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(complete);
                                    player.getCraft().getHandle().playerConnection.sendPacket(packetPlayOutTabComplete);
                                }
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                    return;
                } else if (packet instanceof PacketPlayInUseEntity) {
                    PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet;

                    Field field = useEntity.getClass().getDeclaredField("a");
                    field.setAccessible(true);

                    int entityId = field.getInt(useEntity);
                    if (ServerAPI.getFakePlayers().containsKey(entityId)) {
                        AuroraMCServerPlayer player = ServerAPI.getPlayer(pl);
                        if (player == null) {
                            //Player has not yet loaded fully.
                            return;
                        }
                        if (!player.isLoaded()) {
                            //Player has not yet loaded fully.
                            return;
                        }
                        if (Objects.requireNonNull(useEntity.a()) == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                            PlayerFakePlayerAttackEvent event = new PlayerFakePlayerAttackEvent(player, ServerAPI.getFakePlayers().get(entityId));
                            Bukkit.getPluginManager().callEvent(event);
                        }
                        PlayerFakePlayerInteractEvent event = new PlayerFakePlayerInteractEvent(player, ServerAPI.getFakePlayers().get(entityId));
                        Bukkit.getPluginManager().callEvent(event);
                    } else if (ServerAPI.getHolograms().containsKey(entityId)) {
                        Hologram hologram = ServerAPI.getHolograms().get(entityId);
                        if (!hologram.isPersonal()) {
                            //Not personal, click handlers aren't applicable to universal holograms.
                            return;
                        }
                        AuroraMCServerPlayer player = ServerAPI.getPlayer(pl);
                        if (player == null) {
                            //Player has not yet loaded fully.
                            return;
                        }
                        if (!player.isLoaded()) {
                            //Player has not yet loaded fully.
                            return;
                        }
                        ServerAPI.getHolograms().get(entityId).onClick();
                    }
                }

                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if (packet instanceof PacketPlayOutPlayerInfo) {
                    PacketPlayOutPlayerInfo info = (PacketPlayOutPlayerInfo) packet;

                    Field field = PacketPlayOutPlayerInfo.class.
                            getDeclaredField("a");

                    field.setAccessible(true);

                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction action = (PacketPlayOutPlayerInfo.EnumPlayerInfoAction) field.get(info);

                    field = PacketPlayOutPlayerInfo.class.
                            getDeclaredField("b");

                    field.setAccessible(true);

                    List<PacketPlayOutPlayerInfo.PlayerInfoData> data = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) field.get(info);


                    AuroraMCServerPlayer player = ServerAPI.getPlayer(pl);
                    if (player == null) {

                        //Player has not yet loaded fully. Check if the person is the same person.
                        if (action != PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER) {
                            //Player has not yet loaded fully.
                            return;
                        }
                        if (!data.get(0).a().getId().equals(pl.getUniqueId())) {
                            return;
                        }

                        super.write(channelHandlerContext, packet, channelPromise);
                        return;
                    }
                    if (player.getRank() == null) {
                        //Player has not loaded enough yet.

                        if (action != PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER) {
                            //Player has not yet loaded fully.
                            return;
                        }
                        if (!data.get(0).a().getId().equals(pl.getUniqueId())) {
                            return;
                        }
                        super.write(channelHandlerContext, packet, channelPromise);
                        return;
                    }


                    if (!player.isLoaded() && action == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER) {
                        //Player has not yet loaded fully.
                        return;
                    }


                    List<PacketPlayOutPlayerInfo.PlayerInfoData> clone = new ArrayList<>(data);


                    for (PacketPlayOutPlayerInfo.PlayerInfoData d : clone) {
                        AuroraMCServerPlayer player1 = ServerAPI.getPlayer(d.a().getId());
                        if (player1 != null && player1.isLoaded()) {
                            if (player1.equals(pl)) {
                                continue;
                            }
                            if (action == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER || action == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER) {
                                if (!player1.isVanished() || player1.getRank().getId() >= player.getRank().getId()) {
                                    continue;
                                }
                            } else {
                                if (!player1.isVanished() || player1.getRank().getId() <= player.getRank().getId()) {
                                    continue;
                                }
                            }
                        } else if (action == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER || action == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER) {
                            continue;
                        }

                        data.remove(d);
                    }

                    if (data.size() == 0) {
                        return;
                    }
                }
                super.write(channelHandlerContext, packet, channelPromise);
            }


        };

        ChannelPipeline pipeline = ((CraftPlayer) pl).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", pl.getName(), channelDuplexHandler);
    }

    public static void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }



}
