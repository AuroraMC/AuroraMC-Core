/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.info;

import net.auroramc.api.AuroraMCAPI;
import org.json.JSONObject;

public class ServerInfo extends Info {


    private final JSONObject serverType;
    private int buildNumber, lobbyBuildNumber, gameBuildNumber, engineBuildNumber, buildBuildNumber, duelsBuildNumber, pathfinderBuildNumber;

    private int currentPlayers;
    private int maxPlayers;
    private ServerState serverState;
    private String activeGame;
    private String activeMap;
    public ServerInfo(String name, String ip, int port, Network network, boolean forced, JSONObject serverType, int protocolPort, int buildNumber, int lobbyBuildNumber, int engineBuildNumber, int gameBuildNumber, int buildBuildNumber, int duelsBuildNumber, int pathfinderBuildNumber, String authKey) {
        super(name, ip, port, network, forced, protocolPort, authKey);
        this.serverType = serverType;
        this.buildNumber = buildNumber;
        this.buildBuildNumber = buildBuildNumber;
        this.engineBuildNumber = engineBuildNumber;
        this.gameBuildNumber = gameBuildNumber;
        this.lobbyBuildNumber = lobbyBuildNumber;
        this.duelsBuildNumber = duelsBuildNumber;
        this.pathfinderBuildNumber = pathfinderBuildNumber;
    }

    public JSONObject getServerType() {
        return serverType;
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

    public void fetchData() {
        String[] data = AuroraMCAPI.getDbManager().fetchData(this).split(";");
        String[] players = data[1].split("/");
        currentPlayers = Integer.parseInt(players[0]);
        maxPlayers = Integer.parseInt(players[1]);
        serverState = ServerState.valueOf(data[0]);
        activeGame = data[2];
        activeMap = data[3];
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getActiveGame() {
        return activeGame;
    }

    public String getActiveMap() {
        return activeMap;
    }

    public ServerState getServerState() {
        return serverState;
    }
}
