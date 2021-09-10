package net.auroramc.core.api.backend.communication;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ServerInfo;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

public class CommunicationUtils {

    private static IncomingProtocolMessageThread task;

    public static void init() {
        if (task != null) {
            task.shutdown();
        }
        task = new IncomingProtocolMessageThread(AuroraMCAPI.getServerInfo().getProtocolPort());
        task.start();
    }

    public static UUID sendMessage(ProtocolMessage message) {
        if (message.getDestination().equalsIgnoreCase("Mission Control")) {

        }
        ServerInfo info = AuroraMCAPI.getDbManager().getServerDetailsByName(message.getDestination());
        if (info != null) {
            try (Socket socket = new Socket(info.getIp(), info.getProtocolPort())) {
                ObjectOutputStream outputStream = (ObjectOutputStream) socket.getOutputStream();
                outputStream.writeObject(message);
                outputStream.flush();
                return message.getUuid();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void shutdown() {
        if (task != null) {
            task.shutdown();
            task = null;
        }
    }

}