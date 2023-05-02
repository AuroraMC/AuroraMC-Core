/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.events.server.ServerCloseRequestEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.ZipUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.io.File;
import java.util.Objects;

public class WorldListener implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        WorldCreator creator = new WorldCreator("smp");
        switch (Objects.requireNonNull(ServerAPI.getCore().getConfig().getString("type"))) {
            case "nether": {
                creator.environment(World.Environment.NETHER);
                break;
            }
            case "end": {
                creator.environment(World.Environment.THE_END);
                break;
            }
            case "overworld": {
                creator.environment(World.Environment.NORMAL);
                break;
            }
        }
        creator.seed(-1261677964);


        Bukkit.createWorld(creator);

        AuroraMCAPI.getLogger().info("AuroraMC-SMP loaded and ready to accept connections. Letting mission control know...");
        ProtocolMessage message = new ProtocolMessage(Protocol.SERVER_ONLINE, "Mission Control", "", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name());
        CommunicationUtils.sendMessage(message);
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
