package network.auroramc.core.api.backend;

import org.json.JSONObject;

public class ServerInfo {

    private final String name;
    private final String ip;
    private final int port;
    private final JSONObject serverType;

    public ServerInfo(String name, String ip, int port, JSONObject serverType) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.serverType = serverType;
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
        return ip;
    }
}
