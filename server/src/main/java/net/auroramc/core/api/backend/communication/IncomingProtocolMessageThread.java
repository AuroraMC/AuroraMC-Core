/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.backend.communication;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.server.ProtocolMessageEvent;
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
