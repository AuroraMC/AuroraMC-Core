/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.wineffects;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.WinEffect;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class TwerkApocalypse extends WinEffect {

    public TwerkApocalypse() {
        super(601, "Twerk Apocalypse", "&6&lTwerk Apocalypse", "Twerk on your enemies graves.", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "Reach Kit Level 100 on any kit to unlock this win effect!", true, Material.LEATHER_BOOTS, (short)0, Rarity.LEGENDARY);
    }

    @Override
    public void onWin(AuroraMCPlayer player) {
        List<Team> teams = new ArrayList<>();
        for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
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

            profile.getProperties().put("textures", new ArrayList<>(((CraftPlayer) player.getPlayer()).getHandle().getProfile().getProperties().get("textures")).get(0));
            EntityPlayer pl = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) Bukkit.getWorld("world")).getHandle(), profile, new PlayerInteractManager(((CraftWorld) Bukkit.getWorld("world")).getHandle()));
            double x = (player.getPlayer().getLocation().getX() + (random.nextInt(40) - 20));
            double z = (player.getPlayer().getLocation().getZ() + (random.nextInt(40) - 20));
            Location location = new Location(player.getPlayer().getWorld(), x, 256, z);
            while (location.getBlock().getType() == Material.AIR) {
                location.setY(location.getY() - 1);
                if (location.getY() < 0) {
                    continue outer;
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
                PlayerConnection con = ((CraftPlayer) player1.getPlayer()).getHandle().playerConnection;
                con.sendPacket(p1);
                con.sendPacket(p2);
                con.sendPacket(p3);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, pl));
                    }
                }.runTaskLater(AuroraMCAPI.getCore(), 80);
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
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 10);
    }

}