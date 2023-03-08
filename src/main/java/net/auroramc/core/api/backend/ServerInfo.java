/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.backend;

import net.auroramc.core.api.AuroraMCAPI;
import org.json.JSONObject;

public class ServerInfo {

    private final String name, ip;
    private final JSONObject serverType;
    private final int protocolPort, port;
    private int buildNumber, lobbyBuildNumber, gameBuildNumber, engineBuildNumber, buildBuildNumber, duelsBuildNumber, pathfinderBuildNumber;
    private final Network network;
    private final boolean forced;
    private final String authKey;

    public ServerInfo(String name, String ip, int port, Network network, boolean forced, JSONObject serverType, int protocolPort, int buildNumber, int lobbyBuildNumber, int engineBuildNumber, int gameBuildNumber, int buildBuildNumber, int duelsBuildNumber, int pathfinderBuildNumber, String authKey) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.serverType = serverType;
        this.protocolPort = protocolPort;
        this.buildNumber = buildNumber;
        this.buildBuildNumber = buildBuildNumber;
        this.engineBuildNumber = engineBuildNumber;
        this.gameBuildNumber = gameBuildNumber;
        this.lobbyBuildNumber = lobbyBuildNumber;
        this.network = network;
        this.forced = forced;
        this.authKey = authKey;
        this.duelsBuildNumber = duelsBuildNumber;
        this.pathfinderBuildNumber = pathfinderBuildNumber;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public JSONObject getServerType() {
        return serverType;
    }

    public String getIp() {
        if (AuroraMCAPI.getServerInfo().getRawIP().equalsIgnoreCase(ip) && !AuroraMCAPI.getServerInfo().getName().equals(name)) {
            return "172.18.0.1";
        }
        return ip;
    }

    public int getProtocolPort() {
        return protocolPort;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public int getBuildBuildNumber() {
        return buildBuildNumber;
    }

    public int getEngineBuildNumber() {
        return engineBuildNumber;
    }

    public int getGameBuildNumber() {
        return gameBuildNumber;
    }

    public int getLobbyBuildNumber() {
        return lobbyBuildNumber;
    }

    public boolean isForced() {
        return forced;
    }

    public Network getNetwork() {
        return network;
    }

    public String getRawIP() {
        return ip;
    }

    public void setBuildBuildNumber(int buildBuildNumber) {
        this.buildBuildNumber = buildBuildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public void setEngineBuildNumber(int engineBuildNumber) {
        this.engineBuildNumber = engineBuildNumber;
    }

    public void setGameBuildNumber(int gameBuildNumber) {
        this.gameBuildNumber = gameBuildNumber;
    }

    public void setLobbyBuildNumber(int lobbyBuildNumber) {
        this.lobbyBuildNumber = lobbyBuildNumber;
    }

    public void setPathfinderBuildNumber(int pathfinderBuildNumber) {
        this.pathfinderBuildNumber = pathfinderBuildNumber;
    }

    public void setDuelsBuildNumber(int duelsBuildNumber) {
        this.duelsBuildNumber = duelsBuildNumber;
    }

    public int getDuelsBuildNumber() {
        return duelsBuildNumber;
    }

    public int getPathfinderBuildNumber() {
        return pathfinderBuildNumber;
    }

    public String getAuthKey() {
        return authKey;
    }

    public enum Network {MAIN, TEST, ALPHA}
}
