/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.backend.communication;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.server.ProtocolMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class IncomingProtocolMessageThread extends Thread {

    private int port;
    private boolean listening;
    private ServerSocket socket;

    public IncomingProtocolMessageThread(int port) {
        this.port = port;
        this.setName("Incoming Protocol Messages Thread");
        this.setDaemon(true);
        listening = true;
    }

    @Override
    public void run() {
        while (listening) {
            try (ServerSocket socket = new ServerSocket(port)) {
                this.socket = socket;
                while (listening) {
                    try (Socket connection = socket.accept()) {
                        ObjectInputStream objectInputStream = new ObjectInputStream(connection.getInputStream());
                        ProtocolMessage message = (ProtocolMessage) objectInputStream.readObject();
                        if (!message.getAuthenticationKey().equals(AuroraMCAPI.getInfo().getAuthKey())) {
                            //Check if the auth keys match.
                            return;
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                AuroraMCAPI.getLogger().info("Received plugin message for protocol: " + message.getProtocol().name());
                                ProtocolMessageEvent event = new ProtocolMessageEvent(message);
                                Bukkit.getPluginManager().callEvent(event);
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    } catch (StreamCorruptedException | EOFException ignored) {
                        //Ignore any stream corrupted exception.
                    }
                }
            } catch (Exception e) {
                AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            }
        }
    }

    public void shutdown() {
        listening = false;
        try {
            socket.close();
        } catch (Exception e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

}
