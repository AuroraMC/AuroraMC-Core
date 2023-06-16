/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.backend.communication;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.events.server.ProtocolMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;

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
                    } catch (StreamCorruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        listening = false;
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
