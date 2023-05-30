/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.utils.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Disguise;
import net.auroramc.api.utils.UUIDUtil;
import net.auroramc.api.utils.disguise.CachedSkin;
import net.auroramc.api.utils.disguise.Skin;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class DisguiseUtil {

    private static final String defaultSkinValue = "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZiZTM3NzljOWY5ZGViODkzMmMzYjdiZDI1Nzg4OGQxNTg5NjlhYzkxYWEyNjM1MTU3MmYxYWQ4N2M1MTk0OCJ9fX0===";

    public static boolean changeSkin(AuroraMCServerPlayer player, String skin, String signature, boolean update, boolean undisguise) {
        GameProfile playerProfile = player.getCraft().getHandle().getProfile();
        playerProfile.getProperties().removeAll("textures");
        playerProfile.getProperties().put("textures", new Property("textures", skin, signature));
        if (player.getActiveDisguise() != null) {
            player.getActiveDisguise().updateSkin(new Skin(skin, signature));
        }
        if (update) {
            updatePlayer(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().setDisguise(player, player.getActiveDisguise());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
        return true;
    }

    public static boolean changeSkin(AuroraMCServerPlayer player, UUID uuid, boolean update) {
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
                changeSkin(player, finalSkin.getValue(), finalSkin.getSignature(), update, false);
            }
        }.runTask(ServerAPI.getCore());
        return true;

    }

    public static void changeSkin(AuroraMCServerPlayer player, String username, boolean update, Disguise disguise) {
        new BukkitRunnable(){
            @Override
            public void run() {
                UUID uuid = UUIDUtil.getUUID(username);
                if (uuid != null) {
                    if (!uuid.toString().equals("")) {
                        changeSkin(player, uuid, update);
                    }
                } else {
                    if (update) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                updatePlayer(player);
                                changeSkin(player, defaultSkinValue, null, true, false);
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    public static boolean changeName(AuroraMCServerPlayer player, String username, boolean update) {
        try {
            CraftPlayer craftPlayer = player.getCraft();
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

    public static boolean disguise(AuroraMCServerPlayer player, String username, String skin, Disguise disguise, boolean update) {
        changeSkin(player, skin, update, disguise);
        return changeName(player, username, false);
    }

    public static boolean disguise(AuroraMCServerPlayer player, String username, String skin, String signature, boolean update, boolean undisguise) {
        boolean success = changeSkin(player, skin, signature, update, undisguise);
        return success && changeName(player, username, false);
    }

    public static boolean disguise(AuroraMCServerPlayer player, String username, UUID skin, boolean update) {
        boolean success = changeSkin(player, skin, update);
        return success && changeName(player, username, false);
    }

    @SuppressWarnings("deprecated")
    public static void updatePlayer(@NotNull AuroraMCServerPlayer player) {
        CraftPlayer craftPlayer = player.getCraft();

        //Setup params for when they "respawn".
        PacketPlayOutRespawn respawnPlayer;
        respawnPlayer = new PacketPlayOutRespawn(craftPlayer.getHandle().dimension, craftPlayer.getHandle().world.getDifficulty(), craftPlayer.getHandle().world.getWorldData().getType(), craftPlayer.getHandle().playerInteractManager.getGameMode());


        final boolean flying = player.isFlying();
        final Location location = player.getLocation();
        final int level = player.getLevel();
        final float xp = player.getExp();
        final double maxHealth = player.getMaxHealth();
        final double health = player.getHealth();
        final Collection<PotionEffect> potioneffects = player.getActivePotionEffects();


        Bukkit.getScheduler().runTaskLater(ServerAPI.getCore(), () -> {
            try {
                if (!player.getPreferences().isHideDisguiseNameEnabled()) {
                    craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(craftPlayer.getHandle().getId()));
                    PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, craftPlayer.getHandle());
                    craftPlayer.getHandle().playerConnection.sendPacket(remove);
                    EntityTrackerEntry entry = ((WorldServer)craftPlayer.getHandle().world).tracker.trackedEntities.get(craftPlayer.getHandle().getId());
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
                    player.addPotionEffects(potioneffects);
                }
                for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                    if (player2.canSee(player)) {
                        player2.hidePlayer(player);
                        player2.showPlayer(player);
                        player2.updateNametag(player);
                    }
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
