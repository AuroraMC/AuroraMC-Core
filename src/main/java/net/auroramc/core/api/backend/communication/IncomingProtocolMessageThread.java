package net.auroramc.core.api.backend.communication;

import net.auroramc.core.api.events.ProtocolMessageEvent;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class IncomingProtocolMessageThread extends Thread {

    private int port;
    private boolean listening;
    private ServerSocket socket;

    public IncomingProtocolMessageThread(int port) {
        this.port = port;
        this.setName("Incoming Protocol Messages Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(port)) {
            this.socket = socket;
            while (listening) {
                Socket connection = socket.accept();
                ObjectInputStream objectInputStream = (ObjectInputStream) connection.getInputStream();
                ProtocolMessage message = (ProtocolMessage) objectInputStream.readObject();
                ProtocolMessageEvent event = new ProtocolMessageEvent(message);
                Bukkit.getPluginManager().callEvent(event);
            }
        } catch (SocketException e) {
            listening = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
