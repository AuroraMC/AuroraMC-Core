/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.api.backend.communication;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ProxyInfo;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;

public class CommunicationUtils {

    private static IncomingProtocolMessageThread task;

    public static void init() {
        if (task != null) {
            task.shutdown();
        }
        task = new IncomingProtocolMessageThread(AuroraMCAPI.getInfo().getProtocolPort());
        task.start();
    }

    public static UUID sendMessage(ProtocolMessage message) {
        if (message.getDestination().startsWith("Mission Control")) {
            try (Socket socket = new Socket("PLACEHOLDER", 35566)) {
                message.setProxy(UUID.fromString(AuroraMCAPI.getInfo().getName()));
                message.setAuthenticationKey(AuroraMCAPI.getInfo().getAuthKey());
                message.setNetwork(AuroraMCAPI.getInfo().getNetwork().name());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
                outputStream.flush();
                return message.getUuid();
            } catch (Exception e) {
                return sendMessage(message, 1);
            }
        }
        ProxyInfo info = AuroraMCAPI.getDbManager().getProxyInfo(message.getDestination(), AuroraMCAPI.getInfo().getNetwork().name());
        if (info != null) {
            try (Socket socket = new Socket(info.getIp(), info.getProtocolPort())) {
                message.setProxy(UUID.fromString(info.getName()));
                message.setAuthenticationKey(info.getAuthKey());
                message.setNetwork(info.getNetwork().name());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
                outputStream.flush();
                return message.getUuid();
            } catch (Exception e) {
                return sendMessage(message, 1);
            }
        }
        return null;
    }

    public static UUID sendMessage(ProtocolMessage message, int level) {
        if (message.getDestination().startsWith("Mission Control")) {
            try (Socket socket = new Socket("PLACEHOLDER", 35566)) {
                message.setProxy(UUID.fromString(AuroraMCAPI.getInfo().getName()));
                message.setAuthenticationKey(AuroraMCAPI.getInfo().getAuthKey());
                message.setNetwork(AuroraMCAPI.getInfo().getNetwork().name());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
                outputStream.flush();
                return message.getUuid();
            } catch (Exception e) {
                if (level > 4) {
                    AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                    return null;
                }
                return sendMessage(message, level + 1);
            }
        }
        ProxyInfo info = AuroraMCAPI.getDbManager().getProxyInfo(message.getDestination(), AuroraMCAPI.getInfo().getNetwork().name());
        if (info != null) {
            try (Socket socket = new Socket(info.getIp(), info.getProtocolPort())) {
                message.setProxy(UUID.fromString(info.getName()));
                message.setAuthenticationKey(info.getAuthKey());
                message.setNetwork(info.getNetwork().name());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
                outputStream.flush();
                return message.getUuid();
            } catch (Exception e) {
                if (level > 4) {
                    AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                    return null;
                }
                return sendMessage(message, level + 1);
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

    public static boolean isShutdown() {
        return task == null;
    }

}
