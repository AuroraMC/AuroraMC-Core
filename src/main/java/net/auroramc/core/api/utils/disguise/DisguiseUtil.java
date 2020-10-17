package net.auroramc.core.api.utils.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.server.v1_8_R3.*;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Disguise;
import net.auroramc.core.api.utils.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import java.util.HashSet;
import java.util.UUID;

public class DisguiseUtil {

    private static final String defaultSkinValue = "ewogICJ0aW1lc3RhbXAiIDogMTU5Mzg3NzM0MDc1NywKICAicHJvZmlsZUlkIiA6ICJiMGMzYzA1OThjMjQ0NjY0OWFkYzAzNjE0ZTdhN2UwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICI3dWtlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2YmUzNzc5YzlmOWRlYjg5MzJjM2I3YmQyNTc4ODhkMTU4OTY5YWM5MWFhMjYzNTE1NzJmMWFkODdjNTE5NDgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
    private static final String defaultSkinSignature = "UGHYsvsAZeykNWqutvZD6q4K1KvzolcpLT4yJEPBeCo1vso3gZy0YoExx1RmFyaQu8rzP107sbHg7HaC04d4bg0/m6EjgKLbMV8B3gXbObWEoJ7PzyU/31ezJ11W4wqlSuF3D23DBSA8d2WB3cADiRXkhvMVEHEtXBX3oQZ5U1ZQlGpiVpi3a/hsfbMn2KjYsI25P/koNoBpdKuCy8oYFTd46kOdUW7/OXiBRKjKhUV/TAFxQ7rATMwrp7m4qZpQkfogK5e6lsYj9BueOrKy/Fn5JgejBP7Cx7Dl+u51kGKE/ngbudmzt92eu7H/VzTOY+0zS9d753aFzPyX9WWpfyXCb+o+wzUaccIzome1IX8UKxoVvz+Oh0FFg+E7FihNmdt5liJxPXvS6Bgs+GnaENJBhSb3+lpsSEVAXlqzJ9TZZGCN/1AdchUVhPxrWNUqfqyg9AZAhf0KtfUqVNCcd/QjvTsp5FvkGCkPiFf6Ev4wRTb5n2/vLgPXGDHh/pGFnilfroODI0sjuUUkJLP+ZRcKuiiX5+ain4mMMtNzbgpnLD3HRV7IRfb7Thbxtof2riUODLd5tFikz4isUngxjTERV1pA0xcKbyL6b1iYQ5ZDmzKv/wjCGgQd1jrkVeK0Hkf2WEaF2PkqxCf+lWlLY+kIBnvtRyP1cJDJ+RDR/I0=";

    public static boolean changeSkin(Player player, String skin, String signature, boolean update, AuroraMCPlayer amcPlayer, boolean undisguise) {
        GameProfile playerProfile = ((CraftPlayer) player).getHandle().getProfile();
        playerProfile.getProperties().removeAll("textures");
        playerProfile.getProperties().put("textures", new Property("textures", skin, signature));
        if (amcPlayer.getActiveDisguise() != null) {
            amcPlayer.getActiveDisguise().updateSkin(new Skin(skin, signature));
        }
        if (update) {
            updatePlayer(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().setDisguise(amcPlayer, amcPlayer.getActiveDisguise());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
        return true;
    }

    public static boolean changeSkin(Player player, UUID uuid, boolean update, AuroraMCPlayer amcPlayer) {
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

        Skin finalSkin = skin;
        new BukkitRunnable(){
            @Override
            public void run() {
                changeSkin(player, finalSkin.getValue(), finalSkin.getSignature(), update, amcPlayer, false);
            }
        }.runTask(AuroraMCAPI.getCore());
        return true;

    }

    public static void changeSkin(Player player, String username, boolean update, Disguise disguise, AuroraMCPlayer amcPlayer) {
        new BukkitRunnable(){
            @Override
            public void run() {
                UUID uuid = UUIDUtil.getUUID(username);
                if (uuid != null) {
                    if (!uuid.toString().equals("")) {
                        changeSkin(player, uuid, update, amcPlayer);
                    }
                } else {
                    if (update) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                updatePlayer(player);
                                changeSkin(player, defaultSkinValue, defaultSkinSignature, true, amcPlayer, false);
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

    public static boolean disguise(Player player, String username, String skin, Disguise disguise, boolean update, AuroraMCPlayer amcPlayer) {
        changeSkin(player, skin, update, disguise, amcPlayer);
        return changeName(player, username, false);
    }

    public static boolean disguise(Player player, String username, String skin, String signature, boolean update, AuroraMCPlayer amcPlayer, boolean undisguise) {
        boolean success = changeSkin(player, skin, signature, update, amcPlayer, undisguise);
        return success && changeName(player, username, false);
    }

    public static boolean disguise(Player player, String username, UUID skin, AuroraMCPlayer amcPlayer, boolean update) {
        boolean success = changeSkin(player, skin, update, amcPlayer);
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
                //Update look and location so that the client doesn't unload the chunks.
                craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), new HashSet<>()));
                craftPlayer.getHandle().updateAbilities();
                MinecraftServer.getServer().getPlayerList().updateClient(craftPlayer.getHandle());

                player.setFlying(flying);
                player.teleport(location);
                player.updateInventory();
                player.setLevel(level);
                player.setExp(xp);
                player.setMaxHealth(maxHealth);
                player.setHealth(health);
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
