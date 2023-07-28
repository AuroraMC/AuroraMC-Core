/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Disguise;
import net.auroramc.api.utils.UUIDUtil;
import net.auroramc.api.utils.disguise.CachedSkin;
import net.auroramc.api.utils.disguise.Skin;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

public class DisguiseUtil {

    private static final String defaultSkinValue = "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZiZTM3NzljOWY5ZGViODkzMmMzYjdiZDI1Nzg4OGQxNTg5NjlhYzkxYWEyNjM1MTU3MmYxYWQ4N2M1MTk0OCJ9fX0===";

    public static boolean changeSkin(AuroraMCServerPlayer player, String skin, String signature, boolean update, boolean undisguise) {
        GameProfile playerProfile = player.getCraft().getHandle().fM();
        playerProfile.getProperties().removeAll("textures");
        playerProfile.getProperties().put("textures", new Property("textures", skin, signature));
        if (player.getActiveDisguise() != null) {
            player.getActiveDisguise().updateSkin(new Skin(skin, signature));
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
            GameProfile playerProfile = craftPlayer.getHandle().fM();
            Field ff = playerProfile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(playerProfile, username);


        } catch (Exception e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
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
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            return null;
        }
    }

}
