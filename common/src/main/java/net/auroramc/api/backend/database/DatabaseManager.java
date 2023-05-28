/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.backend.database;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.CrateFactory;
import net.auroramc.api.abstraction.DisguiseFactory;
import net.auroramc.api.backend.info.*;
import net.auroramc.api.backend.store.Payment;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.Crate;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.player.*;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.punishments.*;
import net.auroramc.api.punishments.ipprofiles.IPProfile;
import net.auroramc.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.api.punishments.ipprofiles.ProfileComparison;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.GameStatistics;
import net.auroramc.api.stats.PlayerBank;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.*;
import net.auroramc.api.utils.disguise.CachedSkin;
import net.auroramc.api.utils.disguise.Skin;
import net.md_5.bungee.api.ChatColor;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseManager {

    private final MySQLConnectionPool mysql;
    private final JedisPool jedis;

    public DatabaseManager(String host, String port, String db, String username, String password, String redisHost, String redisAuth) {
        //Setting up MySQL connection pool.
        MySQLConnectionPool mysql1;
        try {
            mysql1 = new MySQLConnectionPool(host, port, db, username, password);
        } catch (ClassNotFoundException e) {
            mysql1 = null;
            e.printStackTrace();
        }
        mysql = mysql1;

        //Setting up Redis connection pool.
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);
        config.setMaxIdle(128);
        config.setMinIdle(16);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        config.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        config.setNumTestsPerEvictionRun(3);
        config.setBlockWhenExhausted(true);
        jedis = new JedisPool(config, redisHost, 6379, 2000, redisAuth);
    }

    public ProxyInfo getProxyInfo(String proxyUUID, String network) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM proxies WHERE uuid = ? AND network = ?");
            statement.setString(1, proxyUUID);
            statement.setString(2, network);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new ProxyInfo(UUID.fromString(set.getString(1)), set.getString(2), set.getInt(3), ServerInfo.Network.valueOf(set.getString(4)), set.getBoolean(5), set.getInt(6), set.getInt(7), set.getString(8));
            } else {
                AuroraMCAPI.getLogger().info("test");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Disguise getDisguise(AuroraMCPlayer player, UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("disguise.%s.skin", uuid))) {
                //They have an active disguise.
                String skin,signature,name;
                Rank rank;

                skin = connection.get(String.format("disguise.%s.skin", uuid));
                signature = connection.get(String.format("disguise.%s.signature", uuid));
                name = connection.get(String.format("disguise.%s.name", uuid));

                rank = Rank.getByID(Integer.parseInt(connection.get(String.format("disguise.%s.rank", uuid))));

                return DisguiseFactory.newDisguise(player, name, skin, signature, rank);
            }
        }

        return null;
    }

    public Disguise getDisguise(UUID player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("disguise.%s.skin", player.toString()))) {
                //They have an active disguise.
                String skin,name;
                Rank rank;

                skin = connection.get(String.format("disguise.%s.skin", player.toString()));
                name = connection.get(String.format("disguise.%s.name", player.toString()));

                rank = Rank.getByID(Integer.parseInt(connection.get(String.format("disguise.%s.rank", player.toString()))));

                return DisguiseFactory.newDisguise((AuroraMCPlayer) null, name, skin, rank);
            }
        }

        return null;
    }

    public Disguise getDisguise(String uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("disguise.%s.skin", uuid))) {
                //They have an active disguise.
                String skin,signature,name;
                Rank rank;

                skin = connection.get(String.format("disguise.%s.skin", uuid));
                signature = connection.get(String.format("disguise.%s.signature", uuid));
                name = connection.get(String.format("disguise.%s.name", uuid));

                rank = Rank.getByID(Integer.parseInt(connection.get(String.format("disguise.%s.rank", uuid))));

                return DisguiseFactory.newDisguise(name, skin, signature, rank);
            }
        }

        return null;
    }

    public void setDisguise(AuroraMCPlayer player, Disguise disguise) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            if (disguise == null) {
                return;
            }
            pipeline.set(String.format("disguise.%s.skin", player.getUniqueId().toString()), disguise.getSkin());
            if (disguise.getSignature() != null) {
                pipeline.set(String.format("disguise.%s.signature", player.getUniqueId().toString()), disguise.getSignature());
            }
            pipeline.set(String.format("disguise.%s.name", player.getUniqueId().toString()), disguise.getName());
            pipeline.set(String.format("disguise.%s.rank", player.getUniqueId().toString()), disguise.getRank().getId() + "");
            pipeline.set(String.format("disguisenames.%s", disguise.getName()), player.getUniqueId().toString());
            pipeline.sync();
        }
    }

    public void undisguise(AuroraMCPlayer player, Disguise disguise) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            pipeline.del(String.format("disguise.%s.skin", player.getUniqueId().toString()));
            pipeline.del(String.format("disguise.%s.signature", player.getUniqueId().toString()));
            pipeline.del(String.format("disguise.%s.name", player.getUniqueId().toString()));
            pipeline.del(String.format("disguise.%s.rank", player.getUniqueId().toString()));
            pipeline.del(String.format("disguisenames.%s", disguise.getName()));
            pipeline.sync();
        }
    }

    public void undisguise(String uuid, String disguiseName) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            pipeline.del(String.format("disguise.%s.skin", uuid));
            pipeline.del(String.format("disguise.%s.signature", uuid));
            pipeline.del(String.format("disguise.%s.name", uuid));
            pipeline.del(String.format("disguise.%s.rank", uuid));
            pipeline.del(String.format("disguisenames.%s", disguiseName));
            pipeline.sync();
        }
    }

    public boolean isAlreadyDisguise(String name) {
        try (Jedis connection = jedis.getResource()) {
            return connection.exists(String.format("disguisenames.%s", name));
        }
    }

    public UUID getUUIDFromDisguise(String name) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("disguisenames.%s", name))) {
                return UUID.fromString(connection.get(String.format("disguisenames.%s", name)));
            }
            return null;
        }
    }

    public int newUser(AuroraMCPlayer player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auroramc_players(uuid, `name`) VALUES (?, ?);");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());

            statement.execute();

            int id = getAuroraMCID(player.getUniqueId());
            //Creating records in necessary databases.

            statement = connection.prepareStatement("INSERT INTO ignored(amc_id, users) VALUES (?,'');");
            statement.setInt(1, id);
            statement.execute();

            statement = connection.prepareStatement("INSERT INTO ranks(amc_id, rank) VALUES (?, 0);");
            statement.setInt(1, id);
            statement.execute();

            try (Jedis con = jedis.getResource()) {
                Pipeline pipeline = con.pipelined();
                //Setting core stats stuff as otherwise the server core wont be able to load player stats properly.
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "xpEarned", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "xpIntoLevel", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "level", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "lobbyTimeMs", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "gameTimeMs", 0);
                pipeline.hset(String.format("stats.%s.core", player.getUniqueId().toString()), "firstJoinTimestamp", System.currentTimeMillis() + "");
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "crownsEarned", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "ticketsEarned", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesPlayed", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesWon", 0);
                pipeline.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesLost", 0);
                pipeline.hincrBy(String.format("bank.%s", player.getUniqueId().toString()), "crowns", 0);
                pipeline.hincrBy(String.format("bank.%s", player.getUniqueId().toString()), "tickets", 0);
                pipeline.hset(String.format("friends.%s", player.getUniqueId().toString()), "visibility", "ALL");
                pipeline.hset(String.format("friends.%s", player.getUniqueId().toString()), "status", "101");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "channel", "ALL");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "friendRequests", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "partyRequests", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "muteInformMode", "DISABLED");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "chatVisibility", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "privateMessageMode", "ALL");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "pingOnPrivateMessage", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "pingOnPartyChat", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "pingOnChatMention", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "hubVisibility", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "hubSpeed", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "hubFlight", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "reportNotifications", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "hubInvisibility", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "ignoreHubKnockback", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "socialMediaNotifications", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "staffLoginNotifications", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "approvalNotifications", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "approvalProcessedNotifications", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "hubForcefield", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "hideDisguiseName", "false");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "pingOnChatMention", "true");
                pipeline.hset(String.format("prefs.%s", player.getUniqueId().toString()), "preferredPronouns", "NONE");
                pipeline.hincrBy("stat.uniqueplayerjoins", "DAILY", 1);
                pipeline.hincrBy("stat.uniqueplayerjoins", "WEEKLY", 1);
                pipeline.hincrBy("stat.uniqueplayerjoins", "ALLTIME", 1);
                pipeline.sync();
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int newUser(UUID uuid, String name) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auroramc_players(uuid, `name`) VALUES (?, ?);");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);

            statement.execute();

            int id = getAuroraMCID(uuid);
            //Creating records in necessary databases.

            statement = connection.prepareStatement("INSERT INTO ignored(amc_id, users) VALUES (?,'');");
            statement.setInt(1, id);
            statement.execute();

            statement = connection.prepareStatement("INSERT INTO ranks(amc_id, rank) VALUES (?, 0);");
            statement.setInt(1, id);
            statement.execute();

            try (Jedis con = jedis.getResource()) {
                //Setting core stats stuff as otherwise the server core wont be able to load player stats properly.
                Pipeline pipeline = con.pipelined();
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "xpEarned", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "xpIntoLevel", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "level", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "lobbyTimeMs", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "gameTimeMs", 0);
                pipeline.hset(String.format("stats.%s.core", uuid), "firstJoinTimestamp", System.currentTimeMillis() + "");
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "crownsEarned", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "ticketsEarned", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "gamesPlayed", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "gamesWon", 0);
                pipeline.hincrBy(String.format("stats.%s.core", uuid), "gamesLost", 0);
                pipeline.hincrBy(String.format("bank.%s", uuid), "crowns", 0);
                pipeline.hincrBy(String.format("bank.%s", uuid), "tickets", 0);
                pipeline.hset(String.format("friends.%s", uuid), "visibility", "ALL");
                pipeline.hset(String.format("friends.%s", uuid), "status", "101");
                pipeline.hset(String.format("prefs.%s", uuid), "channel", "ALL");
                pipeline.hset(String.format("prefs.%s", uuid), "friendRequests", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "partyRequests", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "muteInformMode", "DISABLED");
                pipeline.hset(String.format("prefs.%s", uuid), "chatVisibility", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "privateMessageMode", "ALL");
                pipeline.hset(String.format("prefs.%s", uuid), "pingOnPrivateMessage", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "pingOnPartyChat", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "pingOnChatMention", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "hubVisibility", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "hubSpeed", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "hubFlight", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "reportNotifications", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "hubInvisibility", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "ignoreHubKnockback", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "socialMediaNotifications", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "staffLoginNotifications", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "approvalNotifications", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "approvalProcessedNotifications", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "hubForcefield", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "hideDisguiseName", "false");
                pipeline.hset(String.format("prefs.%s", uuid), "pingOnChatMention", "true");
                pipeline.hset(String.format("prefs.%s", uuid), "preferredPronouns", "NONE");
                pipeline.hincrBy("stat.uniqueplayerjoins", "DAILY", 1);
                pipeline.hincrBy("stat.uniqueplayerjoins", "WEEKLY", 1);
                pipeline.hincrBy("stat.uniqueplayerjoins", "ALLTIME", 1);
                pipeline.sync();
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int getAuroraMCID(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM auroramc_players WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getInt(1);
            } else {
                //NEW USER
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int getAuroraMCID(String name) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM auroramc_players WHERE name = ?");
            statement.setString(1, name);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getInt(1);
            } else {
                //NEW USER
                return -1;
            }

        } catch (SQLException e) {
            return -2;
        }
    }

    public UUID getUUIDFromID(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM auroramc_players WHERE id = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return UUID.fromString(set.getString(1));
            } else {
                //NEW USER
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID getUUIDFromName(String name) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM auroramc_players WHERE name = ?");
            statement.setString(1, name);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return UUID.fromString(set.getString(1));
            } else {
                //NEW USER
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNameFromID(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getString(1);
            } else {
                //NEW USER
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNameFromUUID(String uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE uuid = ?");
            statement.setString(1, uuid);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getString(1);
            } else {
                //NEW USER
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rank getRank(AuroraMCPlayer player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT rank FROM `ranks` WHERE amc_id = ?");
            statement.setLong(1, player.getId());

            ResultSet set = statement.executeQuery();
            if (set.next()) {

                return Rank.getByID(set.getInt(1));
            } else {
                //NEW USER
                statement = connection.prepareStatement("INSERT INTO ranks (amc_id, rank) VALUES (?, ?)");
                statement.setLong(1, player.getId());
                statement.setInt(2, 0);
                return Rank.getByID(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rank getRank(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT rank FROM `ranks` WHERE amc_id = ?");
            statement.setLong(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {

                return Rank.getByID(set.getInt(1));
            } else {
                //NEW USER
                return Rank.getByID(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rank getRank(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT rank FROM `ranks` WHERE amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return Rank.getByID(set.getInt(1));
            } else {
                //NEW USER
                return Rank.getByID(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<SubRank> getSubRanks(AuroraMCPlayer player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT subranks FROM `ranks` WHERE amc_id = ?");
            statement.setLong(1, player.getId());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (set.getString(1) == null) {
                    return new ArrayList<>();
                }
                String[] ranks = set.getString(1).split(",");
                Arrays.sort(ranks);
                ArrayList<SubRank> subRanks = new ArrayList<>();
                for (String rank : ranks) {
                    subRanks.add(SubRank.getByID(Integer.parseInt(rank)));
                }
                return subRanks;
            } else {
                //NEW USER
                return new ArrayList<>();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<SubRank> getSubRanks(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT subranks FROM `ranks` WHERE amc_id = ?");
            statement.setLong(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (set.getString(1) == null) {
                    return new ArrayList<>();
                }
                String[] ranks = set.getString(1).split(",");
                Arrays.sort(ranks);
                ArrayList<SubRank> subRanks = new ArrayList<>();
                for (String rank : ranks) {
                    subRanks.add(SubRank.getByID(Integer.parseInt(rank)));
                }
                return subRanks;
            } else {
                //NEW USER
                return new ArrayList<>();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<SubRank> getSubRanks(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT subranks FROM `ranks` WHERE amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (set.getString(1) == null) {
                    return new ArrayList<>();
                }
                String[] ranks = set.getString(1).split(",");
                Arrays.sort(ranks);
                ArrayList<SubRank> subRanks = new ArrayList<>();
                for (String rank : ranks) {
                    subRanks.add(SubRank.getByID(Integer.parseInt(rank)));
                }
                return subRanks;
            } else {
                //NEW USER
                return new ArrayList<>();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPanelCode(UUID uuid, String code) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("panel.code." + uuid.toString(), code);
            connection.expire("panel.code." + uuid, 60);
        }
    }

    public boolean hasAccount(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePanelPassword(UUID uuid, String password) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET password = ? WHERE uuid = ?");
            statement.setString(1, password);
            statement.setString(2, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePanelAccount(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean setRank(AuroraMCPlayer player, Rank rank) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE ranks SET rank = ? WHERE amc_id = ?");
            statement.setInt(1, rank.getId());
            statement.setLong(2, player.getId());

            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setRank(int id, Rank rank, Rank oldRank) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE ranks SET rank = ? WHERE amc_id = ?");
            statement.setInt(1, rank.getId());
            statement.setLong(2, id);
            boolean success = statement.execute();

            statement = connection.prepareStatement("SELECT discord_id FROM dc_links WHERE amc_id = ?");
            statement.setLong(1, id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                //The user has an active discord link, update ranks for discord.
                String discordId = results.getString(1);
                statement = connection.prepareStatement("SELECT * FROM rank_changes WHERE discord_id = ?");
                statement.setString(1, discordId);
                results = statement.executeQuery();

                if (results.next()) {
                    //There are already registered rank/subrank changes in the database. Check to see if a rank update has already occured.
                    if (results.getString(2) != null) {
                        if (results.getString(2).equals(rank.getId() + "")) {
                            statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND old_rank = ?");
                            statement.setString(1, discordId);
                            statement.setString(2, results.getString(2));
                            statement.execute();
                            return success;
                        }
                        //The first result was a rank change, just update the new_rank column then return.
                        statement = connection.prepareStatement("UPDATE rank_changes SET new_rank = ? WHERE discord_id = ? AND old_rank = ?");
                        statement.setString(1, rank.getId() + "");
                        statement.setString(2, discordId);
                        statement.setString(3, results.getString(2));
                        statement.execute();
                        return success;
                    }
                    while (results.next()) {
                        if (results.getString(2) != null) {
                            if (results.getString(2).equals(rank.getId() + "")) {
                                statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND old_rank = ?");
                                statement.setString(1, discordId);
                                statement.setString(2, results.getString(2));
                                statement.execute();
                                return success;
                            }
                            //The first result was a rank change, just update the new_rank column then return.
                            statement = connection.prepareStatement("UPDATE rank_changes SET new_rank = ? WHERE discord_id = ? AND old_rank = ?");
                            statement.setString(1, rank.getId() + "");
                            statement.setString(2, discordId);
                            statement.setString(3, results.getString(2));
                            statement.execute();
                            return success;
                        }
                    }

                    //If not returned by now, its not already in the database, so just insert it.
                    statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, old_rank, new_rank) VALUES (?,?,?)");
                    statement.setString(1, discordId);
                    statement.setString(2, oldRank.getId() + "");
                    statement.setString(3, rank.getId() + "");
                    statement.execute();
                } else {
                    //Just insert, it is the only rank update so far.
                    statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, old_rank, new_rank) VALUES (?,?,?)");
                    statement.setString(1, discordId);
                    statement.setString(2, oldRank.getId() + "");
                    statement.setString(3, rank.getId() + "");
                    statement.execute();
                }
            }

            return success;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean giveSubrank(int id, SubRank subRank) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT subranks FROM ranks WHERE amc_id = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();

            statement = connection.prepareStatement("UPDATE ranks SET subranks = ? WHERE amc_id = ?");

            if (set.next()) {
                if (set.getString(1) == null) {
                    statement.setString(1, "" + subRank.getId());
                } else {
                    ArrayList<String> subranks = new ArrayList<>(Arrays.asList(set.getString(1).split(",")));
                    subranks.add(subRank.getId() + "");
                    statement.setString(1, String.join(",", subranks));
                }
            } else {
                statement = connection.prepareStatement("INSERT INTO ranks(amc_id, rank, subranks) VALUES (?, ?, ?)");
                statement.setInt(1, id);
                statement.setInt(2, 0);
                statement.setString(3, subRank.getId() + "");
                boolean success = statement.execute();

                statement = connection.prepareStatement("SELECT discord_id FROM dc_links WHERE amc_id = ?");
                statement.setLong(1, id);

                ResultSet results = statement.executeQuery();
                if (results.next()) {
                    //The user has an active discord link, update ranks for discord.
                    String discordId = results.getString(1);
                    statement = connection.prepareStatement("SELECT * FROM rank_changes WHERE discord_id = ?");
                    statement.setString(1, discordId);
                    results = statement.executeQuery();
                    if (results.next()) {
                        if (results.getString(4) != null) {
                            if (results.getString(4).equals("-" + subRank.getId())) {
                                statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                                statement.setString(1, discordId);
                                statement.setString(2, "-" + subRank.getId());

                                statement.execute();
                                return success;

                            }
                        }

                        while (results.next()) {
                            if (results.getString(4).equals("-" + subRank.getId())) {
                                statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                                statement.setString(1, discordId);
                                statement.setString(2, "-" + subRank.getId());

                                statement.execute();
                                return success;
                            }
                        }

                        statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                        statement.setString(1, discordId);
                        statement.setString(2, "+" + subRank.getId());
                        statement.execute();
                    } else {
                        //Just insert, it is the only rank update so far.
                        statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                        statement.setString(1, discordId);
                        statement.setString(2, "+" + subRank.getId());
                        statement.execute();
                    }
                }

                return success;
            }

            statement.setInt(2, id);
            boolean success =  statement.execute();

            statement = connection.prepareStatement("SELECT discord_id FROM dc_links WHERE amc_id = ?");
            statement.setLong(1, id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                //The user has an active discord link, update ranks for discord.
                String discordId = results.getString(1);
                statement = connection.prepareStatement("SELECT * FROM rank_changes WHERE discord_id = ?");
                statement.setString(1, discordId);
                results = statement.executeQuery();
                if (results.next()) {
                    if (results.getString(4) != null) {
                        if (results.getString(4).equals("-" + subRank.getId())) {
                            statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                            statement.setString(1, discordId);
                            statement.setString(2, "-" + subRank.getId());

                            statement.execute();
                            return success;

                        }
                    }

                    while (results.next()) {
                        if (results.getString(4).equals("-" + subRank.getId())) {
                            statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                            statement.setString(1, discordId);
                            statement.setString(2, "-" + subRank.getId());

                            statement.execute();
                            return success;
                        }
                    }

                    statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                    statement.setString(1, discordId);
                    statement.setString(2, "+" + subRank.getId());
                    statement.execute();
                } else {
                    //Just insert, it is the only rank update so far.
                    statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                    statement.setString(1, discordId);
                    statement.setString(2, "+" + subRank.getId());
                    statement.execute();
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean revokeSubrank(int id, SubRank subRank) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT subranks FROM ranks WHERE amc_id = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();

            statement = connection.prepareStatement("UPDATE ranks SET subranks = ? WHERE amc_id = ?");

            if (set.next()) {
                if (set.getString(1) != null) {
                    ArrayList<String> subranks = new ArrayList<>(Arrays.asList(set.getString(1).split(",")));
                    subranks.remove(subRank.getId() + "");
                    if (subranks.size() == 0) {
                        statement.setNull(1,  12);
                    } else {
                        statement.setString(1, String.join(",", subranks));
                    }
                } else {
                    return true;
                }
            } else {
                statement = connection.prepareStatement("INSERT INTO ranks(amc_id, rank) VALUES (?, ?)");
                statement.setInt(1, id);
                statement.setInt(2, 0);
                boolean success = statement.execute();

                statement = connection.prepareStatement("SELECT discord_id FROM dc_links WHERE amc_id = ?");
                statement.setLong(1, id);

                ResultSet results = statement.executeQuery();
                if (results.next()) {
                    //The user has an active discord link, update ranks for discord.
                    String discordId = results.getString(1);
                    statement = connection.prepareStatement("SELECT * FROM rank_changes WHERE discord_id = ?");
                    statement.setString(1, discordId);
                    results = statement.executeQuery();
                    if (results.next()) {
                        if (results.getString(4) != null) {
                            if (results.getString(4).equals("+" + subRank.getId())) {
                                statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                                statement.setString(1, discordId);
                                statement.setString(2, "+" + subRank.getId());

                                statement.execute();
                                return success;

                            }
                        }

                        while (results.next()) {
                            if (results.getString(4).equals("-" + subRank.getId())) {
                                statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                                statement.setString(1, discordId);
                                statement.setString(2, "+" + subRank.getId());

                                statement.execute();
                                return success;
                            }
                        }

                        statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                        statement.setString(1, discordId);
                        statement.setString(2, "-" + subRank.getId());
                        statement.execute();
                    } else {
                        //Just insert, it is the only rank update so far.
                        statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                        statement.setString(1, discordId);
                        statement.setString(2, "-" + subRank.getId());
                        statement.execute();
                    }
                }

                return success;
            }

            statement.setInt(2, id);
            boolean success = statement.execute();

            statement = connection.prepareStatement("SELECT discord_id FROM dc_links WHERE amc_id = ?");
            statement.setLong(1, id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                //The user has an active discord link, update ranks for discord.
                String discordId = results.getString(1);
                statement = connection.prepareStatement("SELECT * FROM rank_changes WHERE discord_id = ?");
                statement.setString(1, discordId);
                results = statement.executeQuery();
                if (results.next()) {
                    if (results.getString(4) != null) {
                        if (results.getString(4).equals("+" + subRank.getId())) {
                            statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                            statement.setString(1, discordId);
                            statement.setString(2, "+" + subRank.getId());

                            statement.execute();
                            return success;

                        }
                    }

                    while (results.next()) {
                        if (results.getString(4).equals("-" + subRank.getId())) {
                            statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND subrank_change = ?");
                            statement.setString(1, discordId);
                            statement.setString(2, "+" + subRank.getId());

                            statement.execute();
                            return success;
                        }
                    }

                    statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                    statement.setString(1, discordId);
                    statement.setString(2, "-" + subRank.getId());
                    statement.execute();
                } else {
                    //Just insert, it is the only rank update so far.
                    statement = connection.prepareStatement("INSERT INTO rank_changes(discord_id, subrank_change) VALUES (?,?)");
                    statement.setString(1, discordId);
                    statement.setString(2, "-" + subRank.getId());
                    statement.execute();
                }
            }

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getDiscord(long id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT discord_id FROM dc_links WHERE amc_id = ?");
            statement.setLong(1, id);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return set.getString(1);
            }
            return null;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean codeExists(String code) {
        try (Jedis connection = jedis.getResource()) {
            return connection.exists(String.format("discord.link.%s", code));
        }
    }

    public void newCode(String code, AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            connection.set(String.format("discord.link.%s", code), player.getUniqueId().toString());
            connection.expire(String.format("discord.link.%s", code), 60);
        }
    }

    public List<Rule> getRules() {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM rules ORDER BY type ASC, weight ASC, rule_id ASC");

            ResultSet results = statement.executeQuery();
            List<Rule> rules = new ArrayList<>();
            while (results.next()) {
                Rule rule = new Rule(results.getInt(1),results.getString(2),results.getString(3), Integer.parseInt(results.getString(4)),Integer.parseInt(results.getString(6)), results.getBoolean(7), results.getBoolean(5));
                rules.add(rule);
            }

            return rules;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Punishment> getPunishmentHistory(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT punishments.punishment_id,punishments.rule_id,punishments.notes,punishments.punisher,punishments.issued,punishments.expire,punishments.status,punishments.evidence,punishments.suffix,punishments.removal_reason,punishments.remover,punishments.removal_timestamp,auroramc_players.name FROM punishments INNER JOIN auroramc_players ON auroramc_players.id=punishments.punisher WHERE (amc_id = ?) ORDER BY issued DESC");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();

            ArrayList<Punishment> punishments = new ArrayList<>();
            while (set.next()) {
                punishments.add(new Punishment(set.getString(1), id, set.getInt(2), set.getString(3), set.getInt(4), Long.parseLong(set.getString(5)),Long.parseLong(set.getString(6)), set.getInt(7), set.getString(8), set.getInt(9), set.getString(10), set.getString(11), ((set.getString(12) == null)?-1:Long.parseLong(set.getString(12))), set.getString(13)));
            }
            return punishments;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean banCodeExists(String code) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM punishments WHERE punishment_id = ?");
            statement.setString(1, code);

            ResultSet results = statement.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void issuePunishment(String code, int amc_id, int ruleID, String extraNotes, int punisherID, long issued, long expire, int status, String uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO punishments(punishment_id,amc_id,rule_id,notes,punisher,issued,expire,status) VALUES (?,?,?,?,?,?,?,?)");
            statement.setString(1, code);
            statement.setInt(2, amc_id);
            statement.setInt(3, ruleID);
            statement.setString(4, extraNotes);
            statement.setInt(5, punisherID);
            statement.setString(6, issued + "");
            statement.setString(7, expire + "");
            statement.setString(8, status + "");

            statement.execute();

            //Insert into redis.
            if (uuid != null) {
                try (Jedis con = jedis.getResource()) {
                    if (con.sismember("bans", uuid)) {
                        //This is a check to ensure that the oldest expiry time remains in the redis, so as not to add a short punishment length to the redis, resulting in bans not applying properly.
                        //This is a super rare case but is possible. Better safe than sorry.
                        if (con.exists(String.format("bans.%s", uuid))) {
                            long ttl = con.ttl(String.format("bans.%s", uuid));

                            if (ttl == -1) {
                                //this means that its a perma ban. do not remove it from the database.
                                return;
                            } else {
                                if (ttl > (int) ((expire-issued)/1000)) {
                                    //The time to live is longer, meaning it will expire after this one would, so do not add this to the redis.
                                    return;
                                }
                            }
                        }
                    } else {
                        con.sadd("bans", uuid);
                    }
                    con.set(String.format("bans.%s", uuid), ruleID + ";" + extraNotes + ";" + status + ";" + issued + ";" + expire + ";" + code);

                    if (expire != -1) {
                        con.expire(String.format("bans.%s", uuid), (int) ((expire-issued)/1000));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Ban getBan(String uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.sismember("bans", uuid)) {
                if (connection.exists(String.format("bans.%s", uuid))) {
                    String[] args = connection.get(String.format("bans.%s", uuid)).split(";");
                    int ruleID = Integer.parseInt(args[0]);
                    String notes = args[1];
                    int status = Integer.parseInt(args[2]);
                    long issued = Long.parseLong(args[3]);
                    long expire = Long.parseLong(args[4]);
                    String code = args[5];

                    return new Ban(code, ruleID, issued, expire, status,notes);
                } else {
                    //Ban is expired as the status isn't there.
                    connection.srem("bans", uuid);
                }
            }
        }
        return null;
    }

    public void expirePunishment(String punishmentCode, int currentStatus) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE punishments SET status = ? WHERE punishment_id = ?");
            if (currentStatus == 3) {
                statement.setInt(1, 6);
            } else {
                statement.setInt(1, 5);
            }

            statement.setString(2, punishmentCode);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ServerInfo getServerDetails(String ip, int port) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM servers WHERE ip = ? AND port = ?");
            statement.setString(1, ip);
            statement.setInt(2, port);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new ServerInfo(set.getString(1), set.getString(2), set.getInt(3), ServerInfo.Network.valueOf(set.getString(4)), set.getBoolean(5), new JSONObject(set.getString(6)), set.getInt(7), set.getInt(8), set.getInt(9), set.getInt(10), set.getInt(11), set.getInt(12), set.getInt(13), set.getInt(14), set.getString(15));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ServerInfo getServerDetailsByName(String name, String network) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM servers WHERE servername = ? AND network = ?");
            statement.setString(1, name);
            statement.setString(2, network);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new ServerInfo(set.getString(1), set.getString(2), set.getInt(3), ServerInfo.Network.valueOf(set.getString(4)), set.getBoolean(5), new JSONObject(set.getString(6)), set.getInt(7), set.getInt(8), set.getInt(9), set.getInt(10), set.getInt(11), set.getInt(12), set.getInt(13), set.getInt(14), set.getString(15));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getServer(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid.toString()))) {
                return connection.get(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid));
            }
        }

        return null;
    }

    public void removePunishment(AuroraMCPlayer remover, long timestamp, String reason, Punishment punishment, UUID uuid, List<Punishment> punishments) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE punishments SET status = ?, removal_reason = ?,remover = ?, removal_timestamp = ? WHERE punishment_id = ?");
            if (punishment.getStatus() == 3) {
                statement.setInt(1, 6);
            } else {
                statement.setInt(1, 5);
            }
            statement.setString(2, reason);

            statement.setString(3, remover.getName());
            statement.setLong(4, timestamp);
            statement.setString(5, punishment.getPunishmentCode());
            statement.execute();

            if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() != 1) {
                //This is a ban, remove it from redis (if it is the ban that is currently there).
                try (Jedis con = jedis.getResource()) {
                    if (con.sismember("bans", uuid.toString())) {
                        if (con.exists(String.format("bans.%s", uuid))) {
                            String[] args = con.get(String.format("bans.%s", uuid)).split(";");
                            String code = args[5];
                            if (code.equalsIgnoreCase(punishment.getPunishmentCode())) {
                                //If this is not the case, there is another punishment that has been applied since this was. Check to see if another ban is valid.
                                punishments = punishments.stream().filter(punishment2 -> punishment2.getStatus() == 1 || punishment2.getStatus() == 2 || punishment2.getStatus() == 3).collect(Collectors.toList());
                                for (Punishment punishment1 : punishments) {
                                    if (AuroraMCAPI.getRules().getRule(punishment1.getRuleID()).getType() != 1 && (punishment1.getExpire() > System.currentTimeMillis() || punishment1.getExpire() == -1) && (punishment1.getRemover() == null) && !punishment1.getPunishmentCode().equalsIgnoreCase(punishment.getPunishmentCode())) {
                                        //This is an active ban. Apply this one instead.
                                        con.set(String.format("bans.%s", uuid), punishment1.getRuleID() + ";" + punishment1.getExtraNotes() + ";" + punishment1.getStatus() + ";" + punishment1.getIssued() + ";" + punishment1.getExpire() + ";" + punishment1.getPunishmentCode());

                                        if (punishment1.getExpire() != -1) {
                                            con.expire(String.format("bans.%s", uuid), (int) ((punishment1.getExpire()-timestamp)/1000));
                                        }

                                        return;
                                    }
                                }
                                //No valid bans, just remove the ban from redis.
                                con.srem("bans", uuid.toString());
                                con.del(String.format("bans.%s", uuid));
                            }
                        } else {
                            //Ban is expired as the record isn't there.
                            con.srem("bans", uuid.toString());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePunishment(String remover, long timestamp, String reason, Punishment punishment, UUID uuid, List<Punishment> punishments) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE punishments SET status = ?, removal_reason = ?,remover = ?, removal_timestamp = ? WHERE punishment_id = ?");
            if (punishment.getStatus() == 3) {
                statement.setInt(1, 6);
            } else {
                statement.setInt(1, 5);
            }
            statement.setString(2, reason);

            statement.setString(3, remover);
            statement.setLong(4, timestamp);
            statement.setString(5, punishment.getPunishmentCode());
            statement.execute();

            if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() != 1) {
                //This is a ban, remove it from redis (if it is the ban that is currently there).
                try (Jedis con = jedis.getResource()) {
                    if (con.sismember("bans", uuid.toString())) {
                        if (con.exists(String.format("bans.%s", uuid))) {
                            String[] args = con.get(String.format("bans.%s", uuid)).split(";");
                            String code = args[5];
                            if (code.equalsIgnoreCase(punishment.getPunishmentCode())) {
                                //If this is not the case, there is another punishment that has been applied since this was. Check to see if another ban is valid.
                                punishments = punishments.stream().filter(punishment2 -> punishment2.getStatus() == 1 || punishment2.getStatus() == 2 || punishment2.getStatus() == 3).collect(Collectors.toList());
                                for (Punishment punishment1 : punishments) {
                                    if (AuroraMCAPI.getRules().getRule(punishment1.getRuleID()).getType() != 1 && (punishment1.getExpire() > System.currentTimeMillis() || punishment1.getExpire() == -1) && (punishment1.getRemover() == null) && !punishment1.getPunishmentCode().equalsIgnoreCase(punishment.getPunishmentCode())) {
                                        //This is an active ban. Apply this one instead.
                                        con.set(String.format("bans.%s", uuid), punishment1.getRuleID() + ";" + punishment1.getExtraNotes() + ";" + punishment1.getStatus() + ";" + punishment1.getIssued() + ";" + punishment1.getExpire() + ";" + punishment1.getPunishmentCode());

                                        if (punishment1.getExpire() != -1) {
                                            con.expire(String.format("bans.%s", uuid), (int) ((punishment1.getExpire()-timestamp)/1000));
                                        }

                                        return;
                                    }
                                }
                                //No valid bans, just remove the ban from redis.
                                con.srem("bans", uuid.toString());
                                con.del(String.format("bans.%s", uuid));
                            }
                        } else {
                            //Ban is expired as the record isn't there.
                            con.srem("bans", uuid.toString());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Punishment getPunishment(String code) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT punishments.punishment_id,punishments.amc_id,punishments.rule_id,punishments.notes,punishments.punisher,punishments.issued,punishments.expire,punishments.status,punishments.evidence,punishments.suffix,punishments.removal_reason,punishments.remover,punishments.removal_timestamp,auroramc_players.name FROM punishments INNER JOIN auroramc_players ON auroramc_players.id=punishments.punisher WHERE (punishment_id = ?) ORDER BY issued DESC");
            statement.setString(1, code);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new Punishment(code, set.getInt(2), set.getInt(3), set.getString(4), set.getInt(5), Long.parseLong(set.getString(6)),Long.parseLong(set.getString(7)), set.getInt(8), set.getString(9), set.getInt(10), set.getString(11), set.getString(12), ((set.getString(13) == null)?-1:Long.parseLong(set.getString(13))), set.getString(14));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void attachEvidence(String code, String evidence) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE punishments SET evidence = ? WHERE punishment_id = ?");
            statement.setString(1, evidence);
            statement.setString(2, code);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Mentor> getMentors() {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mentee_distribution");

            ResultSet set = statement.executeQuery();
            ArrayList<Mentor> mentors = new ArrayList<>();
            while (set.next()) {
                List<Mentee> mentees = new ArrayList<>();
                String[] moniteeIds = set.getString(2).split(",");
                for (String moniteeId : moniteeIds) {
                    try {
                        statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                        statement.setInt(1, Integer.parseInt(moniteeId));
                        ResultSet name = statement.executeQuery();
                        name.next();
                        mentees.add(new Mentee(Integer.parseInt(moniteeId), name.getString(1)));
                    } catch (NumberFormatException ignored) {
                    }
                }
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(1));
                ResultSet name = statement.executeQuery();
                name.next();
                mentors.add(new Mentor(set.getInt(1),name.getString(1), mentees));
            }
            return mentors;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMentee(int id, int mentee) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mentee_distribution WHERE mentor = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                //This is an already existing mentor. Add the ID to the list.
                List<String> menteeIds = Arrays.asList(set.getString(2).split(","));
                menteeIds.add(mentee + "");
                statement = connection.prepareStatement("UPDATE mentee_distribution SET mentees = ? WHERE mentor = ?");
                statement.setString(1, String.join(",",menteeIds));
                statement.setInt(2, id);
                statement.execute();
            } else {
                statement = connection.prepareStatement("INSERT INTO mentee_distribution VALUES (?,?)");
                statement.setInt(1, id);
                statement.setString(2, mentee + "");
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Punishment> getUnassignedPunishments() {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT punishments.punishment_id, punishments.amc_id, punishments.rule_id, punishments.notes, punishments.punisher, punishments.issued, punishments.expire, punishments.status, punishments.evidence, punishments.suffix, punishments.removal_reason, punishments.remover, punishments.removal_timestamp, auroramc_players.name FROM punishments INNER JOIN auroramc_players ON auroramc_players.id=punishments.punisher WHERE (SELECT COUNT(*) FROM mentee_distribution WHERE NOT (mentees REGEXP CONCAT('^', punishments.punisher,'$|^', punishments.punisher,',.*$|^.*,', punishments.punisher,'$|^.*,', punishments.punisher,',.*$'))) AND punishments.status = 2 ORDER BY issued ASC");
            ResultSet set = statement.executeQuery();

            List<Punishment> punishments = new ArrayList<>();
            while (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(2));
                ResultSet name = statement.executeQuery();
                name.next();
                punishments.add(new Punishment(set.getString(1), set.getInt(2), set.getInt(3), set.getString(4), set.getInt(5), Long.parseLong(set.getString(6)),Long.parseLong(set.getString(7)), set.getInt(8), set.getString(9), set.getInt(10), set.getString(11), set.getString(12), ((set.getString(13) == null)?-1:Long.parseLong(set.getString(13))), set.getString(14), name.getString(1)));
            }
            return punishments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Punishment> getUnprocessedPunishments(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT punishments.punishment_id, punishments.amc_id, punishments.rule_id, punishments.notes, punishments.punisher, punishments.issued, punishments.expire, punishments.status, punishments.evidence, punishments.suffix, punishments.removal_reason, punishments.remover, punishments.removal_timestamp, auroramc_players.name FROM punishments INNER JOIN auroramc_players ON auroramc_players.id=punishments.punisher WHERE punisher = ? AND punishments.status = 2 ORDER BY issued ASC");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();

            List<Punishment> punishments = new ArrayList<>();
            while (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(2));
                ResultSet name = statement.executeQuery();
                name.next();
                punishments.add(new Punishment(set.getString(1), set.getInt(2), set.getInt(3), set.getString(4), set.getInt(5), Long.parseLong(set.getString(6)),Long.parseLong(set.getString(7)), set.getInt(8), set.getString(9), set.getInt(10), set.getString(11), set.getString(12), ((set.getString(13) == null)?-1:Long.parseLong(set.getString(13))), set.getString(14), name.getString(1)));
            }
            return punishments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void approvePunishment(Punishment punishment) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE punishments SET status = ? WHERE punishment_id = ?");
            statement.setInt(1, 3);
            statement.setString(2, punishment.getPunishmentCode());

            statement.execute();

            if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() != 1) {
                statement = connection.prepareStatement("SELECT uuid FROM auroramc_players WHERE id = ?");
                statement.setInt(1, punishment.getPunished());
                ResultSet set = statement.executeQuery();
                set.next();
                String uuid = set.getString(1);
                try (Jedis con = jedis.getResource()) {
                    if (con.sismember("bans", uuid)) {
                        //This is a check to ensure that the oldest expiry time remains in the redis, so as not to add a short punishment length to the redis, resulting in bans not applying properly.
                        //This is a super rare case but is possible. Better safe than sorry.
                        if (con.exists(String.format("bans.%s", uuid))) {
                            String[] args = con.get(String.format("bans.%s", uuid)).split(";");
                            String code = args[5];
                            if (code.equalsIgnoreCase(punishment.getPunishmentCode())) {
                                con.set(String.format("bans.%s", uuid), punishment.getRuleID() + ";" + punishment.getExtraNotes() + ";3;" + punishment.getIssued() + ";" + punishment.getExpire() + ";" + code);
                                if (punishment.getExpire() != -1) {
                                    con.expire(String.format("bans.%s", uuid), (int) ((punishment.getExpire()-System.currentTimeMillis())/1000));
                                }
                            }
                        } else {
                            con.srem("bans", uuid);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void denyPunishment(Punishment punishment) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE punishments SET status = ? WHERE punishment_id = ?");
            statement.setInt(1, 4);
            statement.setString(2, punishment.getPunishmentCode());

            statement.execute();

            if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() != 1) {
                statement = connection.prepareStatement("SELECT uuid FROM auroramc_players WHERE id = ?");
                statement.setInt(1, punishment.getPunished());
                ResultSet set = statement.executeQuery();
                set.next();
                String uuid = set.getString(1);
                try (Jedis con = jedis.getResource()) {
                    if (con.sismember("bans", uuid)) {
                        //This is a check to ensure that the oldest expiry time remains in the redis, so as not to add a short punishment length to the redis, resulting in bans not applying properly.
                        //This is a super rare case but is possible. Better safe than sorry.
                        if (con.exists(String.format("bans.%s", uuid))) {
                            String[] args = con.get(String.format("bans.%s", uuid)).split(";");
                            String code = args[5];
                            if (code.equalsIgnoreCase(punishment.getPunishmentCode())) {
                                con.srem("bans", uuid);
                                con.del(String.format("bans.%s", uuid));
                            }
                        } else {
                            con.srem("bans", uuid);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isVanished(AuroraMCPlayer player) {
        try (Jedis con = jedis.getResource()) {
            return con.sismember("vanish", player.getUniqueId().toString());
        }
    }

    public boolean isVanished(UUID player) {
        try (Jedis con = jedis.getResource()) {
            return con.sismember("vanish", player.toString());
        }
    }

    public void channelSet(AuroraMCPlayer player, ChatChannel chatChannel) {
        try (Jedis redisConnection = jedis.getResource()) {
            redisConnection.hset(String.format("prefs.%s", player.getUniqueId()), "channel", chatChannel.name());
        }
    }

    public void vanish(AuroraMCPlayer player) {
        try (Jedis con = jedis.getResource()) {
            con.sadd("vanish", player.getUniqueId().toString());
        }
    }

    public void unvanish(AuroraMCPlayer player) {
        try (Jedis con = jedis.getResource()) {
            con.srem("vanish", player.getUniqueId().toString());
        }
    }

    public void unvanish(String uuid) {
        try (Jedis con = jedis.getResource()) {
            con.srem("vanish", uuid);
        }
    }

    public void removeMentee(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mentee_distribution WHERE mentees LIKE ?");
            statement.setString(1, "%" + id + "%");

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                statement = connection.prepareStatement("UPDATE mentee_distribution SET mentees = ? WHERE mentor = ?");
                List<String> menteeIds = new ArrayList<>(Arrays.asList(set.getString(2).split(",")));
                menteeIds.remove(id + "");
                statement.setString(1, String.join(",",menteeIds));
                statement.setInt(2, set.getInt(1));
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendRawCommand(String sql, Object... args) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            int i = 1;
            for (Object object : args) {
                statement.setObject(i, object);
                i++;
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getMySQLConnection() throws SQLException {
        return mysql.getConnection();
    }

    public CachedSkin getCachedSkin(String uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("skincache.%s.skin", uuid))) {
                // This skin is cached.
                return new CachedSkin(connection.get(String.format("skincache.%s.skin", uuid)), connection.get(String.format("skincache.%s.signature", uuid)), Long.parseLong(connection.get(String.format("skincache.%s.fetched", uuid))));
            } else {
                return null;
            }
        }
    }

    public void cacheSkin(String uuid, String skin, String signature, long fetched) {
        try (Jedis connection = jedis.getResource()) {
            connection.set(String.format("skincache.%s.skin", uuid), skin);
            connection.set(String.format("skincache.%s.signature", uuid), signature);
            connection.set(String.format("skincache.%s.fetched", uuid), fetched + "");
        }
    }

    public List<AdminNote> getAdminNotes(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT admin_notes.note_id,admin_notes.user_id,auroramc_players.name,admin_notes.timestamp,admin_notes.note FROM admin_notes INNER JOIN auroramc_players ON auroramc_players.id=admin_notes.added_by WHERE user_id = ? ORDER BY timestamp DESC");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            List<AdminNote> notes = new ArrayList<>();
            while (set.next()) {
                   notes.add(new AdminNote(set.getInt(1), set.getInt(2), set.getString(3), Long.parseLong(set.getString(4)), set.getString(5)));
            }
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addNewNote(int id, int issuer, long timestamp, String note) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO admin_notes(user_id, added_by, timestamp, note) VALUES (?,?,?,?)");
            statement.setInt(1, id);
            statement.setInt(2, issuer);
            statement.setString(3, timestamp + "");
            statement.setString(4, note);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void globalAccountSuspend(String code, int id, int issuer, long timestamp, String note) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT profile_id FROM ip_logs WHERE amc_id = ?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            List<String> profiles = new ArrayList<>();
            while (set.next()) {
                profiles.add(set.getString(1));
            }

            //Add these IP profiles to the list of banned ones.
            statement = connection.prepareStatement("INSERT INTO global_account_suspensions(punishment_id, root_account, issuer, banned_profiles, timestamp, reason) VALUES (?,?,?,?,?,?)");
            statement.setString(1, code);
            statement.setInt(2, id);
            statement.setInt(3, issuer);
            statement.setString(4, String.join(",",profiles));
            statement.setString(5, timestamp + "");
            statement.setString(6, note);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerProfile ipLookup(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement =  connection.prepareStatement("SELECT id,name,last_used_profile FROM auroramc_players WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                int id = set.getInt(1);
                String name = set.getString(2);
                if (set.getInt(3) > 0) {
                    int profile = set.getInt(3);
                    statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = (SELECT last_used_by FROM ip_profile WHERE profile_id = ?)");
                    statement.setInt(1, profile);
                    set = statement.executeQuery();
                    set.next();
                    String lastUsedby = set.getString(1);

                    statement = connection.prepareStatement("SELECT * FROM ip_logs WHERE amc_id = ? ORDER BY last_used DESC");
                    statement.setInt(1, id);
                    set = statement.executeQuery();
                    List<String> profiles = new ArrayList<>();
                    List<String> sharedAccounts = new ArrayList<>();
                    int bans = 0;
                    int mutes = 0;
                    int numberOfProfiles = 0;
                    while (set.next()) {
                        numberOfProfiles++;
                        if (numberOfProfiles <= 5) {
                            profiles.add(set.getString(2));
                        }
                        statement = connection.prepareStatement("SELECT ip_logs.amc_id, auroramc_players.name FROM ip_logs INNER JOIN auroramc_players ON auroramc_players.id=ip_logs.amc_id WHERE profile_id = ?");
                        statement.setInt(1, set.getInt(2));
                        ResultSet set2 = statement.executeQuery();
                        while (set2.next()) {
                            if (!sharedAccounts.contains(set2.getString(2))) {
                                sharedAccounts.add(set2.getString(2));
                                statement = connection.prepareStatement("SELECT * FROM punishments WHERE amc_id = ? AND (status = 1 OR status = 2 OR status = 3)");
                                statement.setInt(1, set2.getInt(1));
                                ResultSet set3 = statement.executeQuery();
                                boolean banned = false;
                                boolean muted = false;
                                while (set3.next() && (!muted || !banned)) {
                                    if (AuroraMCAPI.getRules().getRule(set3.getInt(3)).getType() == 1) {
                                        muted = true;
                                    } else {
                                        banned = true;
                                    }
                                }
                                if (muted) {
                                    mutes++;
                                }
                                if (banned) {
                                    bans++;
                                }
                            }
                        }
                    }

                    boolean globalAccountSuspension = false;
                    String globalAccountSuspensionReason = null;

                    statement = connection.prepareStatement("SELECT global_account_suspensions.reason FROM global_account_suspensions WHERE (root_account = ?)");
                    statement.setInt(1, id);
                    set = statement.executeQuery();
                    if (set.next()) {
                        globalAccountSuspension = true;
                        globalAccountSuspensionReason = set.getString(1);
                    }

                    sharedAccounts.remove(name);


                    if (sharedAccounts.size() > 5) {
                        sharedAccounts = sharedAccounts.subList(0,5);
                    }

                    return new PlayerProfile(name, numberOfProfiles, profiles, profile, lastUsedby, sharedAccounts.size(), sharedAccounts, bans, mutes, globalAccountSuspension, globalAccountSuspensionReason);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IPProfile ipLookup(int profileId) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT profile_id, ip, last_used_by, last_used_at, ap.name FROM ip_profile INNER JOIN auroramc_players ap on ip_profile.last_used_by = ap.id WHERE profile_id = ?");
            statement.setInt(1, profileId);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                long lastUsedAt = set.getTimestamp(4).getTime();
                String lastUsedBy = set.getString(5);
                List<String> sharedAccounts = new ArrayList<>();
                int bans = 0;
                int mutes = 0;
                statement = connection.prepareStatement("SELECT ip_logs.amc_id, auroramc_players.name FROM ip_logs INNER JOIN auroramc_players ON auroramc_players.id=ip_logs.amc_id WHERE profile_id = ?");
                statement.setInt(1, set.getInt(1));
                ResultSet set2 = statement.executeQuery();
                while (set2.next()) {
                    if (!sharedAccounts.contains(set2.getString(2))) {
                        sharedAccounts.add(set2.getString(2));
                        statement = connection.prepareStatement("SELECT * FROM punishments WHERE amc_id = ? AND (status = 1 OR status = 2 OR status = 3)");
                        statement.setInt(1, set2.getInt(1));
                        ResultSet set3 = statement.executeQuery();
                        boolean banned = false;
                        boolean muted = false;
                        while (set3.next() && (!muted || !banned)) {
                            if (AuroraMCAPI.getRules().getRule(set3.getInt(3)).getType() == 1) {
                                muted = true;
                            } else {
                                banned = true;
                            }
                        }
                        if (muted) {
                            mutes++;
                        }
                        if (banned) {
                            bans++;
                        }
                    }
                }

                boolean globalAccountSuspension = false;
                String globalAccountSuspensionReason = null;

                statement = connection.prepareStatement("SELECT global_account_suspensions.reason FROM global_account_suspensions WHERE (banned_profiles REGEXP CONCAT('^', ?,'$|^', ?,',.*$|^.*,', ?,'$|^.*,', ?,',.*$'))");
                statement.setInt(1, profileId);
                statement.setInt(2, profileId);
                statement.setInt(3, profileId);
                statement.setInt(4, profileId);
                set = statement.executeQuery();
                if (set.next()) {
                    globalAccountSuspension = true;
                    globalAccountSuspensionReason = set.getString(1);
                }

                if (sharedAccounts.size() > 5) {
                    sharedAccounts = sharedAccounts.subList(0,5);
                }

                return new IPProfile(profileId, sharedAccounts.size(), sharedAccounts, lastUsedBy, lastUsedAt, bans, mutes, globalAccountSuspension, globalAccountSuspensionReason);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ProfileComparison ipLookup(UUID uuid, UUID uuid2) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement =  connection.prepareStatement("SELECT id,name,last_used_profile FROM auroramc_players WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String user1name = set.getString(2);
                int user1id = set.getInt(1);
                int user1profile = set.getInt(3);

                statement =  connection.prepareStatement("SELECT id,name,last_used_profile FROM auroramc_players WHERE uuid = ?");
                statement.setString(1, uuid2.toString());
                set = statement.executeQuery();
                if (set.next()) {
                    String user2name = set.getString(2);
                    int user2id = set.getInt(1);
                    int user2profile = set.getInt(3);
                    statement = connection.prepareStatement("SELECT * FROM ip_logs WHERE amc_id = ? ORDER BY last_used DESC");
                    statement.setInt(1, user1id);
                    set = statement.executeQuery();
                    List<String> profiles = new ArrayList<>();
                    List<String> sharedAccounts = new ArrayList<>();
                    int bans = 0;
                    int mutes = 0;
                    while (set.next()) {
                        profiles.add(set.getString(2));
                        statement = connection.prepareStatement("SELECT ip_logs.amc_id, auroramc_players.name FROM ip_logs INNER JOIN auroramc_players ON auroramc_players.id=ip_logs.amc_id WHERE profile_id = ?");
                        statement.setInt(1, set.getInt(2));
                        ResultSet set2 = statement.executeQuery();
                        while (set2.next()) {
                            if (!sharedAccounts.contains(set2.getString(2))) {
                                sharedAccounts.add(set2.getString(2));
                            }
                        }
                    }
                    List<String> finalProfiles = new ArrayList<>();
                    List<String> finalAccounts = new ArrayList<>();
                    int numberOfProfiles = 0;
                    int numberOfAccounts = 0;
                    statement = connection.prepareStatement("SELECT * FROM ip_logs WHERE amc_id = ? ORDER BY last_used DESC");
                    statement.setInt(1, user2id);
                    set = statement.executeQuery();
                    while (set.next()) {
                        if (profiles.contains(set.getString(2))) {
                            numberOfProfiles++;
                            if (finalProfiles.size() <= 4) {
                                finalProfiles.add(set.getString(2));
                            }
                        }
                        statement = connection.prepareStatement("SELECT ip_logs.amc_id, auroramc_players.name FROM ip_logs INNER JOIN auroramc_players ON auroramc_players.id=ip_logs.amc_id WHERE profile_id = ?");
                        statement.setInt(1, set.getInt(2));
                        ResultSet set2 = statement.executeQuery();
                        while (set2.next()) {
                            if (!finalAccounts.contains(set2.getString(2)) && sharedAccounts.contains(set2.getString(2))) {
                                numberOfAccounts++;
                                finalAccounts.add(set2.getString(2));
                                statement = connection.prepareStatement("SELECT * FROM punishments WHERE amc_id = ? AND (status = 1 OR status = 2 OR status = 3)");
                                statement.setInt(1, set2.getInt(1));
                                ResultSet set3 = statement.executeQuery();
                                boolean banned = false;
                                boolean muted = false;
                                while (set3.next() && (!muted || !banned)) {
                                    if (AuroraMCAPI.getRules().getRule(set3.getInt(3)).getType() == 1) {
                                        muted = true;
                                    } else {
                                        banned = true;
                                    }
                                }
                                if (muted) {
                                    mutes++;
                                }
                                if (banned) {
                                    bans++;
                                }
                            }
                        }
                    }
                    if (finalAccounts.remove(user1name)) {
                        numberOfAccounts--;
                    }
                    if (finalAccounts.remove(user2name)) {
                        numberOfAccounts--;
                    }

                    return new ProfileComparison(user1name, user2name, numberOfAccounts, finalAccounts, numberOfProfiles, finalProfiles, bans, mutes, user1profile, user2profile);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ChatFilter loadFilter() {
        try (Jedis connection = jedis.getResource()) {
            List<String> coreWords = new ArrayList<>(connection.smembers("filter.core"));
            List<String> whitelist = new ArrayList<>(connection.smembers("filter.whitelist"));
            List<String> blacklist = new ArrayList<>(connection.smembers("filter.blacklist"));
            List<String> phrases = new ArrayList<>(connection.smembers("filter.phrases"));
            List<String> replacements = new ArrayList<>(connection.smembers("filter.replacements"));
            return new ChatFilter(coreWords, blacklist, whitelist, replacements, phrases);
        }
    }

    public PlayerStatistics getStatistics(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            Map<Integer, GameStatistics> gameStats = new HashMap<>();

            Map<String, String> stats = connection.hgetAll(String.format("stats.%s.game", player.getUniqueId().toString()));
            //Load game statistics.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                String[] s = stat.getKey().split("[.]");
                int gameId = Integer.parseInt(s[0]);
                if (!gameStats.containsKey(gameId)) {
                    Map<String, Long> ss = new HashMap<>();
                    ss.put(s[1], Long.parseLong(stat.getValue()));
                    gameStats.put(gameId, new GameStatistics(gameId, ss));
                } else {
                    gameStats.get(gameId).addStat(s[1], Long.parseLong(stat.getValue()));
                }
            }

            stats = connection.hgetAll(String.format("stats.%s.achievements", player.getUniqueId().toString()));
            Map<Achievement, Integer> achievements = new HashMap<>();
            //Load Achievements.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                Achievement achievement = AuroraMCAPI.getAchievement(Integer.parseInt(stat.getKey()));
                int level = Integer.parseInt(stat.getValue());
                achievements.put(achievement, level);
            }

            stats = connection.hgetAll(String.format("stats.%s.achievements.progress", player.getUniqueId().toString()));
            Map<Achievement, Long> progress = new HashMap<>();
            //Load Achievements.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                Achievement achievement = AuroraMCAPI.getAchievement(Integer.parseInt(stat.getKey()));
                long amount = Long.parseLong(stat.getValue());
                progress.put(achievement, amount);
            }

            //Load core stuff.
            long totalXpEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "xpEarned"));
            long firstJoinTimestamp = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "firstJoinTimestamp"));
            long xpIntoLevel = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "xpIntoLevel"));
            int level = Integer.parseInt(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "level"));
            long lobbyTimeMs = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "lobbyTimeMs"));
            long gameTimeMs = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "gameTimeMs"));

            long ticketsEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "ticketsEarned"));
            long crownsEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "crownsEarned"));
            long gamesWon = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesWon"));
            long gamesLost = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesLost"));
            long gamesPlayed = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesPlayed"));

            return new PlayerStatistics(player, player.getId(), firstJoinTimestamp, totalXpEarned, xpIntoLevel, level, achievements, progress, gameStats, lobbyTimeMs, gameTimeMs, gamesPlayed, gamesWon, gamesLost, ticketsEarned, crownsEarned);
        }
    }

    public PlayerStatistics getStatistics(UUID uuid, int id) {
        try (Jedis connection = jedis.getResource()) {
            Map<Integer, GameStatistics> gameStats = new HashMap<>();

            Map<String, String> stats = connection.hgetAll(String.format("stats.%s.game", uuid.toString()));
            //Load game statistics.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                String[] s = stat.getKey().split("[.]");
                int gameId = Integer.parseInt(s[0]);
                if (!gameStats.containsKey(gameId)) {
                    Map<String, Long> ss = new HashMap<>();
                    ss.put(s[1], Long.parseLong(stat.getValue()));
                    gameStats.put(gameId, new GameStatistics(gameId, ss));
                } else {
                    gameStats.get(gameId).addStat(s[1], Long.parseLong(stat.getValue()));
                }
            }

            stats = connection.hgetAll(String.format("stats.%s.achievements", uuid.toString()));
            Map<Achievement, Integer> achievements = new HashMap<>();
            //Load Achievements.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                Achievement achievement = AuroraMCAPI.getAchievement(Integer.parseInt(stat.getKey()));
                int level = Integer.parseInt(stat.getValue());
                achievements.put(achievement, level);
            }

            stats = connection.hgetAll(String.format("stats.%s.achievements.progress", uuid.toString()));
            Map<Achievement, Long> progress = new HashMap<>();
            //Load Achievements.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                Achievement achievement = AuroraMCAPI.getAchievement(Integer.parseInt(stat.getKey()));
                long amount = Long.parseLong(stat.getValue());
                progress.put(achievement, amount);
            }

            //Load core stuff.
            long totalXpEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "xpEarned"));
            long firstJoinTimestamp = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "firstJoinTimestamp"));
            long xpIntoLevel = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "xpIntoLevel"));
            int level = Integer.parseInt(connection.hget(String.format("stats.%s.core", uuid.toString()), "level"));
            long lobbyTimeMs = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "lobbyTimeMs"));
            long gameTimeMs = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "gameTimeMs"));

            long ticketsEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "ticketsEarned"));
            long crownsEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "crownsEarned"));
            long gamesWon = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "gamesWon"));
            long gamesLost = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "gamesLost"));
            long gamesPlayed = Long.parseLong(connection.hget(String.format("stats.%s.core", uuid.toString()), "gamesPlayed"));

            return new PlayerStatistics(null, id, firstJoinTimestamp, totalXpEarned, xpIntoLevel, level, achievements, progress, gameStats, lobbyTimeMs, gameTimeMs, gamesPlayed, gamesWon, gamesLost, ticketsEarned, crownsEarned);
        }
    }

    public void ticketsEarned(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "ticketsEarned", amount);
        }
    }

    public void crownsEarned(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "crownsEarned", amount);
        }
    }

    public PlayerBank getBank(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            long crowns = Long.parseLong(connection.hget(String.format("bank.%s", player.getUniqueId().toString()), "crowns"));
            long tickets = Long.parseLong(connection.hget(String.format("bank.%s", player.getUniqueId().toString()), "tickets"));
            return new PlayerBank(player, tickets, crowns);
        }
    }

    public PlayerBank getBank(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            long crowns = Long.parseLong(connection.hget(String.format("bank.%s", uuid.toString()), "crowns"));
            long tickets = Long.parseLong(connection.hget(String.format("bank.%s", uuid), "tickets"));
            return new PlayerBank(null, tickets, crowns);
        }
    }

    public void ticketsAdded(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("bank.%s", player.getUniqueId().toString()), "tickets", amount);
        }
    }

    public void crownsAdded(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("bank.%s", player.getUniqueId().toString()), "tickets", amount);
        }
    }

    public void ticketsAdded(UUID uuid, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("bank.%s", uuid.toString()), "tickets", amount);
        }
    }

    public void crownsAdded(UUID uuid, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("bank.%s", uuid.toString()), "crowns", amount);
        }
    }

    public ChatColor getPlusColour(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "plusColour")) {
                return ChatColor.getByChar(connection.hget(String.format("plus.%s", player.getUniqueId()), "plusColour").charAt(0));
            } else {
                return null;
            }
        }
    }

    public ChatColor getLevelColour(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "levelColour")) {
                return ChatColor.getByChar(connection.hget(String.format("plus.%s", player.getUniqueId()), "levelColour").charAt(0));
            } else {
                return null;
            }
        }
    }

    public ChatColor getSuffixColour(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "suffixColour")) {
                return ChatColor.getByChar(connection.hget(String.format("plus.%s", player.getUniqueId()), "suffixColour").charAt(0));
            } else {
                return null;
            }
        }
    }

    public long getExpire(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "expire")) {
                return Long.parseLong(connection.hget(String.format("plus.%s", player.getUniqueId()), "expire"));
            } else {
                return -1;
            }
        }
    }

    public int getDaysSubscribed(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "daysSubscribed")) {
                return Integer.parseInt(connection.hget(String.format("plus.%s", player.getUniqueId()), "daysSubscribed"));
            } else {
                return -1;
            }
        }
    }

    public int getStreak(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "streak")) {
                return Integer.parseInt(connection.hget(String.format("plus.%s", player.getUniqueId()), "streak"));
            } else {
                return -1;
            }
        }
    }

    public int getStreak(AuroraMCPlayer player, long streakStartTimestamp) {
        int days = (int) (System.currentTimeMillis() - streakStartTimestamp) / 86400000;
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("plus.%s", player.getUniqueId()), "streak", days + "");
        }
        return days;
    }

    public int getStreak(UUID player, long streakStartTimestamp) {
        int days = (int) ((System.currentTimeMillis() - streakStartTimestamp) / 86400000);
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("plus.%s", player), "streak", days + "");
        }
        return days;
    }

    public long getStreakStartTimestamp(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getUniqueId()), "streakStart")) {
                return Long.parseLong(connection.hget(String.format("plus.%s", player.getUniqueId()), "streakStart"));
            } else {
                return -1;
            }
        }
    }

    public ChatColor getPlusColour(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "plusColour")) {
                return ChatColor.getByChar(connection.hget(String.format("plus.%s", uuid), "plusColour").charAt(0));
            } else {
                return null;
            }
        }
    }

    public ChatColor getLevelColour(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "levelColour")) {
                return ChatColor.getByChar(connection.hget(String.format("plus.%s", uuid), "levelColour").charAt(0));
            } else {
                return null;
            }
        }
    }

    public ChatColor getSuffixColour(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "suffixColour")) {
                return ChatColor.getByChar(connection.hget(String.format("plus.%s", uuid), "suffixColour").charAt(0));
            } else {
                return null;
            }
        }
    }

    public long getExpire(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "expire")) {
                return Long.parseLong(connection.hget(String.format("plus.%s", uuid), "expire"));
            } else {
                return -1;
            }
        }
    }

    public int getDaysSubscribed(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "daysSubscribed")) {
                return Integer.parseInt(connection.hget(String.format("plus.%s", uuid), "daysSubscribed"));
            } else {
                return -1;
            }
        }
    }

    public int getStreak(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "streak")) {
                return Integer.parseInt(connection.hget(String.format("plus.%s", uuid), "streak"));
            } else {
                return -1;
            }
        }
    }

    public long getStreakStartTimestamp(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", uuid), "streakStart")) {
                return Long.parseLong(connection.hget(String.format("plus.%s", uuid), "streakStart"));
            } else {
                return -1;
            }
        }
    }

    public FriendsList getFriendsList(AuroraMCPlayer player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT friends.amc_id, friends.friend, friends.type, auroramc_players.uuid, auroramc_players.name FROM friends INNER JOIN auroramc_players ON auroramc_players.id=friends.friend WHERE amc_id = ?");
            statement.setInt(1, player.getId());

            ResultSet set = statement.executeQuery();
            Map<UUID, Friend> friends = new HashMap<>();
            Map<UUID, Friend> pendingFriendRequests = new HashMap<>();
            FriendsList.VisibilityMode visibilityMode;
            FriendStatus status = (FriendStatus) AuroraMCAPI.getCosmetics().get(100);

            while (set.next()) {
                Friend friend = new Friend(null, set.getInt(2), UUID.fromString(set.getString(4)), set.getString(5), Friend.FriendType.valueOf(set.getString(3)), null);
                switch (friend.getType()) {
                    case NORMAL:
                    case FAVOURITE:
                        friends.put(friend.getUuid(), friend);
                        break;
                    case PENDING_INCOMING:
                    case PENDING_OUTGOING:
                        pendingFriendRequests.put(friend.getUuid(), friend);
                        break;
                }
            }

            try (Jedis redisConnection = jedis.getResource()) {
                visibilityMode = FriendsList.VisibilityMode.valueOf(redisConnection.hget(String.format("friends.%s", player.getUniqueId()), "visibility"));
                try {
                    status = (FriendStatus) AuroraMCAPI.getCosmetics().get(Integer.parseInt(redisConnection.hget(String.format("friends.%s", player.getUniqueId()), "status")));
                } catch (IllegalArgumentException ignored) {}
            }

            return new FriendsList(player, friends, pendingFriendRequests, visibilityMode, status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FriendsList getFriendsList(int amcId, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT friends.amc_id, friends.friend, friends.type, auroramc_players.uuid, auroramc_players.name FROM friends INNER JOIN auroramc_players ON auroramc_players.id=friends.friend WHERE amc_id = ?");
            statement.setInt(1, amcId);

            ResultSet set = statement.executeQuery();
            Map<UUID, Friend> friends = new HashMap<>();
            Map<UUID, Friend> pendingFriendRequests = new HashMap<>();
            FriendsList.VisibilityMode visibilityMode;
            FriendStatus status = (FriendStatus) AuroraMCAPI.getCosmetics().get(100);

            while (set.next()) {
                Friend friend = new Friend(null, set.getInt(2), UUID.fromString(set.getString(4)), set.getString(5), Friend.FriendType.valueOf(set.getString(3)), null);
                switch (friend.getType()) {
                    case NORMAL:
                    case FAVOURITE:
                        friends.put(friend.getUuid(), friend);
                        break;
                    case PENDING_INCOMING:
                    case PENDING_OUTGOING:
                        pendingFriendRequests.put(friend.getUuid(), friend);
                        break;
                }
            }

            try (Jedis redisConnection = jedis.getResource()) {
                visibilityMode = FriendsList.VisibilityMode.valueOf(redisConnection.hget(String.format("friends.%s", uuid.toString()), "visibility"));
                try {
                    status = (FriendStatus) AuroraMCAPI.getCosmetics().get(Integer.parseInt(redisConnection.hget(String.format("friends.%s", uuid), "status")));
                } catch (Exception e) {
                    status = (FriendStatus) AuroraMCAPI.getCosmetics().get(100);
                }
            }

            return new FriendsList(null, friends, pendingFriendRequests, visibilityMode, status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ChatChannel getChannel(AuroraMCPlayer player) {
        try (Jedis redisConnection = jedis.getResource()) {
            return ChatChannel.valueOf(redisConnection.hget(String.format("prefs.%s", player.getUniqueId()), "channel"));
        }
    }

    public PlayerPreferences getPlayerPreferences(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            boolean friendRequests, partyRequests, chatVisibility, pingOnPrivateMessage, pingOnPartyChat, hubVisibility, hubSpeed, hubFlight, reportNotifications, hubInvisibility, ignoreHubKnockback, socialMediaNotifications, staffLoginNotifications, approvalNotifications, approvalProcessedNotifications, hubForcefield, hideDisguiseName, pingOnChatMention;
            PlayerPreferences.MuteInformMode muteInformMode = PlayerPreferences.MuteInformMode.valueOf(connection.hget(String.format("prefs.%s", player.getUniqueId()), "muteInformMode"));
            PlayerPreferences.PrivateMessageMode privateMessageMode = PlayerPreferences.PrivateMessageMode.valueOf(connection.hget(String.format("prefs.%s", player.getUniqueId()), "privateMessageMode"));
            Pronoun preferredPronouns = Pronoun.valueOf(connection.hget(String.format("prefs.%s", player.getUniqueId()), "preferredPronouns"));

            friendRequests = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "friendRequests"));
            partyRequests = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "partyRequests"));
            chatVisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "chatVisibility"));
            pingOnPrivateMessage = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "pingOnPrivateMessage"));
            pingOnPartyChat = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "pingOnPartyChat"));
            hubVisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "hubVisibility"));
            hubSpeed = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "hubSpeed"));
            hubFlight = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "hubFlight"));
            reportNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "reportNotifications"));
            hubInvisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "hubInvisibility"));
            ignoreHubKnockback = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "ignoreHubKnockback"));
            socialMediaNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "socialMediaNotifications"));
            staffLoginNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "staffLoginNotifications"));
            approvalNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "approvalNotifications"));
            approvalProcessedNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "approvalProcessedNotifications"));
            hubForcefield = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "hubForcefield"));
            hideDisguiseName = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "hideDisguiseName"));
            pingOnChatMention = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getUniqueId()), "pingOnChatMention"));

            return new PlayerPreferences(player, friendRequests, partyRequests, muteInformMode, chatVisibility, privateMessageMode, pingOnPrivateMessage, pingOnPartyChat, hubVisibility, hubSpeed, hubFlight, reportNotifications, hubInvisibility, ignoreHubKnockback, socialMediaNotifications, staffLoginNotifications, approvalNotifications, approvalProcessedNotifications, hubForcefield, hideDisguiseName, pingOnChatMention, preferredPronouns);
        }
    }

    public boolean isHideDisguiseName(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            return Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hideDisguiseName"));
        }
    }

    public boolean reportDump(UUID uuid, JSONObject json, long timestamp) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO chatlogs VALUES (?, ?, ?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, AuroraMCAPI.getInfo().getName());
            statement.setLong(3, timestamp);
            statement.setString(4, json.toString());

            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getChatLog(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT chatlog FROM chatlogs WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new JSONObject(set.getString(1));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateChatlog(UUID uuid, JSONObject json) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE chatlogs SET chatlog = ? WHERE uuid = ?");
            statement.setString(2, uuid.toString());
            statement.setString(1, json.toString());

            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void attachChatLog(int id, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE reports SET chatlog_uuid = ? WHERE id = ?");
            statement.setInt(2, id);
            statement.setString(1, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerReport assignReport(int handler, PlayerReport.QueueType queue, PlayerReport.ReportType type) {
        if (type == null) {
            return assignReport(handler, queue);
        }
        try (Connection connection = mysql.getConnection()) {
            CallableStatement statement = connection.prepareCall("{CALL handle_report(?,?,?)}");
            statement.setInt(1, handler);
            statement.setString(2, queue.name());
            statement.setString(3, type.name());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                PreparedStatement statement2 = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement2.setInt(1, set.getInt(13));
                ResultSet nameSet = statement2.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);
                return new PlayerReport(set.getInt(1), set.getInt(13), name, new ArrayList<>(Arrays.stream(set.getString(2).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(3), PlayerReport.ReportType.valueOf(set.getString(4)), ((set.getString(5) == null)?null: PlayerReport.ChatType.valueOf(set.getString(5))), PlayerReport.ReportReason.valueOf(set.getString(6)), set.getInt(7), set.getString(12), PlayerReport.ReportOutcome.valueOf(set.getString(8)), ((set.getString(10) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(10))), (PlayerReport.QueueType.valueOf(set.getString(11))), ((set.getString(9) == null)?null:UUID.fromString(set.getString(9))));
            } else {
                return null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PlayerReport assignReport(int handler, PlayerReport.QueueType queue) {
        try (Connection connection = mysql.getConnection()) {
            CallableStatement statement = connection.prepareCall("{CALL handle_any_report(?,?)}");
            statement.setInt(1, handler);
            statement.setString(2, queue.name());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                PreparedStatement statement2 = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement2.setInt(1, set.getInt(13));
                ResultSet nameSet = statement2.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);
                return new PlayerReport(set.getInt(1), set.getInt(13), name, new ArrayList<>(Arrays.stream(set.getString(2).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(3), PlayerReport.ReportType.valueOf(set.getString(4)), ((set.getString(5) == null)?null: PlayerReport.ChatType.valueOf(set.getString(5))), PlayerReport.ReportReason.valueOf(set.getString(6)), set.getInt(7), set.getString(12), PlayerReport.ReportOutcome.valueOf(set.getString(8)), ((set.getString(10) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(10))), (PlayerReport.QueueType.valueOf(set.getString(11))), ((set.getString(9) == null)?null:UUID.fromString(set.getString(9))));
            } else {
                return null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void handleReport(int report, PlayerReport.ReportOutcome outcome, PlayerReport.ReportReason acceptedReason) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE reports SET outcome = ?, reason_accepted = ? WHERE id = ?");
            statement.setInt(3, report);
            statement.setString(1, outcome.name());
            if (acceptedReason == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, acceptedReason.name());
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void forwardReportToLeadership(int report) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE reports SET handler = NULL, queue = 'LEADERSHIP' WHERE id = ?");
            statement.setInt(1, report);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void abortReport(int report) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE reports SET handler = NULL WHERE id = ?");
            statement.setInt(1, report);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerReport getActiveReport(int handler) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT reports.id, reports.reporters, reports.timestamp, reports.type, reports.chat_type, reports.reason, reports.handler, reports.outcome, reports.chatlog_uuid, reports.reason_accepted, reports.queue, auroramc_players.name, reports.suspect FROM reports INNER JOIN auroramc_players ON auroramc_players.id=reports.handler WHERE reports.handler = ? AND reports.outcome = 'PENDING'");
            statement.setInt(1, handler);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(13));

                ResultSet nameSet = statement.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);
                return new PlayerReport(set.getInt(1), set.getInt(13), name, new ArrayList<>(Arrays.stream(set.getString(2).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(3), PlayerReport.ReportType.valueOf(set.getString(4)), ((set.getString(5) == null)?null: PlayerReport.ChatType.valueOf(set.getString(5))), PlayerReport.ReportReason.valueOf(set.getString(6)), set.getInt(7), set.getString(12), PlayerReport.ReportOutcome.valueOf(set.getString(8)), ((set.getString(10) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(10))), (PlayerReport.QueueType.valueOf(set.getString(11))), ((set.getString(9) == null)?null:UUID.fromString(set.getString(9))));
            } else {
                return null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PlayerReport getRecentReport(int suspect, PlayerReport.ReportType type, PlayerReport.ReportReason reason) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports WHERE suspect = ? AND timestamp > ? AND outcome = 'PENDING' AND type = ? AND reason = ?");
            statement.setInt(1, suspect);
            statement.setLong(2, (System.currentTimeMillis() - 60000));
            statement.setString(3, type.name());
            statement.setString(4, reason.name());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(2));

                ResultSet nameSet = statement.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);
                return new PlayerReport(set.getInt(1), set.getInt(2), name, new ArrayList<>(Arrays.stream(set.getString(3).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(4), PlayerReport.ReportType.valueOf(set.getString(5)), ((set.getString(6) == null)?null: PlayerReport.ChatType.valueOf(set.getString(6))), PlayerReport.ReportReason.valueOf(set.getString(7)), set.getInt(8), null, PlayerReport.ReportOutcome.valueOf(set.getString(9)), ((set.getString(11) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(11))), (PlayerReport.QueueType.valueOf(set.getString(12))), ((set.getString(10) == null)?null:UUID.fromString(set.getString(10))));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PlayerReport getActiveReport(int suspect, PlayerReport.ReportType type) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports WHERE suspect = ? AND outcome = 'PENDING' AND type = ?");
            statement.setInt(1, suspect);
            statement.setString(2, type.name());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(2));

                ResultSet nameSet = statement.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);
                return new PlayerReport(set.getInt(1), set.getInt(2), name, new ArrayList<>(Arrays.stream(set.getString(3).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(4), PlayerReport.ReportType.valueOf(set.getString(5)), ((set.getString(6) == null)?null: PlayerReport.ChatType.valueOf(set.getString(6))), PlayerReport.ReportReason.valueOf(set.getString(7)), set.getInt(8), null, PlayerReport.ReportOutcome.valueOf(set.getString(9)), ((set.getString(11) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(11))), (PlayerReport.QueueType.valueOf(set.getString(12))), ((set.getString(10) == null)?null:UUID.fromString(set.getString(10))));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addReporter(int report, int reporter) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE reports SET reporters = concat(reporters, ',',?)  WHERE id = ?");
            statement.setString(1, reporter + "");
            statement.setInt(2, report);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int newReport(int suspect, int reporter, long timestamp, PlayerReport.ReportType type, PlayerReport.ChatType chatType, PlayerReport.ReportReason reason, UUID chatlog, PlayerReport.QueueType queue) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO reports(suspect, reporters, timestamp, type, chat_type, reason, handler, outcome, chatlog_uuid, reason_accepted, queue) VALUES (?,?,?,?,?,?,NULL,'PENDING',?,NULL,?)");
            statement.setInt(1, suspect);
            statement.setInt(2, reporter);
            statement.setLong(3, timestamp);
            statement.setString(4, type.name());
            if (chatType == null) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, chatType.name());
            }
            statement.setString(6, reason.name());
            if (chatlog == null) {
                statement.setNull(7, Types.VARCHAR);
            } else {
                statement.setString(7, chatlog.toString());
            }
            statement.setString(8, queue.name());

            statement.execute();
            statement = connection.prepareStatement("SELECT id FROM reports WHERE suspect = ? AND reporters = ? AND timestamp = ? AND type = ?");
            statement.setInt(1, suspect);
            statement.setInt(2, reporter);
            statement.setLong(3, timestamp);
            statement.setString(4, type.name());

            ResultSet set = statement.executeQuery();
            set.next();
            return set.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addUsernameBan(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.sadd("usernamebans", name.toLowerCase());
        }
    }

    public void removeUsernameBan(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.srem("usernamebans", name.toLowerCase());
        }
    }

    public boolean isUsernameBanned(String s) {
        try (Jedis connection = jedis.getResource()) {
            return connection.sismember("usernamebans", s.toLowerCase());
        }
    }

    public List<String> getUsernameBans() {
        try (Jedis connection = jedis.getResource()) {
            Set<String> usernames = connection.smembers("usernamebans");
            return new ArrayList<>(usernames);
        }
    }

    public void addSMPBlacklist(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.sadd("smp.blacklist", name.toLowerCase());
        }
    }

    public void removeSMPBlacklist(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.srem("smp.blacklist", name.toLowerCase());
        }
    }

    public boolean isSMPBlacklist(String s) {
        try (Jedis connection = jedis.getResource()) {
            return connection.sismember("smp.blacklist", s.toLowerCase());
        }
    }

    public List<String> getSMPBlacklist() {
        try (Jedis connection = jedis.getResource()) {
            Set<String> usernames = connection.smembers("smp.blacklist");
            return new ArrayList<>(usernames);
        }
    }

    public List<Integer> getGlobalAccountIDs(String code) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT banned_profiles FROM global_account_suspensions WHERE punishment_id = ?");
            statement.setString(1, code);
            ResultSet set = statement.executeQuery();
            set.next();

            List<Integer> parsedIds = new ArrayList<>();
            for (String profileId : set.getString(1).split(",")) {
                statement = connection.prepareStatement("SELECT amc_id FROM ip_logs WHERE profile_id = ?");
                statement.setInt(1, Integer.parseInt(profileId));
                set = statement.executeQuery();
                while (set.next()) {
                    parsedIds.add(set.getInt(1));
                }
            }
            return parsedIds;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GlobalAccountSuspension getGlobalAccountSuspension(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM auroramc_players WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement = connection.prepareStatement("SELECT global_account_suspensions.punishment_id,auroramc_players.name,global_account_suspensions.issuer, global_account_suspensions.banned_profiles, global_account_suspensions.timestamp, global_account_suspensions.reason FROM global_account_suspensions INNER JOIN auroramc_players ON auroramc_players.id=global_account_suspensions.root_account WHERE (root_account = ?)");
                statement.setInt(1, set.getInt(1));
                set = statement.executeQuery();
                if (set.next()) {
                    return new GlobalAccountSuspension(set.getString(1), set.getString(2), set.getString(6), new ArrayList<>(Arrays.asList(set.getString(4).split(","))), set.getInt(3), Long.parseLong(set.getString(5)));
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void checkIP(String unencryptedip, UUID uuid, String rootBan, int rootId) {
        try (Connection connection = mysql.getConnection()) {
            String ip = hashIP(unencryptedip);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ip_profile WHERE ip = ?");
            statement.setString(1, ip);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                //This IP profile exists, check if the IP profile is already there.
                int profile = set.getInt(1);
                //This can return more than 1 value, so check if they absolutely have the right suspension.
                statement = connection.prepareStatement("SELECT * FROM global_account_suspensions WHERE punishment_id = ?");
                statement.setString(1, rootBan);
                set = statement.executeQuery();
                set.next();
                List<String> profiles = new ArrayList<>(Arrays.asList(set.getString(4).split(",")));
                if (profiles.contains(profile + "")) {
                    //This is already in the list of IP profiles for this ban, ignore it.
                    return;
                }

                //The list of profiles does not contain this profile. Add it. (it also means they have never connected through it before).
                statement = connection.prepareStatement("UPDATE global_account_suspensions SET banned_profiles = CONCAT(banned_profiles, ?) WHERE punishment_id = ?");
                statement.setString(1, profile + "");
                statement.setString(2, rootBan);
                statement.execute();

                statement = connection.prepareStatement("UPDATE auroramc_players SET last_used_profile = ? WHERE id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                statement.setString(1, profile + "");
                statement.setString(2, uuid.toString());
                statement.execute();

                statement = connection.prepareStatement("UPDATE auroramc_players SET last_used_profile = ? WHERE id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                statement.setString(1, profile + "");
                statement.setString(2, uuid.toString());
                statement.execute();

                statement = connection.prepareStatement("SELECT * FROM ip_logs WHERE profile_id = ? AND amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                statement.setString(1, String.valueOf(profile));
                statement.setString(2, uuid.toString());
                ResultSet set2 = statement.executeQuery();
                if (set2.next()) {
                    statement = connection.prepareStatement("UPDATE ip_logs SET last_used = CURRENT_TIMESTAMP , total_usages = total_usages + 1 WHERE profile_id = ? AND amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                    statement.execute();
                } else {
                    statement = connection.prepareStatement("INSERT INTO ip_logs values ((SELECT id FROM auroramc_players WHERE uuid = ?), ?, CURRENT_TIMESTAMP, 1)");
                    statement.setString(1, uuid.toString());
                    statement.setInt(2, profile);
                    statement.execute();
                }

            } else {
                //This is an IP that has no profile, create a new one.
                statement = connection.prepareStatement("INSERT INTO ip_profile(ip, ids, last_used_by, last_used_at) VALUES (?,?,(SELECT id FROM auroramc_players WHERE uuid = ?),CURRENT_TIMESTAMP)");
                statement.setString(1, ip);
                statement.setString(2, rootId + "");
                statement.setString(3, rootId + "");
                statement.execute();

                statement = connection.prepareStatement("SELECT * FROM ip_profile WHERE ip = ?");
                statement.setString(1, ip);
                set = statement.executeQuery();
                set.next();

                statement = connection.prepareStatement("UPDATE global_account_suspensions SET banned_profiles = CONCAT(banned_profiles, ?) WHERE punishment_id = ?");
                statement.setString(1, set.getInt(1) + "");
                statement.setString(2, rootBan);
                statement.execute();

                statement = connection.prepareStatement("UPDATE auroramc_players SET last_used_profile = ? WHERE id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                statement.setString(1, set.getInt(1) + "");
                statement.setString(2, uuid.toString());
                statement.execute();


                statement = connection.prepareStatement("SELECT * FROM ip_logs WHERE profile_id = ? AND amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                statement.setString(1, set.getInt(1) + "");
                statement.setString(2, uuid.toString());
                ResultSet set2 = statement.executeQuery();
                if (set2.next()) {
                    statement = connection.prepareStatement("UPDATE ip_logs SET last_used = CURRENT_TIMESTAMP , total_usages = total_usages + 1 WHERE profile_id = ? AND amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?)");
                    statement.execute();
                } else {
                    statement = connection.prepareStatement("INSERT INTO ip_logs values ((SELECT id FROM auroramc_players WHERE uuid = ?), ?, CURRENT_TIMESTAMP, 1)");
                    statement.setString(1, uuid.toString());
                    statement.setInt(2, set.getInt(1));
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlreadyGlobalBanned(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM punishments WHERE amc_id = ? AND status = 1 AND rule_id = 22");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public GlobalAccountSuspension getGlobalAccountSuspension(String unencryptedip) {
        try (Connection connection = mysql.getConnection()) {
            String ip = hashIP(unencryptedip);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ip_profile WHERE ip = ?");
            statement.setString(1, ip);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                int profile = set.getInt(1);
                //This can return more than 1 value, so check if they absolutely have the right suspension.
                statement = connection.prepareStatement("SELECT global_account_suspensions.punishment_id,auroramc_players.name,global_account_suspensions.issuer, global_account_suspensions.banned_profiles, global_account_suspensions.timestamp, global_account_suspensions.reason FROM global_account_suspensions INNER JOIN auroramc_players ON auroramc_players.id=global_account_suspensions.root_account WHERE global_account_suspensions.banned_profiles REGEXP CONCAT('^', ?,'$|^', ?,',.*$|^.*,', ?,'$|^.*,', ?,',.*$')");
                statement.setString(1, profile + "");
                statement.setString(2, profile + "");
                statement.setString(3, profile + "");
                statement.setString(4, profile + "");
                set = statement.executeQuery();
                while (set.next()) {
                    List<String> profiles = new ArrayList<>(Arrays.asList(set.getString(4).split(",")));
                    if (profiles.contains(profile + "")) {
                        //This is the correct one.
                        return new GlobalAccountSuspension(set.getString(1), set.getString(2), set.getString(6), new ArrayList<>(Arrays.asList(set.getString(4).split(","))), set.getInt(3), Long.parseLong(set.getString(5)));
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public int getOfflineReports(int id) {
        try (Jedis connection = jedis.getResource()) {
            String amount = connection.hget("reports.offline", id + "");
            if (amount != null) {
                int total = Integer.parseInt(amount);
                if (total > 0) {
                    connection.hset("reports.offline", id + "", "0");
                    return total;
                }
            }
        }
        return 0;
    }

    public void addOfflinereport(int id) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy("reports.offline", id + "", 1);
        }
    }

    public List<PlayerReport> getSubmittedReports(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT reports.id, reports.reporters, reports.timestamp, reports.type, reports.chat_type, reports.reason, reports.handler, reports.outcome, reports.chatlog_uuid, reports.reason_accepted, reports.queue, reports.suspect FROM reports WHERE (reports.reporters REGEXP CONCAT('^', ?,'$|^', ?,',.*$|^.*,', ?,'$|^.*,', ?,',.*$')) ORDER BY timestamp DESC LIMIT 56");
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.setInt(4, id);

            ResultSet set = statement.executeQuery();
            List<PlayerReport> reports = new ArrayList<>();
            while (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(12));

                ResultSet nameSet = statement.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);
                reports.add(new PlayerReport(set.getInt(1), set.getInt(12), name, new ArrayList<>(Arrays.stream(set.getString(2).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(3), PlayerReport.ReportType.valueOf(set.getString(4)), ((set.getString(5) == null)?null: PlayerReport.ChatType.valueOf(set.getString(5))), PlayerReport.ReportReason.valueOf(set.getString(6)), set.getInt(7), null, PlayerReport.ReportOutcome.valueOf(set.getString(8)), ((set.getString(10) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(10))), (PlayerReport.QueueType.valueOf(set.getString(11))), ((set.getString(9) == null)?null:UUID.fromString(set.getString(9)))));
            }
            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PlayerReport getReport(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT reports.id, reports.reporters, reports.timestamp, reports.type, reports.chat_type, reports.reason, reports.handler, reports.outcome, reports.chatlog_uuid, reports.reason_accepted, reports.queue, reports.suspect FROM reports WHERE id = ? ORDER BY timestamp DESC LIMIT 56");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(12));

                ResultSet nameSet = statement.executeQuery();
                nameSet.next();
                String name = nameSet.getString(1);

                statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                statement.setInt(1, set.getInt(7));

                ResultSet name2Set = statement.executeQuery();
                String name2 = null;
                if (name2Set.next()) {
                    name2 = name2Set.getString(1);
                }
                return new PlayerReport(set.getInt(1), set.getInt(12), name, new ArrayList<>(Arrays.stream(set.getString(2).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())), set.getLong(3), PlayerReport.ReportType.valueOf(set.getString(4)), ((set.getString(5) == null)?null: PlayerReport.ChatType.valueOf(set.getString(5))), PlayerReport.ReportReason.valueOf(set.getString(6)), set.getInt(7), name2, PlayerReport.ReportOutcome.valueOf(set.getString(8)), ((set.getString(10) == null)?null: PlayerReport.ReportReason.valueOf(set.getString(10))), (PlayerReport.QueueType.valueOf(set.getString(11))), ((set.getString(9) == null)?null:UUID.fromString(set.getString(9))));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasActiveSession(int player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM sessions WHERE record_after IS NULL AND timestamp + INTERVAL 1 DAY > now() AND amc_id = ?");
            statement.setInt(1, player);

            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasActiveSession(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            return connection.exists(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid)) && connection.exists(String.format("proxy.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid));
        }
    }

    public List<IgnoredPlayer> getIgnoredPlayers(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ignored WHERE amc_id = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                List<IgnoredPlayer> ignoredPlayers = new ArrayList<>();
                String[] idArray = set.getString(2).split(",");
                if (idArray.length == 1) {
                    if (idArray[0].equalsIgnoreCase("")) {
                        return ignoredPlayers;
                    }
                } else if (idArray.length == 0) {
                    return ignoredPlayers;
                }
                List<Integer> ids = Arrays.stream(idArray).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                for (int target : ids) {
                    ignoredPlayers.add(new IgnoredPlayer(target, getNameFromID(target)));
                }
                return ignoredPlayers;
            } else {
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void setIgnoredPlayers(int id, List<IgnoredPlayer> users) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE ignored SET users = ? WHERE amc_id = ?");
            List<String> usersStrings = users.stream().map(IgnoredPlayer::toString).collect(Collectors.toList());
            statement.setInt(2, id);
            statement.setString(1, String.join(",", usersStrings));

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cosmetic> getUnlockedCosmetics(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            List<Cosmetic> cosmetics = new ArrayList<>();
            Set<String> cosmeticsStrings = connection.smembers(String.format("cosmetics.unlocked.%s", uuid.toString()));
            for (String s : cosmeticsStrings) {
                int i = Integer.parseInt(s);
                if (AuroraMCAPI.getCosmetics().get(i) == null) {
                    AuroraMCAPI.getLogger().info("Cosmetic with ID " + i + " does not exist.");
                    continue;
                }
                cosmetics.add(AuroraMCAPI.getCosmetics().get(i));
            }
            return cosmetics;
        }
    }

    public boolean hasUnlockedCosmetic(UUID uuid, int id) {
        try (Jedis connection = jedis.getResource()) {
            return connection.sismember(String.format("cosmetics.unlocked.%s", uuid.toString()), id + "");
        }
    }

    public void addCosmetic(UUID uuid, Cosmetic cosmetic) {
        try (Jedis connection = jedis.getResource()) {
            connection.sadd(String.format("cosmetics.unlocked.%s", uuid.toString()), cosmetic.getId() + "");
        }
    }

    public void removeCosmetic(UUID uuid, Cosmetic cosmetic) {
        try (Jedis connection = jedis.getResource()) {
            connection.srem(String.format("cosmetics.unlocked.%s", uuid.toString()), cosmetic.getId() + "");
        }
    }

    public HashMap<Cosmetic.CosmeticType, Cosmetic> getActiveCosmetics(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            HashMap<Cosmetic.CosmeticType, Cosmetic> activeCosmetics = new HashMap<>();
            for (Cosmetic.CosmeticType type : Cosmetic.CosmeticType.values()) {
                if (connection.hexists(String.format("cosmetics.active.%s", uuid.toString()), type.name())) {
                    String id = connection.hget(String.format("cosmetics.active.%s", uuid.toString()), type.name());
                    int i = Integer.parseInt(id);
                    activeCosmetics.put(type, AuroraMCAPI.getCosmetics().get(i));
                }
            }
            return activeCosmetics;
        }
    }

    public List<Payment> getPayments(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM store_payments WHERE amc_id = ? ORDER BY date_processed DESC");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            List<Payment> payments = new ArrayList<>();
            while (set.next()) {
                String[] packagesStrings = set.getString(6).split(",");
                List<Payment.Package> packages = new ArrayList<>();
                for (String packageString : packagesStrings) {
                    packages.add(Payment.Package.getByID(Integer.parseInt(packageString)));
                }
                List<UUID> uuids = new ArrayList<>();
                if (!set.getString(7).equals("")) {
                    String[] uuidStrings = set.getString(7).split(",");
                    for (String uuidString : uuidStrings) {
                        uuids.add(UUID.fromString(uuidString));
                    }
                }
                payments.add(new Payment(set.getInt(1), set.getInt(2), set.getString(3), set.getDouble(4), set.getTimestamp(5).getTime(), packages, uuids, Payment.PaymentStatus.valueOf(set.getString(8))));
            }
            return payments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void equipCosmetic(UUID uuid, Cosmetic cosmetic) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("cosmetics.active.%s", uuid.toString()), cosmetic.getType().name(), cosmetic.getId() + "");
        }
    }

    public void unequipCosmetic(UUID uuid, Cosmetic cosmetic) {
        try (Jedis connection = jedis.getResource()) {
            connection.hdel(String.format("cosmetics.active.%s", uuid.toString()), cosmetic.getType().name());
        }
    }

    public UUID getUUID(String name) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM auroramc_players WHERE name = ?");
            statement.setString(1, name);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return UUID.fromString(set.getString(1));
            } else {
                //NEW USER
                return null;
            }

        } catch (SQLException e) {
            return null;
        }
    }

    public UUID getUUID(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM auroramc_players WHERE id = ?");
            statement.setInt(1, id);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return UUID.fromString(set.getString(1));
            } else {
                //NEW USER
                return null;
            }

        } catch (SQLException e) {
            return null;
        }
    }

    public String getRandomSkin() {
        try (Jedis connection = jedis.getResource()) {
            return connection.srandmember("randomskins");
        }
    }

    public void pushSkin(Skin skin) {
        try (Jedis connection = jedis.getResource()) {
            connection.sadd("randomskins", skin.getValue() + ";" + skin.getSignature());
        }
    }

    public void addLobbyTime(UUID uuid, long ms) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", uuid.toString()), "lobbyTimeMs", ms);
        }
    }

    public void addGameTime(UUID uuid, long ms) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", uuid.toString()), "gameTimeMs", ms);
        }
    }

    public PlayerKitLevel getKitLevel(int player, int gameId, int kitId) {
        try (Jedis connection = jedis.getResource()) {
            String level = connection.hget("kitlevel." + player, gameId + "." + kitId);
            if (level != null) {
                String[] split = level.split(";");
                return new PlayerKitLevel(player, gameId, kitId, Integer.parseInt(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2]), Short.parseShort(split[3]));
            } else {
                return new PlayerKitLevel(player, gameId, kitId, 0, 0L, 0L, (short)0);
            }
        }
    }

    public List<Crate> getCrates(int amcId) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM crates WHERE amc_id = ?");
            statement.setInt(1, amcId);

            ResultSet set = statement.executeQuery();

            List<Crate> crates = new ArrayList<>();
            while (set.next()) {
                Crate.CrateReward reward = null;
                if (set.getString(4) != null) {
                    String[] args = set.getString(4).split(":");
                    switch (args[0]) {
                        case "cosmetic": {
                            reward = new Crate.CrateReward(AuroraMCAPI.getCosmetics().get(Integer.parseInt(args[1])), null, 0);
                            break;
                        }
                        case "rank": {
                            reward = new Crate.CrateReward(null, Rank.getByID(Integer.parseInt(args[1])), 0);
                            break;
                        }
                        case "plus": {
                            reward = new Crate.CrateReward(null, null, Integer.parseInt(args[1]));
                            break;
                        }
                    }
                }
                switch (set.getString(2)) {
                    case "IRON": {
                        crates.add(CrateFactory.generateIronCrate(UUID.fromString(set.getString(1)), amcId, reward, set.getTimestamp(5).getTime(), ((set.getTimestamp(6) == null)?-1:set.getTimestamp(6).getTime())));
                        break;
                    }
                    case "GOLD": {
                        crates.add(CrateFactory.generateGoldCrate(UUID.fromString(set.getString(1)), amcId, reward, set.getTimestamp(5).getTime(), ((set.getTimestamp(6) == null)?-1:set.getTimestamp(6).getTime())));
                        break;
                    }
                    case "DIAMOND": {
                        crates.add(CrateFactory.generateDiamondCrate(UUID.fromString(set.getString(1)), amcId, reward, set.getTimestamp(5).getTime(), ((set.getTimestamp(6) == null)?-1:set.getTimestamp(6).getTime())));
                        break;
                    }
                    case "EMERALD": {
                        crates.add(CrateFactory.generateEmeraldCrate(UUID.fromString(set.getString(1)), amcId, reward, set.getTimestamp(5).getTime(), ((set.getTimestamp(6) == null)?-1:set.getTimestamp(6).getTime())));
                        break;
                    }
                }
            }
            return crates;
        } catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Crate getCrate(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM crates WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                Crate.CrateReward reward = null;
                if (set.getString(4) != null) {
                    String[] args = set.getString(4).split(":");
                    switch (args[0]) {
                        case "cosmetic": {
                            reward = new Crate.CrateReward(AuroraMCAPI.getCosmetics().get(Integer.parseInt(args[1])), null, 0);
                            break;
                        }
                        case "rank": {
                            reward = new Crate.CrateReward(null, Rank.getByID(Integer.parseInt(args[1])), 0);
                            break;
                        }
                        case "plus": {
                            reward = new Crate.CrateReward(null, null, Integer.parseInt(args[1]));
                            break;
                        }
                    }
                }
                switch (set.getString(2)) {
                    case "IRON": {
                        return CrateFactory.generateIronCrate(UUID.fromString(set.getString(1)), set.getInt(3), reward, set.getTimestamp(5).getTime(), set.getTimestamp(6).getTime());
                    }
                    case "GOLD": {
                        return CrateFactory.generateGoldCrate(UUID.fromString(set.getString(1)), set.getInt(3), reward, set.getTimestamp(5).getTime(), set.getTimestamp(6).getTime());
                    }
                    case "DIAMOND": {
                        return CrateFactory.generateDiamondCrate(UUID.fromString(set.getString(1)), set.getInt(3), reward, set.getTimestamp(5).getTime(), set.getTimestamp(6).getTime());
                    }
                    case "EMERALD": {
                        return CrateFactory.generateEmeraldCrate(UUID.fromString(set.getString(1)), set.getInt(3), reward, set.getTimestamp(5).getTime(), set.getTimestamp(6).getTime());
                    }
                }
            }
            return null;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void setOpened(Crate crate) {
        try (Connection connection = AuroraMCAPI.getDbManager().getMySQLConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE crates SET opened = ?, reward = ? WHERE uuid = ? AND amc_id = ?");
            statement.setTimestamp(1, new Timestamp(crate.getOpened()));
            statement.setString(2, crate.getLoot().toString());
            statement.setString(3, crate.getUuid().toString());
            statement.setInt(4, crate.getOwner());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<GameLog> getGames(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game_session WHERE (game_session.players REGEXP CONCAT('^', ?,'$|^', ?,',.*$|^.*,', ?,'$|^.*,', ?,',.*$')) ORDER BY timestamp DESC LIMIT 28");
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.setInt(4, id);

            ResultSet set = statement.executeQuery();
            List<GameLog> gameLogs = new ArrayList<>();
            while (set.next()) {
                gameLogs.add(new GameLog(UUID.fromString(set.getString(1)), set.getTimestamp(2).getTime(), set.getString(3), set.getString(4), new JSONObject(set.getString(5))));
            }
            return gameLogs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void achievementGet(AuroraMCPlayer player, Achievement achievement, int tier) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("stats.%s.achievements", player.getUniqueId().toString()), achievement.getAchievementId() + "", tier + "");
        }
    }

    public void achievementProgress(AuroraMCPlayer player, Achievement achievement, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.achievements.progress", player.getUniqueId().toString()), achievement.getAchievementId() + "", amount);
        }
    }

    public void statisticIncrement(AuroraMCPlayer player, int gameId, String key, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.game", player.getUniqueId().toString()), gameId + "." + key, amount);
        }
    }

    public void xpAdd(AuroraMCPlayer player, long level, long xpIntoLevel, long totalXpEarned) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("stats.%s.core", player.getUniqueId().toString()), "level", level + "");
            connection.hset(String.format("stats.%s.core", player.getUniqueId().toString()), "xpIntoLevel", xpIntoLevel + "");
            connection.hset(String.format("stats.%s.core", player.getUniqueId().toString()), "xpEarned", totalXpEarned + "");
        }
    }

    public void achievementGet(UUID uuid, Achievement achievement, int tier) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("stats.%s.achievements", uuid.toString()), achievement.getAchievementId() + "", tier + "");
        }
    }

    public void achievementRemove(UUID uuid, Achievement achievement) {
        try (Jedis connection = jedis.getResource()) {
            connection.hdel(String.format("stats.%s.achievements", uuid.toString()), achievement.getAchievementId() + "");
        }
    }

    public void achievementProgress(UUID uuid, Achievement achievement, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.achievements.progress", uuid.toString()), achievement.getAchievementId() + "", amount);
        }
    }

    public void achievementSet(UUID uuid, Achievement achievement, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("stats.%s.achievements.progress", uuid.toString()), achievement.getAchievementId() + "", amount + "");
        }
    }

    public void statisticIncrement(UUID uuid, int gameId, String key, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.game", uuid.toString()), gameId + "." + key, amount);
        }
    }

    public void xpAdd(UUID uuid, long level, long xpIntoLevel, long totalXpEarned) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("stats.%s.core", uuid), "level", level + "");
            connection.hset(String.format("stats.%s.core", uuid), "xpIntoLevel", xpIntoLevel + "");
            connection.hset(String.format("stats.%s.core", uuid), "xpEarned", totalXpEarned + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gamePlayed(AuroraMCPlayer player, boolean win) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), ((win) ? "gamesWon" : "gamesLost"), 1);
            connection.hincrBy(String.format("stats.%s.core", player.getUniqueId().toString()), "gamesPlayed", 1);
        }
    }

    public void newFriendRequest(AuroraMCPlayer player, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO friends(amc_id, friend, type) VALUES (?, (SELECT id FROM auroramc_players WHERE uuid = ?), 'PENDING_OUTGOING')");
            statement.setInt(1, player.getId());
            statement.setString(2, uuid.toString());

            statement.execute();

            statement = connection.prepareStatement("INSERT INTO friends(amc_id, friend, type) VALUES ((SELECT id FROM auroramc_players WHERE uuid = ?), ?, 'PENDING_INCOMING')");
            statement.setString(1, uuid.toString());
            statement.setInt(2, player.getId());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isFavouriteFriend(int player1, int player2) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friends WHERE amc_id = ? AND friend = ? AND type = 'FAVOURITE'");
            statement.setInt(2, player1);
            statement.setInt(1, player2);

            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void friendRequestAccepted(AuroraMCPlayer player, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE friends SET type = 'NORMAL' WHERE amc_id = ? AND friend = (SELECT id FROM auroramc_players WHERE uuid = ?)");
            statement.setInt(1, player.getId());
            statement.setString(2, uuid.toString());

            statement.execute();

            statement = connection.prepareStatement("UPDATE friends SET type = 'NORMAL' WHERE amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?) AND friend = ?");
            statement.setString(1, uuid.toString());
            statement.setInt(2, player.getId());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void favouritedFriend(AuroraMCPlayer player, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE friends SET type = 'FAVOURITE' WHERE amc_id = ? AND friend = (SELECT id FROM auroramc_players WHERE uuid = ?)");
            statement.setInt(1, player.getId());
            statement.setString(2, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unfavouritedFriend(AuroraMCPlayer player, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE friends SET type = 'NORMAL' WHERE amc_id = ? AND friend = (SELECT id FROM auroramc_players WHERE uuid = ?)");
            statement.setInt(1, player.getId());
            statement.setString(2, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void friendDeleted(AuroraMCPlayer player, UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM friends WHERE amc_id = (SELECT id FROM auroramc_players WHERE uuid = ?) AND friend = ?");
            statement.setString(1, uuid.toString());
            statement.setInt(2, player.getId());

            statement.execute();

            statement = connection.prepareStatement("DELETE FROM friends WHERE friend = (SELECT id FROM auroramc_players WHERE uuid = ?) AND amc_id = ?");
            statement.setString(1, uuid.toString());
            statement.setInt(2, player.getId());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void statusSet(AuroraMCPlayer player, FriendStatus status) {
        try (Jedis redisConnection = jedis.getResource()) {
            redisConnection.hset(String.format("friends.%s", player.getUniqueId()), "status", status.getId() + "");
        }
    }

    public void visibilitySet(AuroraMCPlayer player, FriendsList.VisibilityMode visibilityMode) {
        try (Jedis redisConnection = jedis.getResource()) {
            redisConnection.hset(String.format("friends.%s", player.getUniqueId()), "visibility", visibilityMode.name());
        }
    }

    public void setApprovalNotifications(AuroraMCPlayer player, boolean approvalNotifications) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "approvalNotifications", "" + approvalNotifications);
        }
    }
    public void setPreferredPronouns(AuroraMCPlayer player, Pronoun preferredPronouns) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "preferredPronouns", "" + preferredPronouns);
        }
    }

    public void setApprovalProcessedNotifications(AuroraMCPlayer player, boolean approvalProcessedNotifications) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "approvalProcessedNotifications", "" + approvalProcessedNotifications);
        }
    }

    public void setChatVisibility(AuroraMCPlayer player, boolean chatVisibility) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "chatVisibility", "" + chatVisibility);
        }
    }

    public void setFriendRequests(AuroraMCPlayer player, boolean friendRequests) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "friendRequests", "" + friendRequests);
        }
    }

    public void setHideDisguiseName(AuroraMCPlayer player, boolean hideDisguiseName) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "hideDisguiseName", "" + hideDisguiseName);
        }
    }

    public void setHubFlight(AuroraMCPlayer player, boolean hubFlight) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "hubFlight", "" + hubFlight);
        }
    }

    public void setHubForcefield(AuroraMCPlayer player, boolean hubForcefield) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "hubForcefield", "" + hubForcefield);
        }
    }

    public void setHubInvisibility(AuroraMCPlayer player, boolean hubInvisibility) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "hubInvisibility", "" + hubInvisibility);
        }
    }

    public void setHubSpeed(AuroraMCPlayer player, boolean hubSpeed) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "hubSpeed", "" + hubSpeed);
        }
    }

    public void setHubVisibility(AuroraMCPlayer player, boolean hubVisibility) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "hubVisibility", "" + hubVisibility);
        }
    }

    public void setIgnoreHubKnockback(AuroraMCPlayer player, boolean ignoreHubKnockback) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "ignoreHubKnockback", "" + ignoreHubKnockback);
        }
    }

    public void setMuteInformMode(AuroraMCPlayer player, PlayerPreferences.MuteInformMode muteInformMode) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "muteInformMode", muteInformMode.name());
        }
    }

    public void setPartyRequests(AuroraMCPlayer player, boolean partyRequests) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "partyRequests", "" + partyRequests);
        }
    }

    public void setPingOnPartyChat(AuroraMCPlayer player, boolean pingOnPartyChat) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "pingOnPartyChat", "" + pingOnPartyChat);
        }
    }

    public void setPingOnPrivateMessage(AuroraMCPlayer player, boolean pingOnPrivateMessage) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "pingOnPrivateMessage", "" + pingOnPrivateMessage);
        }
    }

    public void setPingOnChatMention(AuroraMCPlayer player, boolean pingOnChatMention) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "pingOnChatMention", "" + pingOnChatMention);
        }
    }

    public void setPrivateMessageMode(AuroraMCPlayer player, PlayerPreferences.PrivateMessageMode privateMessageMode) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "privateMessageMode", privateMessageMode.name());
        }
    }

    public void setReportNotifications(AuroraMCPlayer player, boolean reportNotifications) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "reportNotifications", "" + reportNotifications);
        }
    }

    public void setSocialMediaNotifications(AuroraMCPlayer player, boolean socialMediaNotifications) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "socialMediaNotifications", "" + socialMediaNotifications);
        }
    }

    public void setStaffLoginNotifications(AuroraMCPlayer player, boolean staffLoginNotifications) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset(String.format("prefs.%s", player.getUniqueId()), "staffLoginNotifications", "" + staffLoginNotifications);
        }
    }

    public String fetchData(ServerInfo info) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists("serverdata." + info.getNetwork().name() + "." + info.getName())) {
                return connection.get("serverdata." + info.getNetwork() + "." + info.getName());
            } else {
                return "IDLE;0/0;N/A;N/A";
            }
        }
    }

    public int getPlayerCount() {
        try (Jedis connection = jedis.getResource()) {
            String amount = connection.get(String.format("playercount.%s", AuroraMCAPI.getInfo().getNetwork().name().toLowerCase()));
            if (amount != null) {
                return Integer.parseInt(amount);
            }
            return 0;
        }
    }

    public void jrModLeave(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.srem("online.jrmod", name);
        }
    }
    public void jrModJoin(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.sadd("online.jrmod", name);
        }
    }

    public void modLeave(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.srem("online.mod", name);
        }
    }
    public void modJoin(String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.sadd("online.mod", name);
        }
    }

    public Set<String> getModsOnline() {
        try (Jedis connection = jedis.getResource()) {
            return connection.smembers("online.mod");
        }
    }

    public Set<String> getJrModsOnline() {
        try (Jedis connection = jedis.getResource()) {
            return connection.smembers("online.jrmod");
        }
    }

    public synchronized void updateServer(UUID uuid, String server) {
        try (Jedis connection = jedis.getResource()) {
            //Insert the server they are initially on.
            connection.set(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid.toString()), server);
            //Expires after 30m to prevent values getting stuck in the database in the event that the proxy crashes.
            //Proxy has to renew this value every 15m or anytime the server changes.
            connection.expire(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid), 120);
        }
    }

    public synchronized void setProxyUUID(UUID uuid, UUID proxy) {
        try (Jedis connection = jedis.getResource()) {
            //Insert the server they are initially on.
            connection.set(String.format("proxy.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid.toString()), proxy.toString());
            //Expires after 30m to prevent values getting stuck in the database in the event that the proxy crashes.
            //Proxy has to renew this value every 15m.
            connection.expire(String.format("proxy.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid), 120);
        }
    }

    public synchronized String getCurrentServer(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            return connection.get(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid));
        }
    }


    public synchronized UUID getProxy(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            return UUID.fromString(connection.get(String.format("proxy.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), uuid)));
        }
    }

    public void sessionEnd(UUID player, int id, PlayerStatistics stats, UUID session) {
        try (Jedis connection = jedis.getResource()) {
            //delete the active server to prevent players joining from other proxies.
            connection.del(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(),player));
            connection.del(String.format("proxy.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(),player));

            try (Connection con = mysql.getConnection()) {
                PreparedStatement statement = con.prepareStatement("UPDATE sessions SET record_after = ? WHERE amc_id = ? AND session_uuid = ?");
                statement.setString(1, stats.toJSON());
                statement.setInt(2, id);
                statement.setString(3, session.toString());

                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void sessionEndDisconnected(UUID player, int id, UUID session) {
        try (Jedis connection = jedis.getResource()) {
            //delete the active server to prevent players joining from other proxies.
            connection.del(String.format("server.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), player));
            connection.del(String.format("proxy.%s.%s", AuroraMCAPI.getInfo().getNetwork().name(), player));

            try (Connection con = mysql.getConnection()) {
                PreparedStatement statement = con.prepareStatement("UPDATE sessions SET record_after = (SELECT record_before WHERE amc_id = ? AND session_uuid = ?) WHERE amc_id = ? AND session_uuid = ?");
                statement.setInt(1, id);
                statement.setString(2, session.toString());
                statement.setInt(3, id);
                statement.setString(4, session.toString());

                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void newSession(UUID uuid, UUID player, int id, long timestamp) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO sessions(amc_id, proxy_uuid, session_uuid, record_before) VALUES ((SELECT id FROM auroramc_players WHERE uuid = ?), ?,?,?)");

            String json = "{}";

            if (getAuroraMCID(player) != -1) {
                PlayerStatistics stats = getStatistics(player, id);
                json = stats.toJSON();
            }
            statement.setString(1, player.toString());
            statement.setString(2, AuroraMCAPI.getInfo().getName());
            statement.setString(3, uuid.toString());
            statement.setString(4, json);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ServerInfo> getServers() {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM servers WHERE network = ?");
            statement.setString(1, AuroraMCAPI.getInfo().getNetwork().name());
            ResultSet set = statement.executeQuery();

            ArrayList<ServerInfo> servers = new ArrayList<>();
            while (set.next()) {
                servers.add(new ServerInfo(set.getString(1), set.getString(2), set.getInt(3), ServerInfo.Network.valueOf(set.getString(4)), set.getBoolean(5), new JSONObject(set.getString(6)), set.getInt(7), set.getInt(8), set.getInt(9), set.getInt(10), set.getInt(11), set.getInt(12), set.getInt(13), set.getInt(14), set.getString(15)));
            }

            return servers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void extend(UUID uuid, int days) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("plus.%s",uuid), "daysSubscribed", days);
            long expire = (Long.parseLong(connection.hget(String.format("plus.%s", uuid), "expire")) + (((long) days) * 86400000));
            connection.hset(String.format("plus.%s", uuid), "expire", expire + "");
        }
    }

    public void newSubscription(UUID uuid, int days) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("plus.%s",uuid), "daysSubscribed", days);
            long expire = System.currentTimeMillis() + ((long) days) * 86400000L;
            connection.hset(String.format("plus.%s", uuid), "expire", expire + "");
            connection.hset(String.format("plus.%s", uuid), "streakStart", System.currentTimeMillis() + "");
            connection.hset(String.format("plus.%s", uuid), "streak", "0");
        }
    }

    public FriendsList.VisibilityMode getVisibilityMode(UUID uuid) {
        try (Jedis redisConnection = jedis.getResource()) {
            return FriendsList.VisibilityMode.valueOf(redisConnection.hget(String.format("friends.%s", uuid.toString()), "visibility"));
        }
    }

    public FriendStatus getFriendStatus(UUID uuid) {
        try (Jedis redisConnection = jedis.getResource()) {
            try {
                return (FriendStatus) AuroraMCAPI.getCosmetics().get(Integer.parseInt(redisConnection.hget(String.format("friends.%s", uuid), "status")));
            } catch (Exception ignored) {
                return (FriendStatus) AuroraMCAPI.getCosmetics().get(100);
            }
        }
    }

    public PlayerPreferences getPlayerPreferences(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            boolean friendRequests, partyRequests, chatVisibility, pingOnPrivateMessage, pingOnPartyChat, hubVisibility, hubSpeed, hubFlight, reportNotifications, hubInvisibility, ignoreHubKnockback, socialMediaNotifications, staffLoginNotifications, approvalNotifications, approvalProcessedNotifications, hubForcefield, hideDisguiseName, pingOnChatMention;
            PlayerPreferences.MuteInformMode muteInformMode = PlayerPreferences.MuteInformMode.valueOf(connection.hget(String.format("prefs.%s", uuid), "muteInformMode"));
            PlayerPreferences.PrivateMessageMode privateMessageMode = PlayerPreferences.PrivateMessageMode.valueOf(connection.hget(String.format("prefs.%s", uuid), "privateMessageMode"));
            Pronoun preferredPronouns = Pronoun.valueOf(connection.hget(String.format("prefs.%s", uuid), "preferredPronouns"));

            friendRequests = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "friendRequests"));
            partyRequests = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "partyRequests"));
            chatVisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "chatVisibility"));
            pingOnPrivateMessage = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "pingOnPrivateMessage"));
            pingOnPartyChat = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "pingOnPartyChat"));
            hubVisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hubVisibility"));
            hubSpeed = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hubSpeed"));
            hubFlight = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hubFlight"));
            reportNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "reportNotifications"));
            hubInvisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hubInvisibility"));
            ignoreHubKnockback = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "ignoreHubKnockback"));
            socialMediaNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "socialMediaNotifications"));
            staffLoginNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "staffLoginNotifications"));
            approvalNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "approvalNotifications"));
            approvalProcessedNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "approvalProcessedNotifications"));
            hubForcefield = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hubForcefield"));
            hideDisguiseName = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "hideDisguiseName"));
            pingOnChatMention = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", uuid), "pingOnChatMention"));

            return new PlayerPreferences(null, friendRequests, partyRequests, muteInformMode, chatVisibility, privateMessageMode, pingOnPrivateMessage, pingOnPartyChat, hubVisibility, hubSpeed, hubFlight, reportNotifications, hubInvisibility, ignoreHubKnockback, socialMediaNotifications, staffLoginNotifications, approvalNotifications, approvalProcessedNotifications, hubForcefield, hideDisguiseName, pingOnChatMention, preferredPronouns);
        }
    }

    public long getLastSeen(int id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("seen." + id)) {
                return -1;
            }
            return Long.parseLong(connection.get("seen." + id));
        }
    }

    public void setLastSeen(int id, long timestamp) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("seen." + id, String.valueOf(timestamp));
        }
    }

    public String hashIP(String ip) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(ip.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            // return the HashText
            return hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SMPLocation getSMPLogoutLocation(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".logout")) {
                return null;
            }
            double x = Double.parseDouble(connection.hget("smp." + id + ".logout", "x"));
            double y = Double.parseDouble(connection.hget("smp." + id + ".logout", "y"));
            double z = Double.parseDouble(connection.hget("smp." + id + ".logout", "z"));
            float pitch = Float.parseFloat(connection.hget("smp." + id + ".logout", "pitch"));
            float yaw = Float.parseFloat(connection.hget("smp." + id + ".logout", "yaw"));
            SMPLocation.Dimension dimension = SMPLocation.Dimension.valueOf(connection.hget("smp." + id + ".logout", "dimension"));
            SMPLocation.Reason reason = SMPLocation.Reason.valueOf(connection.hget("smp." + id + ".logout", "reason"));

            return new SMPLocation(dimension, x, y, z, pitch, yaw, reason);
        }
    }

    public void setSMPLogoutLocation(UUID id, SMPLocation location) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset("smp." + id + ".logout", "x", String.valueOf(location.getX()));
            connection.hset("smp." + id + ".logout", "y", String.valueOf(location.getY()));
            connection.hset("smp." + id + ".logout", "z", String.valueOf(location.getZ()));
            connection.hset("smp." + id + ".logout", "pitch", String.valueOf(location.getPitch()));
            connection.hset("smp." + id + ".logout", "yaw", String.valueOf(location.getYaw()));
            connection.hset("smp." + id + ".logout", "dimension", location.getDimension().name());
            connection.hset("smp." + id + ".logout", "reason", location.getReason().name());
        }
    }

    public SMPLocation getSMPHomeLocation(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".home")) {
                return null;
            }
            double x = Double.parseDouble(connection.hget("smp." + id + ".home", "x"));
            double y = Double.parseDouble(connection.hget("smp." + id + ".home", "y"));
            double z = Double.parseDouble(connection.hget("smp." + id + ".home", "z"));
            float pitch = Float.parseFloat(connection.hget("smp." + id + ".home", "pitch"));
            float yaw = Float.parseFloat(connection.hget("smp." + id + ".home", "yaw"));
            SMPLocation.Dimension dimension = SMPLocation.Dimension.valueOf(connection.hget("smp." + id + ".home", "dimension"));
            SMPLocation.Reason reason = SMPLocation.Reason.valueOf(connection.hget("smp." + id + ".home", "reason"));

            return new SMPLocation(dimension, x, y, z, pitch, yaw, reason);
        }
    }

    public void setSMPHomeLocation(UUID id, SMPLocation location) {
        try (Jedis connection = jedis.getResource()) {
            if (location == null) {
                connection.del("smp." + id + ".home");
                return;
            }
            connection.hset("smp." + id + ".home", "x", String.valueOf(location.getX()));
            connection.hset("smp." + id + ".home", "y", String.valueOf(location.getY()));
            connection.hset("smp." + id + ".home", "z", String.valueOf(location.getZ()));
            connection.hset("smp." + id + ".home", "pitch", String.valueOf(location.getPitch()));
            connection.hset("smp." + id + ".home", "yaw", String.valueOf(location.getYaw()));
            connection.hset("smp." + id + ".home", "dimension", location.getDimension().name());
            connection.hset("smp." + id + ".home", "reason", location.getReason().name());
        }
    }

    public SMPLocation getSMPBackLocation(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".back")) {
                return null;
            }
            double x = Double.parseDouble(connection.hget("smp." + id + ".back", "x"));
            double y = Double.parseDouble(connection.hget("smp." + id + ".back", "y"));
            double z = Double.parseDouble(connection.hget("smp." + id + ".back", "z"));
            float pitch = Float.parseFloat(connection.hget("smp." + id + ".back", "pitch"));
            float yaw = Float.parseFloat(connection.hget("smp." + id + ".back", "yaw"));
            SMPLocation.Dimension dimension = SMPLocation.Dimension.valueOf(connection.hget("smp." + id + ".back", "dimension"));
            SMPLocation.Reason reason = SMPLocation.Reason.valueOf(connection.hget("smp." + id + ".back", "reason"));

            return new SMPLocation(dimension, x, y, z, pitch, yaw, reason);
        }
    }

    public void setSMPBackLocation(UUID id, SMPLocation location) {
        try (Jedis connection = jedis.getResource()) {
            if (location == null) {
                connection.del("smp." + id + ".back");
                return;
            }
            connection.hset("smp." + id + ".back", "x", String.valueOf(location.getX()));
            connection.hset("smp." + id + ".back", "y", String.valueOf(location.getY()));
            connection.hset("smp." + id + ".back", "z", String.valueOf(location.getZ()));
            connection.hset("smp." + id + ".back", "pitch", String.valueOf(location.getPitch()));
            connection.hset("smp." + id + ".back", "yaw", String.valueOf(location.getYaw()));
            connection.hset("smp." + id + ".back", "dimension", location.getDimension().name());
            connection.hset("smp." + id + ".back", "reason", location.getReason().name());
        }
    }

    public SMPLocation getSMPTeamHomeLocation(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".teamhome")) {
                return null;
            }
            double x = Double.parseDouble(connection.hget("smp." + id + ".teamhome", "x"));
            double y = Double.parseDouble(connection.hget("smp." + id + ".teamhome", "y"));
            double z = Double.parseDouble(connection.hget("smp." + id + ".teamhome", "z"));
            float pitch = Float.parseFloat(connection.hget("smp." + id + ".teamhome", "pitch"));
            float yaw = Float.parseFloat(connection.hget("smp." + id + ".teamhome", "yaw"));
            SMPLocation.Dimension dimension = SMPLocation.Dimension.valueOf(connection.hget("smp." + id + ".teamhome", "dimension"));
            SMPLocation.Reason reason = SMPLocation.Reason.valueOf(connection.hget("smp." + id + ".teamhome", "reason"));

            return new SMPLocation(dimension, x, y, z, pitch, yaw, reason);
        }
    }

    public void setSMPTeamHomeLocation(UUID id, SMPLocation location) {
        try (Jedis connection = jedis.getResource()) {
            if (location == null) {
                connection.del("smp." + id + ".teamhome");
                return;
            }
            connection.hset("smp." + id + ".teamhome", "x", String.valueOf(location.getX()));
            connection.hset("smp." + id + ".teamhome", "y", String.valueOf(location.getY()));
            connection.hset("smp." + id + ".teamhome", "z", String.valueOf(location.getZ()));
            connection.hset("smp." + id + ".teamhome", "pitch", String.valueOf(location.getPitch()));
            connection.hset("smp." + id + ".teamhome", "yaw", String.valueOf(location.getYaw()));
            connection.hset("smp." + id + ".teamhome", "dimension", location.getDimension().name());
            connection.hset("smp." + id + ".teamhome", "reason", location.getReason().name());
        }
    }

    public SMPLocation getSMPBedLocation(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".bed")) {
                return null;
            }
            double x = Double.parseDouble(connection.hget("smp." + id + ".bed", "x"));
            double y = Double.parseDouble(connection.hget("smp." + id + ".bed", "y"));
            double z = Double.parseDouble(connection.hget("smp." + id + ".bed", "z"));

            return new SMPLocation(null, x, y, z, -1, -1, null);
        }
    }

    public void setSMPBedLocation(UUID id, SMPLocation location) {
        try (Jedis connection = jedis.getResource()) {
            if (location == null) {
                connection.del("smp." + id + ".bed");
            } else {
                connection.hset("smp." + id + ".bed", "x", String.valueOf(location.getX()));
                connection.hset("smp." + id + ".bed", "y", String.valueOf(location.getY()));
                connection.hset("smp." + id + ".bed", "z", String.valueOf(location.getZ()));
            }
        }
    }

    public void setInventory(UUID id, String[] inventory) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".inventory", String.join(",", inventory));
        }
    }

    public double getHealth(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".health")) {
                return 20.0;
            }
            return Double.parseDouble(connection.get("smp." + id + ".health"));
        }
    }

    public void setHealth(UUID id, double health) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".health", String.valueOf(health));
        }
    }

    public int getHunger(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".hunger")) {
                return 20;
            }
            return Integer.parseInt(connection.get("smp." + id + ".hunger"));
        }
    }

    public void setHunger(UUID id, int hunger) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".hunger", String.valueOf(hunger));
        }
    }

    public float getLogoutFall(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".fall")) {
                return 0;
            }
            return Float.parseFloat(connection.get("smp." + id + ".fall"));
        }
    }

    public void setLogoutFall(UUID id, float fall) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".fall", String.valueOf(fall));
        }
    }

    public int getLevel(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".level")) {
                return 0;
            }
            return Integer.parseInt(connection.get("smp." + id + ".level"));
        }
    }

    public void setLevel(UUID id, int level) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".level", String.valueOf(level));
        }
    }

    public float getExp(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".exp")) {
                return 0;
            }
            return Float.parseFloat(connection.get("smp." + id + ".exp"));
        }
    }

    public void setExp(UUID id, float exp) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".exp", String.valueOf(exp));
        }
    }


    public int getFireTicks(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".fire")) {
                return 0;
            }
            return Integer.parseInt(connection.get("smp." + id + ".fire"));
        }
    }

    public void setFireTicks(UUID id, int fire) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("smp." + id + ".fire", String.valueOf(fire));
        }
    }


    public SMPLocation getLogoutVector(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".logout")) {
                return null;
            }
            double x = Double.parseDouble(connection.hget("smp." + id + ".vector", "x"));
            double y = Double.parseDouble(connection.hget("smp." + id + ".vector", "y"));
            double z = Double.parseDouble(connection.hget("smp." + id + ".vector", "z"));

            return new SMPLocation(null, x, y, z, -1, -1, null);
        }
    }

    public void setLogoutVector(UUID id, SMPLocation location) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset("smp." + id + ".vector", "x", String.valueOf(location.getX()));
            connection.hset("smp." + id + ".vector", "y", String.valueOf(location.getY()));
            connection.hset("smp." + id + ".vector", "z", String.valueOf(location.getZ()));
        }
    }

    public List<SMPPotionEffect> getPotionEffects(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            Map<String, String> potions = connection.hgetAll("smp." + id + ".potions");
            List<SMPPotionEffect> potionEffects = new ArrayList<>();
            for (Map.Entry<String, String> potion : potions.entrySet()) {
                String[] split = potion.getValue().split(";");
                int amount = Integer.parseInt(split[0]);
                int duration = Integer.parseInt(split[1]);
                potionEffects.add(new SMPPotionEffect(potion.getKey(), amount, duration));
            }
            return potionEffects;
        }
    }

    public void setPotionEffects(UUID id, List<SMPPotionEffect> effects) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            pipeline.del("smp." + id + ".potions");
            for (SMPPotionEffect effect : effects) {
                pipeline.hset("smp." + id + ".potions", effect.getType(), effect.getLevel() + ";" + effect.getDuration());
            }
            pipeline.sync();
        }
    }

    public String getSMPTeamName(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            return connection.hget("smp.team." + uuid.toString(), "name");
        }
    }

    public void setSMPTeamName(UUID uuid, String name) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset("smp.team." + uuid.toString(), "name", name);
        }
    }

    public String getSMPTeamPrefix(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            return connection.hget("smp.team." + uuid.toString(), "prefix");
        }
    }

    public void setSMPTeamPrefix(UUID uuid, String prefix) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset("smp.team." + uuid.toString(), "prefix", prefix);
        }
    }

    public UUID getSMPTeamLeader(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp.team." + uuid.toString())) {
                return null;
            }
            return UUID.fromString(connection.hget("smp.team." + uuid, "leader"));
        }
    }

    public void setSMPTeamLeader(UUID uuid, UUID leader) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset("smp.team." + uuid, "leader", leader.toString());
        }
    }

    public List<UUID> getSMPTeamMembers(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp.team." + uuid.toString())) {
                return new ArrayList<>();
            }
            List<UUID> uuids = new ArrayList<>();
            String[] strings = connection.hget("smp.team." + uuid, "members").split(";");
            for (String string : strings) {
                if (string.equals("")) {
                    continue;
                }
                uuids.add(UUID.fromString(string));
            }
            return uuids;
        }
    }

    public void setSMPTeamMembers(UUID uuid, List<String> members) {
        try (Jedis connection = jedis.getResource()) {
            connection.hset("smp.team." + uuid, "members", String.join(";", members));
        }
    }

    public void disbandSMPTeam(UUID uuid, List<UUID> members) {
        try (Jedis connection = jedis.getResource()) {
            connection.del("smp.team." + uuid);
            for (UUID id : members) {
                connection.del("smp." + id + ".team");
            }
        }
    }

    public String[] getInventory(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".inventory")) {
                return null;
            }
            return connection.get("smp." + id + ".inventory").split(",");
        }
    }

    public UUID getSMPTeam(UUID id) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("smp." + id + ".team")) {
                return null;
            }
            return UUID.fromString(connection.get("smp." + id + ".team"));
        }
    }

    public void setSMPTeam(UUID id, UUID team) {
        try (Jedis connection = jedis.getResource()) {
            if (team == null) {
                connection.del("smp." + id + ".team");
                return;
            }
            connection.set("smp." + id + ".team", team.toString());
        }
    }

    public void logBlockEvent(BlockLogEvent e) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO smp_blocklogs VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setTimestamp(1, new Timestamp(e.getTimestamp()));
            statement.setString(2, e.getType().name());
            statement.setString(3, e.getLocation().getDimension().name());
            statement.setInt(4, (int) e.getLocation().getX());
            statement.setInt(5, (int) e.getLocation().getY());
            statement.setInt(6, (int) e.getLocation().getZ());
            if (e.getPlayer() == null) {
                statement.setNull(7, Types.VARCHAR);
            } else {
                statement.setString(7, e.getPlayer().toString());
            }
            statement.setString(8, e.getMaterial());

            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<BlockLogEvent> getBlockLog(int x, int y, int z, SMPLocation.Dimension dimension, int limit, BlockLogEvent.LogType logType) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement;
            if (logType == BlockLogEvent.LogType.ALL) {
                statement = connection.prepareStatement("SELECT * FROM smp_blocklogs WHERE x = ? AND y = ? AND z = ? AND dimension = ? ORDER BY timestamp DESC LIMIT ?");
                statement.setInt(5, limit);
            } else {
                statement = connection.prepareStatement("SELECT * FROM smp_blocklogs WHERE x = ? AND y = ? AND z = ? AND dimension = ? AND type = ? ORDER BY timestamp DESC LIMIT ?");
                statement.setInt(6, limit);
                statement.setString(5, logType.name());
            }
            statement.setInt(1, x);
            statement.setInt(2, y);
            statement.setInt(3, z);
            statement.setString(4, dimension.name());


            ResultSet set = statement.executeQuery();
            List<BlockLogEvent> events = new ArrayList<>();
            while (set.next()) {
                events.add(new BlockLogEvent(set.getTimestamp(1).getTime(), ((set.getString(7) == null)?null:UUID.fromString(set.getString(7))), BlockLogEvent.LogType.valueOf(set.getString(2)), new SMPLocation(dimension, x, y, z, 0, 0, null), set.getString(8)));
            }
            return events;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<BlockLogEvent> getBlockLog(UUID uuid, SMPLocation.Dimension dimension, int limit, BlockLogEvent.LogType logType) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement;
            if (logType == BlockLogEvent.LogType.ALL) {
                statement = connection.prepareStatement("SELECT * FROM smp_blocklogs WHERE uuid = ? AND dimension = ? ORDER BY timestamp DESC LIMIT ?");
                statement.setInt(3, limit);
            } else {
                statement = connection.prepareStatement("SELECT * FROM smp_blocklogs WHERE uuid = ? AND dimension = ? AND type = ? ORDER BY timestamp DESC LIMIT ?");
                statement.setInt(4, limit);
                statement.setString(3, logType.name());
            }
            statement.setString(1, uuid.toString());
            statement.setString(2, dimension.name());


            ResultSet set = statement.executeQuery();
            List<BlockLogEvent> events = new ArrayList<>();
            while (set.next()) {
                events.add(new BlockLogEvent(set.getTimestamp(1).getTime(), uuid, BlockLogEvent.LogType.valueOf(set.getString(2)), new SMPLocation(dimension, set.getInt(4), set.getInt(5), set.getInt(6), 0, 0, null), set.getString(8)));
            }
            return events;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<BlockLogEvent> getBlockLog(String material, SMPLocation.Dimension dimension, int limit, BlockLogEvent.LogType logType) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement;
            if (logType == BlockLogEvent.LogType.ALL) {
                statement = connection.prepareStatement("SELECT * FROM smp_blocklogs WHERE material = ? AND dimension = ? ORDER BY timestamp DESC LIMIT ?");
                statement.setInt(3, limit);
            } else {
                statement = connection.prepareStatement("SELECT * FROM smp_blocklogs WHERE material = ? AND dimension = ? AND type = ? ORDER BY timestamp DESC LIMIT ?");
                statement.setInt(4, limit);
                statement.setString(3, logType.name());
            }
            statement.setString(1, material);
            statement.setString(2, dimension.name());


            ResultSet set = statement.executeQuery();
            List<BlockLogEvent> events = new ArrayList<>();
            while (set.next()) {
                events.add(new BlockLogEvent(set.getTimestamp(1).getTime(), ((set.getString(7) == null)?null:UUID.fromString(set.getString(7))), BlockLogEvent.LogType.valueOf(set.getString(2)), new SMPLocation(dimension, set.getInt(4), set.getInt(5), set.getInt(6), 0, 0, null), material));
            }
            return events;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void addPreloadMessage(UUID uuid, Rank rank) {
        try (Jedis connection = jedis.getResource()) {
            connection.set("preload." + uuid, rank.name());
        }
    }

    public Rank getPreloadMessage(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            if (!connection.exists("preload." + uuid)) {
                return null;
            }
            return Rank.valueOf(connection.get("preload." + uuid));
        }
    }

    public void removePreloadMessage(UUID uuid) {
        try (Jedis connection = jedis.getResource()) {
            connection.del("preload." + uuid);
        }
    }

    public Jedis getRedisConnection() {
        return jedis.getResource();
    }
}

