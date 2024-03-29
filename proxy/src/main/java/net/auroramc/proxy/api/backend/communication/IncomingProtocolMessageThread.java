/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.api.backend.communication;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.events.ProtocolMessageEvent;
import net.md_5.bungee.api.ProxyServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;

public class IncomingProtocolMessageThread extends Thread {

    private int port;
    private boolean listening;
    private ServerSocket socket;

    public IncomingProtocolMessageThread(int port) {
        this.port = port;
        this.setName("Incoming Server Protocol Messages Thread");
        this.setDaemon(true);
        listening = true;
    }

    @Override
    public void run() {
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
                    try {
                        ProxyServer.getInstance().getPluginManager().callEvent(new ProtocolMessageEvent(message));
                    } catch (Exception e) {
                        ProxyAPI.getCore().getLogger().log(Level.WARNING, "An error occurred when attempting to call the protocol message event.", e);
                    }
                }  catch (StreamCorruptedException | EOFException ignored) {
                    //Ignore any stream corrupted exception.
                }
            }
        } catch (SocketException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            listening = false;
        } catch (IOException | ClassNotFoundException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
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
