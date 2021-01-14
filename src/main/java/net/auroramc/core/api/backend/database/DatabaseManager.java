package net.auroramc.core.api.backend.database;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ServerInfo;
import net.auroramc.core.api.backend.database.util.MySQLConnectionPool;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.api.players.*;
import net.auroramc.core.api.players.friends.Friend;
import net.auroramc.core.api.players.friends.FriendStatus;
import net.auroramc.core.api.players.friends.FriendsList;
import net.auroramc.core.api.players.lookup.IPLookup;
import net.auroramc.core.api.players.lookup.LookupUser;
import net.auroramc.core.api.punishments.AdminNote;
import net.auroramc.core.api.punishments.Ban;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.Rule;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.stats.GameStatistics;
import net.auroramc.core.api.stats.PlayerBank;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.ChatFilter;
import net.auroramc.core.api.utils.disguise.CachedSkin;
import org.bukkit.Bukkit;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseManager {

    private final MySQLConnectionPool mysql;
    private final JedisPool jedis;

    public DatabaseManager() {
        //Setting up MySQL connection pool.
        MySQLConnectionPool mysql1;
        try {
            mysql1 = new MySQLConnectionPool(AuroraMCAPI.getCore().getConfig().getString("mysqlhost"), AuroraMCAPI.getCore().getConfig().getString("mysqlport"), AuroraMCAPI.getCore().getConfig().getString("mysqldb"), AuroraMCAPI.getCore().getConfig().getString("mysqlusername"), AuroraMCAPI.getCore().getConfig().getString("mysqlpassword"));
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
        jedis = new JedisPool(config, AuroraMCAPI.getCore().getConfig().getString("redishost"), 6379, 2000, AuroraMCAPI.getCore().getConfig().getString("redisauth"));
    }

    public Disguise getDisguise(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.exists(String.format("disguise.%s.skin", player.getPlayer().getUniqueId().toString()))) {
                //They have an active disguise.
                String skin,signature,name;
                Rank rank;

                skin = connection.get(String.format("disguise.%s.skin", player.getPlayer().getUniqueId().toString()));
                signature = connection.get(String.format("disguise.%s.signature", player.getPlayer().getUniqueId().toString()));
                name = connection.get(String.format("disguise.%s.name", player.getPlayer().getUniqueId().toString()));

                rank = AuroraMCAPI.getRanks().get(Integer.parseInt(connection.get(String.format("disguise.%s.rank", player.getPlayer().getUniqueId().toString()))));

                return new Disguise(player, name, skin, signature, rank);
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
            pipeline.set(String.format("disguise.%s.skin", player.getPlayer().getUniqueId().toString()), disguise.getSkin());
            pipeline.set(String.format("disguise.%s.signature", player.getPlayer().getUniqueId().toString()), disguise.getSignature());
            pipeline.set(String.format("disguise.%s.name", player.getPlayer().getUniqueId().toString()), disguise.getName());
            pipeline.set(String.format("disguise.%s.rank", player.getPlayer().getUniqueId().toString()), disguise.getRank().getId() + "");
            pipeline.sync();
        }
    }

    public void undisguise(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            pipeline.del(String.format("disguise.%s.skin", player.getPlayer().getUniqueId().toString()));
            pipeline.del(String.format("disguise.%s.signature", player.getPlayer().getUniqueId().toString()));
            pipeline.del(String.format("disguise.%s.name", player.getPlayer().getUniqueId().toString()));
            pipeline.del(String.format("disguise.%s.rank", player.getPlayer().getUniqueId().toString()));
            pipeline.sync();
        }
    }

    public void undisguise(String uuid) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            pipeline.del(String.format("disguise.%s.skin", uuid));
            pipeline.del(String.format("disguise.%s.signature", uuid));
            pipeline.del(String.format("disguise.%s.name", uuid));
            pipeline.del(String.format("disguise.%s.rank", uuid));
            pipeline.sync();
        }
    }

    public int newUser(AuroraMCPlayer player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auroramc_players(uuid, `name`) VALUES (?, ?);");
            statement.setString(1, player.getPlayer().getUniqueId().toString());
            statement.setString(2, player.getPlayer().getName());

            statement.execute();

            int id = getAuroraMCID(player.getPlayer().getUniqueId());
            //Creating records in necessary databases.

            statement = connection.prepareStatement("INSERT INTO ignored(amc_id, users) VALUES (?,'');");
            statement.setInt(1, id);
            statement.execute();

            statement = connection.prepareStatement("INSERT INTO ranks(amc_id, rank) VALUES (?, 0);");
            statement.setInt(1, id);
            statement.execute();

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



    public Rank getRank(AuroraMCPlayer player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT rank FROM `ranks` WHERE amc_id = ?");
            statement.setLong(1, player.getId());

            ResultSet set = statement.executeQuery();
            if (set.next()) {

                return AuroraMCAPI.getRanks().get(set.getInt(1));
            } else {
                //NEW USER
                statement = connection.prepareStatement("INSERT INTO ranks (amc_id, rank) VALUES (?, ?)");
                statement.setLong(1, player.getId());
                statement.setInt(2, 0);
                return AuroraMCAPI.getRanks().get(0);
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
                return AuroraMCAPI.getRanks().get(set.getInt(1));
            } else {
                //NEW USER
                return AuroraMCAPI.getRanks().get(0);
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
                    subRanks.add(AuroraMCAPI.getSubRanks().get(Integer.parseInt(rank)));
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
                    subRanks.add(AuroraMCAPI.getSubRanks().get(Integer.parseInt(rank)));
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
                    Bukkit.getLogger().info("test");
                    //There are already registered rank/subrank changes in the database. Check to see if a rank update has already occured.
                    if (results.getString(2) != null) {
                        if (results.getString(2).equals(rank.getId() + "")) {
                            statement = connection.prepareStatement("DELETE FROM rank_changes WHERE discord_id = ? AND old_rank = ?");
                            statement.setString(1, discordId);
                            statement.setString(2, results.getString(2));
                            statement.execute();
                            return success;
                        }
                        Bukkit.getLogger().info("test");
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
                            Bukkit.getLogger().info("test");
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
            connection.set(String.format("discord.link.%s", code), player.getPlayer().getUniqueId().toString());
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
                return new ServerInfo(set.getString(1), ip, port, new JSONObject(set.getString(4)));
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
            if (connection.exists(String.format("server.%s", uuid.toString()))) {
                return connection.get(String.format("server.%s", uuid.toString()));
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

            statement.setString(3, remover.getPlayer().getName());
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
            return con.sismember("vanish", player.getPlayer().getUniqueId().toString());
        }
    }

    public void vanish(AuroraMCPlayer player) {
        try (Jedis con = jedis.getResource()) {
            con.sadd("vanish", player.getPlayer().getUniqueId().toString());
        }
    }

    public void unvanish(AuroraMCPlayer player) {
        try (Jedis con = jedis.getResource()) {
            con.srem("vanish", player.getPlayer().getUniqueId().toString());
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

    public List<String> globalAccountSuspend(String code, int id, int issuer, long timestamp, String note) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT known_ip_profiles FROM auroramc_players WHERE id = ?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();

            //Add these IP profiles to the list of banned ones.
            statement = connection.prepareStatement("INSERT INTO global_account_suspensions(punishment_id, root_account, issuer, banned_profiles, timestamp, reason) VALUES (?,?,?,?,?,?)");
            statement.setString(1, code);
            statement.setInt(2, id);
            statement.setInt(3, issuer);
            statement.setString(4, set.getString(1));
            statement.setString(5, timestamp + "");
            statement.setString(6, note);
            statement.execute();

            List<String> usernames = new ArrayList<>();
            for (String profileId : set.getString(1).split(",")) {
                statement = connection.prepareStatement("SELECT ids FROM ip_profile WHERE profile_id = ?");
                statement.setInt(1, Integer.parseInt(profileId));
                set = statement.executeQuery();
                set.next();
                List<String> ids = new ArrayList<>(Arrays.asList(set.getString(1).split(",")));
                for (String amcId : ids) {
                    statement = connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                    statement.setInt(1, Integer.parseInt(amcId));
                    set = statement.executeQuery();
                    set.next();
                    if (!usernames.contains(set.getString(1))) {
                        usernames.add(set.getString(1));
                    }
                }
            }
            return usernames;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IPLookup ipLookup(UUID uuid) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement =  connection.prepareStatement("SELECT last_used_profile FROM auroramc_players WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (set.getInt(1) > 0) {
                    int profile = set.getInt(1);
                    statement = connection.prepareStatement("SELECT * FROM ip_profile WHERE profile_id = ?");
                    statement.setInt(1, profile);
                    set = statement.executeQuery();
                    set.next();

                    List<LookupUser> users = new ArrayList<>();
                    String ip = set.getString(2);
                    int amountBanned = 0;
                    int amountMuted = 0;
                    List<String> userids = new ArrayList<>(Arrays.asList(set.getString(3).split(",")));
                    for (String userID : userids) {
                        statement =  connection.prepareStatement("SELECT name FROM auroramc_players WHERE id = ?");
                        statement.setInt(1, Integer.parseInt(userID));
                        set = statement.executeQuery();
                        set.next();
                        String name = set.getString(1);

                        statement = connection.prepareStatement("SELECT * FROM punishments WHERE amc_id = ? AND (status = 1 OR status = 2 OR status = 3)");
                        statement.setInt(1, Integer.parseInt(userID));
                        set = statement.executeQuery();
                        boolean banned = false;
                        boolean muted = false;
                        while (set.next() && (!muted || !banned)) {
                            if (AuroraMCAPI.getRules().getRule(set.getInt(3)).getType() == 1) {
                                muted = true;
                            } else {
                                banned = true;
                            }
                        }
                        if (muted) {
                            amountMuted++;
                        }
                        if (banned) {
                            amountBanned++;
                        }
                        users.add(new LookupUser(Integer.parseInt(userID), name, muted, banned));
                    }

                    return new IPLookup(ip, profile, users, amountBanned, amountMuted);
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

            Map<String, String> stats = connection.hgetAll(String.format("stats.%s.game", player.getPlayer().getUniqueId().toString()));
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

            stats = connection.hgetAll(String.format("stats.%s.achievements", player.getPlayer().getUniqueId().toString()));
            Map<Achievement, Integer> achievements = new HashMap<>();
            //Load Achievements.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                Achievement achievement = AuroraMCAPI.getAchievement(Integer.parseInt(stat.getKey()));
                int level = Integer.parseInt(stat.getValue());
                achievements.put(achievement, level);
            }

            stats = connection.hgetAll(String.format("stats.%s.achievements.progress", player.getPlayer().getUniqueId().toString()));
            Map<Achievement, Long> progress = new HashMap<>();
            //Load Achievements.
            for (Map.Entry<String, String> stat : stats.entrySet()) {
                Achievement achievement = AuroraMCAPI.getAchievement(Integer.parseInt(stat.getKey()));
                long amount = Long.parseLong(stat.getValue());
                progress.put(achievement, amount);
            }

            //Load core stuff.
            long totalXpEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "xpEarned"));
            long firstJoinTimestamp = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "firstJoinTimestamp"));
            long xpIntoLevel = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "xpIntoLevel"));
            int level = Integer.parseInt(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "level"));
            long lobbyTimeMs = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "lobbyTimeMs"));
            long gameTimeMs = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "gameTimeMs"));

            long ticketsEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "ticketsEarned"));
            long crownsEarned = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "crownsEarned"));
            long gamesWon = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "gamesWon"));
            long gamesLost = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "gamesLost"));
            long gamesPlayed = Long.parseLong(connection.hget(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "gamesPlayed"));

            return new PlayerStatistics(player, firstJoinTimestamp, totalXpEarned, xpIntoLevel, level, achievements, progress, gameStats, lobbyTimeMs, gameTimeMs, gamesPlayed, gamesWon, gamesLost, ticketsEarned, crownsEarned);
        }
    }

    public PlayerStatistics getStatistics(UUID uuid) {
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

            return new PlayerStatistics(null, firstJoinTimestamp, totalXpEarned, xpIntoLevel, level, achievements, progress, gameStats, lobbyTimeMs, gameTimeMs, gamesPlayed, gamesWon, gamesLost, ticketsEarned, crownsEarned);
        }
    }

    public void ticketsEarned(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "ticketsEarned", amount);
        }
    }

    public void crownsEarned(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("stats.%s.core", player.getPlayer().getUniqueId().toString()), "crownsEarned", amount);
        }
    }

    public PlayerBank getBank(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            long crowns = Long.parseLong(connection.hget(String.format("bank.%s", player.getPlayer().getUniqueId().toString()), "crowns"));
            long tickets = Long.parseLong(connection.hget(String.format("bank.%s", player.getPlayer().getUniqueId().toString()), "tickets"));
            return new PlayerBank(player, tickets, crowns);
        }
    }

    public void ticketsAdded(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("bank.%s", player.getPlayer().getUniqueId().toString()), "tickets", amount);
        }
    }

    public void crownsAdded(AuroraMCPlayer player, long amount) {
        try (Jedis connection = jedis.getResource()) {
            connection.hincrBy(String.format("bank.%s", player.getPlayer().getUniqueId().toString()), "tickets", amount);
        }
    }

    public Character getPlusColour(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getPlayer().getUniqueId()), "plusColour")) {
                return connection.hget(String.format("plus.%s", player.getPlayer().getUniqueId()), "plusColour").charAt(0);
            } else {
                return null;
            }
        }
    }

    public Character getLevelColour(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getPlayer().getUniqueId()), "levelColour")) {
                return connection.hget(String.format("plus.%s", player.getPlayer().getUniqueId()), "levelColour").charAt(0);
            } else {
                return null;
            }
        }
    }

    public long getExpire(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getPlayer().getUniqueId()), "expire")) {
                return Long.parseLong(connection.hget(String.format("plus.%s", player.getPlayer().getUniqueId()), "expire"));
            } else {
                return -1;
            }
        }
    }

    public int getDaysSubscribed(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getPlayer().getUniqueId()), "daysSubscribed")) {
                return Integer.parseInt(connection.hget(String.format("plus.%s", player.getPlayer().getUniqueId()), "daysSubscribed"));
            } else {
                return -1;
            }
        }
    }

    public int getStreak(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getPlayer().getUniqueId()), "streak")) {
                return Integer.parseInt(connection.hget(String.format("plus.%s", player.getPlayer().getUniqueId()), "streak"));
            } else {
                return -1;
            }
        }
    }

    public long getStreakStartTimestamp(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            if (connection.hexists(String.format("plus.%s", player.getPlayer().getUniqueId()), "streakStart")) {
                return Long.parseLong(connection.hget(String.format("plus.%s", player.getPlayer().getUniqueId()), "streakStart"));
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
            FriendStatus status;

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
                visibilityMode = FriendsList.VisibilityMode.valueOf(redisConnection.hget(String.format("friends.%s", player.getPlayer().getUniqueId()), "visibility"));
                status = FriendStatus.valueOf(redisConnection.hget(String.format("friends.%s", player.getPlayer().getUniqueId()), "status"));
            }

            return new FriendsList(player, friends, pendingFriendRequests, visibilityMode, status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ChatChannel getChannel(AuroraMCPlayer player) {
        try (Jedis redisConnection = jedis.getResource()) {
            return ChatChannel.valueOf(redisConnection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "channel"));
        }
    }

    public PlayerPreferences getPlayerPreferences(AuroraMCPlayer player) {
        try (Jedis connection = jedis.getResource()) {
            boolean friendRequests, partyRequests, chatVisibility, pingOnPrivateMessage, pingOnPartyChat, hubVisibility, hubSpeed, hubFlight, reportNotifications, hubInvisibility, ignoreHubKnockback, socialMediaNotifications, staffLoginNotifications, approvalNotifications, approvalProcessedNotifications, hubForcefield, hideDisguiseName;
            PlayerPreferences.MuteInformMode muteInformMode = PlayerPreferences.MuteInformMode.valueOf(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "muteInformMode"));
            PlayerPreferences.PrivateMessageMode privateMessageMode = PlayerPreferences.PrivateMessageMode.valueOf(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "privateMessagesMode"));

            friendRequests = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "friendRequests"));
            partyRequests = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "partyRequests"));
            chatVisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "chatVisibility"));
            pingOnPrivateMessage = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "pingOnPrivateMessage"));
            pingOnPartyChat = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "pingOnPartyChat"));
            hubVisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "hubVisibility"));
            hubSpeed = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "hubSpeed"));
            hubFlight = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "hubFlight"));
            reportNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "reportNotifications"));
            hubInvisibility = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "hubInvisibility"));
            ignoreHubKnockback = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "ignoreHubKnockback"));
            socialMediaNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "socialMediaNotifications"));
            staffLoginNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "staffLoginNotifications"));
            approvalNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "approvalNotifications"));
            approvalProcessedNotifications = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "approvalProcessedNotifications"));
            hubForcefield = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "hubForcefield"));
            hideDisguiseName = Boolean.parseBoolean(connection.hget(String.format("prefs.%s", player.getPlayer().getUniqueId()), "hideDisguiseName"));

            return new PlayerPreferences(player, friendRequests, partyRequests, muteInformMode, chatVisibility, privateMessageMode, pingOnPrivateMessage, pingOnPartyChat, hubVisibility, hubSpeed, hubFlight, reportNotifications, hubInvisibility, ignoreHubKnockback, socialMediaNotifications, staffLoginNotifications, approvalNotifications, approvalProcessedNotifications, hubForcefield, hideDisguiseName);
        }
    }

    public boolean reportDump(UUID uuid, JSONObject json, long timestamp) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO chatlogs VALUES (?, ?, ?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, AuroraMCAPI.getServerInfo().getName());
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

    public PlayerReport assignReport(int handler, PlayerReport.QueueType queue, PlayerReport.ReportType type) {
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

    public PlayerReport getRecentReport(int suspect, PlayerReport.ReportType type) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports WHERE suspect = ? AND timestamp > ? AND outcome = 'PENDING' AND type = ?");
            statement.setInt(1, suspect);
            statement.setLong(2, (System.currentTimeMillis() - 60000));
            statement.setString(3, type.name());

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

    public boolean isUsernameBanned(String s) {
        try (Jedis connection = jedis.getResource()) {
            return connection.sismember("usernamebans", s.toLowerCase());
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

    public boolean hasActiveSession(int player) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM sessions WHERE record_after IS NOT NULL AND timestamp + INTERVAL 1 DAY > now() AND amc_id = ?");
            statement.setInt(1, player);

            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
