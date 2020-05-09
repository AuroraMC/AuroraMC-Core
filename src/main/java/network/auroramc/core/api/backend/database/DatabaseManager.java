package network.auroramc.core.api.backend.database;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.database.util.MySQLConnectionPool;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.Disguise;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.Rule;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
                Rank rank = null;

                skin = connection.get(String.format("disguise.%s.skin", player.getPlayer().getUniqueId().toString()));
                name = connection.get(String.format("disguise.%s.name", player.getPlayer().getUniqueId().toString()));

                    rank = AuroraMCAPI.getRanks().get(Integer.parseInt(connection.get(String.format("disguise.%s.rank", player.getPlayer().getUniqueId().toString()))));

                return new Disguise(player, name, skin, rank);
            }
        }

        return null;
    }

    public void setDisguise(AuroraMCPlayer player, Disguise disguise) {
        try (Jedis connection = jedis.getResource()) {
            Pipeline pipeline = connection.pipelined();
            pipeline.set(String.format("disguise.%s.skin", player.getPlayer().getUniqueId().toString()), disguise.getSkin());
            pipeline.set(String.format("disguise.%s.signature", player.getPlayer().getUniqueId().toString()), disguise.getSignature());
            pipeline.set(String.format("disguise.%s.name", player.getPlayer().getUniqueId().toString()), disguise.getName());
            pipeline.set(String.format("disguise.%s.rank", player.getPlayer().getUniqueId().toString()), disguise.getRank().getId() + "");
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
            statement = connection.prepareStatement("INSERT INTO bank(amc_id, currency, level, xp_into_level, total_xp) VALUES (?, 0, 0, 0, 0);");

            statement.setInt(1, id);
            statement.execute();

            statement = connection.prepareStatement("INSERT INTO friends(amc_id, friends) VALUES (?,'');");
            statement.setInt(1, id);
            statement.execute();

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
            return -2;
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
                statement = connection.prepareStatement("INSERT INTO rank (amc_id, rank) VALUES (?, ?)");
                statement.setLong(1, player.getId());
                statement.setInt(2, 0);
                return AuroraMCAPI.getRanks().get(0);
            }

        } catch (SQLException e) {
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
            return null;
        }
    }

    public boolean setRank(AuroraMCPlayer player, Rank rank) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE ranks SET rank = ? WHERE amc_id = ?");
            statement.setInt(1, rank.getId());
            statement.setLong(2, player.getId());

            return statement.execute();

        } catch (SQLException ignored) {
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
        } catch (SQLException ignored) {
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
        } catch (SQLException ignored) {
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
                Rule rule = new Rule(results.getInt(1),results.getString(2),results.getString(3), Integer.parseInt(results.getString(4)),Integer.parseInt(results.getString(5)), results.getBoolean(6));
                rules.add(rule);
            }

            return rules;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<Punishment> getPunishmentHistory(int id) {
        try (Connection connection = mysql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT punishment_id,rule_id,notes,punisher,issued,expire,status,evidence,suffix,removal_reason,remover FROM punishments WHERE (amc_id = ?) ORDER BY issued ASC");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();

            ArrayList<Punishment> punishments = new ArrayList<>();
            while (set.next()) {
                punishments.add(new Punishment(set.getString(1), id, set.getInt(2), set.getString(3), set.getInt(4), Long.parseLong(set.getString(5)),Long.parseLong(set.getString(6)), set.getInt(7), set.getString(8), set.getInt(9), set.getString(10), set.getInt(11)));
            }
            return punishments;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
