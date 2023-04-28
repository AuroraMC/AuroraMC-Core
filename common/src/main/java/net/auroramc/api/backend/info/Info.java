/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.backend.info;

import net.auroramc.api.AuroraMCAPI;

public abstract class Info {

    private final String name, ip;
    private final Network network;
    private final boolean forced;
    private final String authKey;
    private final int protocolPort, port;

    public Info(String name, String ip, int port, Network network, boolean forced, int protocolPort, String authKey) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.protocolPort = protocolPort;
        this.network = network;
        this.forced = forced;
        this.authKey = authKey;
    }

    public boolean isForced() {
        return forced;
    }

    public Info.Network getNetwork() {
        return network;
    }

    public String getRawIP() {
        return ip;
    }

    public String getIp() {
        if (AuroraMCAPI.getInfo().getRawIP().equalsIgnoreCase(ip) && !AuroraMCAPI.getInfo().getName().equals(name)) {
            return "172.18.0.1";
        }
        return ip;
    }

    public int getProtocolPort() {
        return protocolPort;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public String getAuthKey() {
        return authKey;
    }

    public enum Network {MAIN, TEST, ALPHA}


}
