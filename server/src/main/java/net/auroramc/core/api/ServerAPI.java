/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.ChatSlowLength;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.AuroraMC;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.events.player.PlayerLeaveEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.auroramc.core.api.utils.holograms.HologramLine;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerAPI {

    private static AuroraMC core;
    private static Map<Player, AuroraMCServerPlayer> players;
    private static BukkitTask silenceTask;

    private static Map<Integer, EntityPlayer> fakePlayers;
    private static Map<Integer, Hologram> holograms;
    private static final HashMap<AuroraMCServerPlayer, GUI> openGUIs;

    private static boolean shuttingDown;
    private static boolean eventMode;

    static {
        openGUIs = new HashMap<>();
        holograms = new HashMap<>();
        fakePlayers = new HashMap<>();
        shuttingDown = false;
        eventMode = false;
    }

    public static void init(AuroraMC core) {
        AuroraMCAPI.init(core.getLogger(), new ServerAbstractedMethods(), core.getConfig().getString("mysqlhost"), core.getConfig().getString("mysqlport"), core.getConfig().getString("mysqldb"), core.getConfig().getString("mysqlusername"), core.getConfig().getString("mysqlpassword"), core.getConfig().getString("name"), core.getConfig().getString("network"), core.getConfig().getString("redishost"), core.getConfig().getString("redisauth"),false);
        ServerAPI.core = core;
        players = new HashMap<>();

        CommunicationUtils.init();
    }

    public static AuroraMC getCore() {
        return core;
    }

    public static void newPlayer(Player player, AuroraMCServerPlayer auroraMCServerPlayer) {
        players.put(player, auroraMCServerPlayer);
    }

    public static void playerLeave(Player player) {
        PlayerLeaveEvent event = new PlayerLeaveEvent(players.get(player));
        if (event.getPlayer().isLoaded()) {
            Bukkit.getPluginManager().callEvent(event);
        }
        players.remove(player);
    }

    public static AuroraMCServerPlayer getPlayer(Player player) {
        return players.get(player);
    }

    public static AuroraMCServerPlayer getDisguisedPlayer(String name) {
        for (AuroraMCServerPlayer player : players.values()) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getName().equalsIgnoreCase(name)) {
                    return player;
                }
            }
        }
        return null;
    }

    public static AuroraMCServerPlayer getPlayer(String name) {
        for (AuroraMCServerPlayer player : players.values()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }



    public static AuroraMCServerPlayer getPlayer(UUID uuid) {
        for (AuroraMCServerPlayer player : players.values()) {
            if (player.getUniqueId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public static ArrayList<AuroraMCServerPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public static void openGUI(AuroraMCServerPlayer player, GUI gui) {
        openGUIs.put(player, gui);
    }

    public static GUI getGUI(AuroraMCServerPlayer player) {
        return openGUIs.get(player);
    }

    public static void closeGUI(AuroraMCServerPlayer player) {
        openGUIs.remove(player);
    }

    public static Map<Integer, Hologram> getHolograms() {
        return holograms;
    }

    public static void registerHologram(Hologram hologram) {
        for (HologramLine value : hologram.getLines().values()) {
            holograms.put(value.getArmorStand().getId(), hologram);
        }
    }

    public static void deregisterHologram(Hologram hologram) {
        for (HologramLine value : hologram.getLines().values()) {
            holograms.remove(value.getArmorStand().getId());
        }
    }

    public static void registerFakePlayer(EntityPlayer player) {
        fakePlayers.put(player.getId(), player);
    }

    public static Map<Integer, EntityPlayer> getFakePlayers() {
        return fakePlayers;
    }

    public static int getOpenGUIs() {
        return openGUIs.size();
    }

    public static void enableChatSilence(short seconds, boolean sendMessage) {
        if (silenceTask != null) {
            silenceTask.cancel();
        }
        AuroraMCAPI.enableChatSilence(seconds);
        if (seconds != -1) {
            silenceTask = new BukkitRunnable(){
                @Override
                public void run() {
                    if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                        disableSilence();
                    }
                }
            }.runTaskLater(core, seconds*20);
        }

        ChatSlowLength length = new ChatSlowLength(seconds);
        if (sendMessage) {
            for (AuroraMCServerPlayer player : getPlayers()) {
                if (player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info")) {
                    player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("The chat has been silenced for **%s**. You are immune to Chat Silence due to your rank.", length.getFormatted())));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("The chat has been silenced for **%s**.", length.getFormatted())));
                }
            }
        }
    }

    public static void disableSilence() {
        AuroraMCAPI.disableSilence();
        for (AuroraMCServerPlayer player : getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Silence", "Chat is no longer silenced."));
        }
        if (silenceTask != null) {
            BukkitTask task = silenceTask;
            silenceTask = null;
            task.cancel();
        }
    }

    public static void loadEvent() throws IOException, InvalidPluginException, InvalidDescriptionException {
        if (eventMode) return;
        FileUtils.moveFileToDirectory(new File(Bukkit.getServer().getWorldContainer(), "AuroraMC-Events-1.0-SNAPSHOT.jar"), new File(Bukkit.getServer().getWorldContainer(), "plugins"), true);
        Bukkit.getPluginManager().loadPlugin(new File(Bukkit.getServer().getWorldContainer(), "plugins/AuroraMC-Events-1.0-SNAPSHOT.jar"));
        eventMode = true;
    }

    public static boolean isEventMode() {
        return eventMode;
    }

    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    public static void setShuttingDown(boolean shuttingDown) {
        ServerAPI.shuttingDown = shuttingDown;
    }


}
