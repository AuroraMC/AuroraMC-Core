/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.utils;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.netty.channel.*;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.events.player.AsyncPlayerChatEvent;
import net.auroramc.smp.api.events.player.PlayerFakePlayerInteractEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.holograms.Hologram;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
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
                    String message = tabComplete.c();
                    int i = tabComplete.a();
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
                                                SuggestionsBuilder sb = new SuggestionsBuilder(message, message.length());
                                                finalCompletions.forEach(sb::suggest);
                                                PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(i, sb.build());
                                                player.getCraft().getHandle().b.a(packetPlayOutTabComplete);
                                                break;
                                            }
                                        }
                                    } else {
                                        PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(i, new SuggestionsBuilder(message, 0).build());
                                        player.getCraft().getHandle().b.a(packetPlayOutTabComplete);
                                    }
                                } else {
                                    List<String> completions = AuroraMCAPI.getCommands().stream().filter((command) -> command.startsWith(message.split(" ")[0].replace("/","").toLowerCase())).collect(Collectors.toList());
                                    SuggestionsBuilder builder = new SuggestionsBuilder(message, message.length());
                                    completionLoop:
                                    for (String commandLabel : completions) {
                                        ServerCommand command = (ServerCommand) AuroraMCAPI.getCommand(commandLabel);
                                        for (Permission permission : command.getPermission()) {
                                            if (player.hasPermission(permission.getId())) {
                                                builder.suggest("/" + commandLabel);
                                                continue completionLoop;
                                            }
                                        }
                                    }
                                    PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(i, builder.build());
                                    player.getCraft().getHandle().b.a(packetPlayOutTabComplete);
                                }
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                        return;
                    }
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
                } else if (packet instanceof PacketPlayInChat) {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(pl);
                    if (player == null) {
                        //Player has not yet loaded fully.
                        return;
                    }
                    if (!player.isLoaded()) {
                        //Player has not yet loaded fully.
                        return;
                    }
                    String string = ((PacketPlayInChat) packet).a();
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            AsyncPlayerChatEvent chatEvent = new AsyncPlayerChatEvent(true, player, string);
                            Bukkit.getPluginManager().callEvent(chatEvent);
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                    return;
                }

                super.channelRead(channelHandlerContext, packet);
            }

            /*@Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if (packet instanceof ClientboundPlayerInfoUpdatePacket) {
                    ClientboundPlayerInfoUpdatePacket info = (ClientboundPlayerInfoUpdatePacket) packet;

                    Field field = ClientboundPlayerInfoUpdatePacket.class.
                            getDeclaredField("a");

                    field.setAccessible(true);

                    ClientboundPlayerInfoUpdatePacket.a action = (ClientboundPlayerInfoUpdatePacket.a) field.get(info);

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
            }*/


        };
        try {
            Field field = PlayerConnection.class.
                    getDeclaredField("h");

            field.setAccessible(true);

            NetworkManager manager = (NetworkManager) field.get(((CraftPlayer) pl).getHandle().b);
            manager.m.pipeline().addBefore("packet_handler", pl.getName(), channelDuplexHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void removePlayer(Player player) {
        try {
            Field field = PlayerConnection.class.
                    getDeclaredField("h");

            field.setAccessible(true);

            NetworkManager manager = (NetworkManager) field.get(((CraftPlayer) player).getHandle().b);
            Channel channel = manager.m;
            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(player.getName());
                return null;
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}
