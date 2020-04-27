package network.auroramc.core.api.backend.database;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.database.util.MySQLConnectionPool;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.Disguise;
import redis.clients.jedis.Jedis;

public class DatabaseManager {

    private MySQLConnectionPool mysql;
    private Jedis jedis;

    public DatabaseManager() {
        try {
            mysql = new MySQLConnectionPool(AuroraMCAPI.getCore().getConfig().getString("mysqlhost"), AuroraMCAPI.getCore().getConfig().getString("mysqlport"), AuroraMCAPI.getCore().getConfig().getString("mysqlusername"), AuroraMCAPI.getCore().getConfig().getString("mysqlpassword"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        jedis = new Jedis(AuroraMCAPI.getCore().getConfig().getString("redishost"),AuroraMCAPI.getCore().getConfig().getInt("redisport"), 5000);
        jedis.auth(AuroraMCAPI.getCore().getConfig().getString("redisauth"));
    }

    public Disguise getDisguise(AuroraMCPlayer player) {
        if (jedis.exists("disguise.")) {

        }
        return null;
    }

}
