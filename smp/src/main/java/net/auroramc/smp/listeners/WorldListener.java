/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.backend.communication.CommunicationUtils;
import net.auroramc.smp.api.backend.communication.Protocol;
import net.auroramc.smp.api.backend.communication.ProtocolMessage;
import net.auroramc.smp.api.events.server.ServerCloseRequestEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.*;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.util.Arrays;
import java.util.Objects;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldInitEvent e) {
        if (e.getWorld().getName().contains("world")) {
            e.getWorld().setKeepSpawnInMemory(false);
            e.getWorld().setAutoSave(false);
            e.getWorld().setSpawnFlags(false, false);
            //Disable entity spawning.
            for (SpawnCategory category : SpawnCategory.values()) {
                if (category == SpawnCategory.MISC) {
                    continue;
                }
                e.getWorld().setTicksPerSpawns(category, 0);
            }
        }
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        WorldCreator creator = new WorldCreator("smp");
        switch (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type"))) {
            case "NETHER": {
                creator.environment(World.Environment.NETHER);
                break;
            }
            case "END": {
                creator.environment(World.Environment.THE_END);
                break;
            }
            case "OVERWORLD": {
                creator.environment(World.Environment.NORMAL);
                break;
            }
        }
        creator.seed(-1261677964);

        creator.generateStructures(true);
        World smp = Bukkit.createWorld(creator);

        smp.setPVP(false);
        smp.setGameRule(GameRule.DO_FIRE_TICK, false);
        if (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type")).equalsIgnoreCase("OVERWORLD")) {
            smp.setGameRule(GameRule.KEEP_INVENTORY, true);
        }
        smp.setGameRule(GameRule.DO_INSOMNIA, true);
        smp.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 35);
        smp.setDifficulty(Difficulty.HARD);

        AuroraMCAPI.getLogger().info("AuroraMC-SMP loaded and ready to accept connections. Letting mission control know...");
        ProtocolMessage message = new ProtocolMessage(Protocol.SERVER_ONLINE, "Mission Control", "", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name());
        CommunicationUtils.sendMessage(message);

        World world = Bukkit.getWorld("world");
        world.setKeepSpawnInMemory(false);
        for (Chunk chunk : Arrays.asList(world.getLoadedChunks())) {
            world.unloadChunk(chunk);
        }
        Bukkit.unloadWorld(world, false);

        world = Bukkit.getWorld("world_nether");
        world.setKeepSpawnInMemory(false);
        for (Chunk chunk : Arrays.asList(world.getLoadedChunks())) {
            world.unloadChunk(chunk);
        }

        world = Bukkit.getWorld("world_the_end");
        world.setKeepSpawnInMemory(false);
        for (Chunk chunk : Arrays.asList(world.getLoadedChunks())) {
            world.unloadChunk(chunk);
        }
        Bukkit.unloadWorld(world, false);
    }

    @EventHandler
    public void onShutdown(ServerCloseRequestEvent e) {
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "NuttersSMP is being restarted for an update. You are being sent to a Lobby."));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Lobby");
            out.writeUTF(player.getUniqueId().toString());
            player.sendPluginMessage(out.toByteArray());
        }
        /*World world = Bukkit.getWorld("smp");
        assert world != null;
        Bukkit.unloadWorld(world, true);
        File file = world.getWorldFolder();
        ZipUtil.zipFile();*/
        ServerAPI.setShuttingDown(true);
        CommunicationUtils.sendMessage(new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", e.getType(), AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name()));
    }

}
