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
        Team team = player.getScoreboard().getScoreboard().registerNewTeam("twerk");
        team.setNameTagVisibility(NameTagVisibility.NEVER);

        List<EntityPlayer> players = new ArrayList<>();
        final Random random = new Random();
        outer:
        for (int i = 0; i < 20; i++) {
            GameProfile profile;
            UUID uuid = UUID.randomUUID();
            profile = new GameProfile(uuid, uuid.toString());
            team.addEntry(uuid.toString());

            profile.getProperties().put("textures", new ArrayList<>(((CraftPlayer) player.getPlayer()).getHandle().getProfile().getProperties().get("textures")).get(0));
            EntityPlayer pl = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) Bukkit.getWorld("world")).getHandle(), profile, new PlayerInteractManager(((CraftWorld) Bukkit.getWorld("world")).getHandle()));
            double x = (player.getPlayer().getLocation().getX() + (random.nextInt(20) - 10));
            double z = (player.getPlayer().getLocation().getZ() + (random.nextInt(20) - 10));
            Location location = new Location(player.getPlayer().getWorld(), x, 256, z);
            while (location.getBlock().getType() == Material.AIR) {
                location.setY(location.getY() - 1);
                if (location.getY() < 0) {
                    continue outer;
                }
            }
            pl.setLocation(x, location.getY(), z, (random.nextInt(360) - 180), 0f);
            if (i < 10) {
                DataWatcher dw = pl.getDataWatcher();
                dw.watch(0, (byte) 2);
            }
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
                    for (int i = 0; i < 20; i++) {
                        EntityPlayer pl = players.get(i);
                        DataWatcher dw = pl.getDataWatcher();
                        dw.watch(0, (byte) 2);
                        packets.add(new PacketPlayOutEntityMetadata(pl.getId(), dw, false));
                    }
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        for (PacketPlayOutEntityMetadata packet : packets) {
                            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                    i++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(AuroraMCAPI.getCore(), 0, 10);
    }

}
