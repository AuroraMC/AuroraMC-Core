package net.auroramc.core.api.utils;

import io.netty.channel.*;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.myles.ViaVersion.api.Via;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleteInjector {

    public static void onJoin(AuroraMCPlayer player) {
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
                                if (message.contains(" ")) {
                                    String commandLabel = message.split(" ")[0].replace("/","");
                                    Command command = AuroraMCAPI.getCommand(commandLabel);
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
                                                ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packetPlayOutTabComplete);
                                                break;
                                            }
                                        }
                                    } else {
                                        String[] complete = new String[0];
                                        PacketPlayOutTabComplete packetPlayOutTabComplete = new PacketPlayOutTabComplete(complete);
                                        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packetPlayOutTabComplete);
                                    }
                                } else {
                                    List<String> completions = AuroraMCAPI.getCommands().stream().filter((command) -> command.startsWith(message.split(" ")[0].replace("/","").toLowerCase())).collect(Collectors.toList());
                                    List<String> finalCompletions = new ArrayList<>();
                                    completionLoop:
                                    for (String commandLabel : completions) {
                                        Command command = AuroraMCAPI.getCommand(commandLabel);
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
                                    ((CraftPlayer)player.getPlayer()).getHandle().playerConnection.sendPacket(packetPlayOutTabComplete);
                                }
                            }
                        }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    }
                    return;
                }

                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "PACKET WRITE: " + ChatColor.GREEN + packet.toString());
                super.write(channelHandlerContext, packet, channelPromise);
            }


        };

        ChannelPipeline pipeline = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getPlayer().getName(), channelDuplexHandler);
    }

    public static void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }



}
