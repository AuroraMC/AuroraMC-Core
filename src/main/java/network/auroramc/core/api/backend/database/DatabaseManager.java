package network.auroramc.core.api.backend.database;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.ServerInfo;
import network.auroramc.core.api.backend.database.util.MySQLConnectionPool;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.Disguise;
import network.auroramc.core.api.players.Mentee;
import network.auroramc.core.api.players.Mentor;
import network.auroramc.core.api.punishments.Ban;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.Rule;
import org.bukkit.Bukkit;
import org.json.JSONObject;
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
                statement = connection.prepareStatement("INSERT INTO ranks (amc_id, rank) VALUES (?, ?)");
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
                Rule rule = new Rule(results.getInt(1),results.getString(2),results.getString(3), Integer.parseInt(results.getString(4)),Integer.parseInt(results.getString(6)), results.getBoolean(7), results.getBoolean(5));
                rules.add(rule);
            }

            return rules;
        } catch (SQLException ignored) {
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
            PreparedStatement statement = connection.prepareStatement("SELECT punishments.punishment_id, punishments.amc_id, punishments.rule_id, punishments.notes, punishments.punisher, punishments.issued, punishments.expire, punishments.status, punishments.evidence, punishments.suffix, punishments.removal_reason, punishments.remover, punishments.removal_timestamp, auroramc_players.name FROM punishments INNER JOIN auroramc_players ON auroramc_players.id=punishments.punisher WHERE (SELECT COUNT(*) FROM mentee_distribution WHERE mentees LIKE CONCAT('%', punishments.punisher, '%')) = 0 AND punishments.status = 2 ORDER BY issued ASC");
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


}
