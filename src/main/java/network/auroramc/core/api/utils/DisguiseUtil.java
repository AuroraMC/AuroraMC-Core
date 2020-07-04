package network.auroramc.core.api.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.server.v1_8_R3.*;
import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.Disguise;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisguiseUtil {

    public static boolean changeSkin(Player player, String skin, String signature, boolean update) {
        GameProfile playerProfile = ((CraftPlayer) player).getHandle().getProfile();
        playerProfile.getProperties().removeAll("textures");
        playerProfile.getProperties().put("textures", new Property("textures", skin, signature));
        new BukkitRunnable() {
            @Override
            public void run() {
                AuroraMCPlayer pl = AuroraMCAPI.getPlayer(player);
                AuroraMCAPI.getDbManager().setDisguise(pl, pl.getActiveDisguise());
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        if (update) {
            updatePlayer(player);
        }
        return true;
    }

    public static boolean changeSkin(Player player, UUID uuid, boolean update, Disguise disguise) {
        Skin skin = AuroraMCAPI.getDbManager().getCachedSkin(uuid.toString());

        if (skin != null) {
            CachedSkin cachedSkin = (CachedSkin) skin;
            if (System.currentTimeMillis() - cachedSkin.getLastUpdated() > 60000) {
                skin = getSkin(uuid);
            }
        } else {
            skin = getSkin(uuid);
        }

        if (skin == null) {
            return false;
        }

        if (!(skin instanceof CachedSkin)) {
            AuroraMCAPI.getDbManager().cacheSkin(uuid.toString(), skin.getValue(), skin.getSignature(), System.currentTimeMillis());
        }

        disguise.updateSkin(skin);

        Skin finalSkin = skin;
        new BukkitRunnable(){
            @Override
            public void run() {
                changeSkin(player, finalSkin.getValue(), finalSkin.getSignature(), update);
            }
        }.runTask(AuroraMCAPI.getCore());
        return true;

    }

    public static void changeSkin(Player player, String username, boolean update, Disguise disguise) {
        new BukkitRunnable(){
            @Override
            public void run() {
                UUID uuid = UUIDUtil.getUUID(username);
                if (uuid != null) {
                    if (!uuid.toString().equals("")) {
                        changeSkin(player, uuid, update, disguise);
                    }
                } else {
                    if (update) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                updatePlayer(player);
                                disguise.updateSkin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTU5Mzg3NzM0MDc1NywKICAicHJvZmlsZUlkIiA6ICJiMGMzYzA1OThjMjQ0NjY0OWFkYzAzNjE0ZTdhN2UwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICI3dWtlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2YmUzNzc5YzlmOWRlYjg5MzJjM2I3YmQyNTc4ODhkMTU4OTY5YWM5MWFhMjYzNTE1NzJmMWFkODdjNTE5NDgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "UGHYsvsAZeykNWqutvZD6q4K1KvzolcpLT4yJEPBeCo1vso3gZy0YoExx1RmFyaQu8rzP107sbHg7HaC04d4bg0/m6EjgKLbMV8B3gXbObWEoJ7PzyU/31ezJ11W4wqlSuF3D23DBSA8d2WB3cADiRXkhvMVEHEtXBX3oQZ5U1ZQlGpiVpi3a/hsfbMn2KjYsI25P/koNoBpdKuCy8oYFTd46kOdUW7/OXiBRKjKhUV/TAFxQ7rATMwrp7m4qZpQkfogK5e6lsYj9BueOrKy/Fn5JgejBP7Cx7Dl+u51kGKE/ngbudmzt92eu7H/VzTOY+0zS9d753aFzPyX9WWpfyXCb+o+wzUaccIzome1IX8UKxoVvz+Oh0FFg+E7FihNmdt5liJxPXvS6Bgs+GnaENJBhSb3+lpsSEVAXlqzJ9TZZGCN/1AdchUVhPxrWNUqfqyg9AZAhf0KtfUqVNCcd/QjvTsp5FvkGCkPiFf6Ev4wRTb5n2/vLgPXGDHh/pGFnilfroODI0sjuUUkJLP+ZRcKuiiX5+ain4mMMtNzbgpnLD3HRV7IRfb7Thbxtof2riUODLd5tFikz4isUngxjTERV1pA0xcKbyL6b1iYQ5ZDmzKv/wjCGgQd1jrkVeK0Hkf2WEaF2PkqxCf+lWlLY+kIBnvtRyP1cJDJ+RDR/I0="));
                                changeSkin(player, disguise.getSkin(), disguise.getSignature(), true);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public static boolean changeName(Player player, String username, boolean update) {
        try {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            GameProfile playerProfile = craftPlayer.getHandle().getProfile();
            Field ff = playerProfile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(playerProfile, username);

            if (update) {
                updatePlayer(player);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean disguise(Player player, String username, String skin, Disguise disguise, boolean update) {
        changeSkin(player, skin, update, disguise);
        return changeName(player, username, false);
    }

    public static boolean disguise(Player player, String username, String skin, String signature, boolean update) {
        boolean success = changeSkin(player, skin, signature, update);
        return success && changeName(player, username, false);
    }

    public static boolean disguise(Player player, String username, UUID skin, Disguise disguise, boolean update) {
        boolean success = changeSkin(player, skin, update, disguise);
        return success && changeName(player, username, false);
    }

    @SuppressWarnings("deprecated")
    public static void updatePlayer(@NotNull Player player) {
        CraftPlayer craftPlayer = ((CraftPlayer) player);

        //Setup params for when they "respawn".
        PacketPlayOutRespawn respawnPlayer;
        respawnPlayer = new PacketPlayOutRespawn(craftPlayer.getHandle().dimension, craftPlayer.getHandle().world.getDifficulty(), craftPlayer.getHandle().world.getWorldData().getType(), craftPlayer.getHandle().playerInteractManager.getGameMode());

        final boolean flying = player.isFlying();
        final Location location = player.getLocation();
        final int level = player.getLevel();
        final float xp = player.getExp();
        final double maxHealth = player.getMaxHealth();
        final double health = player.getHealth();


        Bukkit.getScheduler().runTaskLater(AuroraMCAPI.getCore(), () -> {
            try {
                craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(craftPlayer.getHandle().getId()));
                PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, craftPlayer.getHandle());
                craftPlayer.getHandle().playerConnection.sendPacket(remove);
                EntityTrackerEntry entry = ((WorldServer)((CraftPlayer) player).getHandle().world).tracker.trackedEntities.get(craftPlayer.getHandle().getId());
                if (entry != null) {
                    entry.clear(craftPlayer.getHandle());
                }
                craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, craftPlayer.getHandle()));
                entry = ((WorldServer)(craftPlayer.getHandle().world)).tracker.trackedEntities.get(craftPlayer.getHandle().getId());
                if (entry != null) {
                    entry.updatePlayer(craftPlayer.getHandle());
                }
                craftPlayer.getHandle().playerConnection.sendPacket(respawnPlayer);
                craftPlayer.getHandle().updateAbilities();
                MinecraftServer.getServer().getPlayerList().updateClient(craftPlayer.getHandle());

                player.setFlying(flying);
                player.teleport(location);
                player.updateInventory();
                player.setLevel(level);
                player.setExp(xp);
                player.setMaxHealth(maxHealth);
                player.setHealth(health);

                int i = Bukkit.getServer().getViewDistance();
                for (int x = -i;x<=i;x++) {
                    for (int z = -i;z<=i;z++) {
                        int finalX = x;
                        int finalZ = z;
                        //Schedule all of the chunks to be reloaded.
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Chunk chunk = ((CraftChunk) player.getLocation().getWorld().getChunkAt((player.getLocation().getBlockX() + (finalX *16)), (player.getLocation().getBlockZ() + (finalZ *16)))).getHandle();
                                craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, true, 20));
                                craftPlayer.getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(chunk.bukkitChunk.getX(), chunk.bukkitChunk.getZ()));
                                chunk.bukkitChunk.getWorld().refreshChunk(chunk.bukkitChunk.getX(), chunk.bukkitChunk.getZ());
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.hidePlayer(player);
                    player2.showPlayer(player);
                    AuroraMCAPI.getPlayer(player2).updateNametag(AuroraMCAPI.getPlayer(player));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 1);
    }

    public static Skin getSkin(UUID uuid) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String reply = reader.readLine().trim().replace(" ","");
                while (!reply.contains("value")) {
                    reply = reader.readLine().trim().replace(" ","");
                }
                String skin = reply.split("\"value\":\"")[1].split("\"")[0];
                while (!reply.contains("signature")) {
                    reply = reader.readLine().trim().replace(" ","");
                }
                String signature = reply.split("\"signature\":\"")[1].split("\"")[0];

                return new Skin(skin, signature);
            } else {
                Bukkit.getLogger().warning("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
