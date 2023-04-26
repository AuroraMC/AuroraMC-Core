/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.executors.wineffects;

import com.mojang.authlib.GameProfile;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TwerkApocalypseExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public TwerkApocalypseExecutor() {
        super(AuroraMCAPI.getCosmetics().get(601));
    }

    @Override
    public void execute(AuroraMCPlayer pl1) {
        AuroraMCServerPlayer player = (AuroraMCServerPlayer) pl1;
        List<Team> teams = new ArrayList<>();
        for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
            Team team = player1.getScoreboard().getScoreboard().registerNewTeam("twerk");
            team.setNameTagVisibility(NameTagVisibility.NEVER);
            teams.add(team);
        }

        List<EntityPlayer> players = new ArrayList<>();
        final Random random = new Random();
        outer:
        for (int i = 0; i < 20; i++) {
            GameProfile profile;
            UUID uuid = UUID.randomUUID();
            profile = new GameProfile(uuid, uuid.toString().substring(0, 16));
            for (Team team : teams) {
                team.addEntry(uuid.toString().substring(0, 16));
            }

            profile.getProperties().put("textures", new ArrayList<>(player.getCraft().getHandle().getProfile().getProperties().get("textures")).get(0));
            EntityPlayer pl = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) Bukkit.getWorld("world")).getHandle(), profile, new PlayerInteractManager(((CraftWorld) Bukkit.getWorld("world")).getHandle()));
            double x = (player.getLocation().getX() + (random.nextInt(40) - 20));
            double y = 256;
            double z = (player.getLocation().getZ() + (random.nextInt(40) - 20));

            for (int i2 = 5;i2 >= -5;i2--) {
                Location location = new Location(player.getWorld(), x, player.getLocation().getY() + i2, z);
                if (location.getBlock().getType() != Material.AIR) {
                    y = player.getLocation().getY() + i2;
                    break;
                }
            }

            Location location = new Location(player.getWorld(), x, y, z);
            if (y == 256) {
                while (location.getBlock().getType() == Material.AIR) {
                    location.setY(location.getY() - 1);
                    if (location.getY() < 0) {
                        continue outer;
                    }
                }
            }
            pl.setLocation(x, location.getY() + 1, z, (random.nextInt(360) - 180), 0f);
            players.add(pl);
        }


        for (EntityPlayer pl : players) {
            PacketPlayOutPlayerInfo p1 = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, pl);
            PacketPlayOutNamedEntitySpawn p2 = new PacketPlayOutNamedEntitySpawn(pl);
            PacketPlayOutEntityHeadRotation p3 = new PacketPlayOutEntityHeadRotation(pl, (byte) ((pl.yaw * 256.0F) / 360.0F));
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (!player1.getWorld().equals(player.getWorld())) {
                    continue;
                }
                PlayerConnection con = ((CraftPlayer) player1.getPlayer()).getHandle().playerConnection;
                con.sendPacket(p1);
                con.sendPacket(p2);
                con.sendPacket(p3);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, pl));
                    }
                }.runTaskLater(ServerAPI.getCore(), 80);
            }
        }
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i <= 18) {
                    List<PacketPlayOutEntityMetadata> packets = new ArrayList<>();
                    if (i % 2 == 0) {
                        for (int x = 0; x < players.size(); x++) {
                            EntityPlayer pl = players.get(x);
                            DataWatcher dw = pl.getDataWatcher();
                            if (x < players.size() / 2) {
                                dw.watch(0, (byte) 0);
                            } else {
                                dw.watch(0, (byte) 2);
                            }
                            packets.add(new PacketPlayOutEntityMetadata(pl.getId(), dw, false));
                        }
                    } else {
                        for (int x = 0; x < players.size(); x++) {
                            EntityPlayer pl = players.get(x);
                            DataWatcher dw = pl.getDataWatcher();
                            if (x < players.size() / 2) {
                                dw.watch(0, (byte) 2);
                            } else {
                                dw.watch(0, (byte) 0);
                            }
                            packets.add(new PacketPlayOutEntityMetadata(pl.getId(), dw, false));
                        }
                    }
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (!pl.getWorld().equals(player.getWorld())) {
                            continue;
                        }
                        for (PacketPlayOutEntityMetadata packet : packets) {
                            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                    i++;
                } else {
                    for (Team team : teams) {
                        team.unregister();
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(ServerAPI.getCore(), 0, 10);
    }

    @Override
    public void execute(AuroraMCPlayer player, double x, double y, double z) {
    }

    @Override
    public void execute(Object entity) {
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
