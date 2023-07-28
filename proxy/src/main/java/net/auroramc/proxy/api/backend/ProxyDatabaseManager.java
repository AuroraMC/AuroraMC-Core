/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.backend;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.Info;
import net.auroramc.api.permissions.Rank;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.player.party.Party;
import net.auroramc.proxy.api.player.party.PartyInvite;
import net.auroramc.proxy.api.player.party.PartyPlayer;
import net.auroramc.proxy.api.utils.MaintenanceMode;
import net.auroramc.proxy.api.utils.PanelAccountType;
import net.auroramc.proxy.api.utils.ProxySettings;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ProxyDatabaseManager {

    public static void newParty(Party party) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            Pipeline pipeline = connection.pipelined();

            pipeline.hset(String.format("party.%s", party.getUuid()), "leader", party.getLeader().getUuid().toString());
            pipeline.hset(String.format("party.%s", party.getUuid()), "players", "");
            pipeline.hset(String.format("party.%s", party.getUuid()), "invites", party.getPartyInvites().get(0).getPlayer().getUuid().toString() + ";" + party.getPartyInvites().get(0).getExpires());
            pipeline.sync();
        }
    }

    public static void updatePartyInvites(Party party) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {

            List<String> invites = new ArrayList<>();
            for (PartyInvite invite : party.getPartyInvites()) {
                invites.add(invite.getPlayer().getUuid().toString() + ";" + invite.getExpires());
            }

            connection.hset(String.format("party.%s", party.getUuid()), "invites", String.join(",", invites));
        }
    }

    public static void updateMembers(Party party) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            List<String> players = new ArrayList<>();
            for (PartyPlayer player : party.getPlayers()) {
                players.add(player.getUuid().toString());
            }
            connection.hset(String.format("party.%s", party.getUuid()), "players", String.join(",", players));
        }
    }

    public static void updateLeader(Party party) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            connection.hset(String.format("party.%s", party.getUuid()), "leader", party.getLeader().getUuid().toString());
        }
    }

    public static void disbandParty(Party party) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            connection.del(String.format("party.%s", party.getUuid()));
        }
    }

    public static Party getParty(UUID partyUUID) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            List<PartyPlayer> members = new ArrayList<>();
            List<PartyInvite> invites = new ArrayList<>();
            String[] mems = connection.hget(String.format("party.%s", partyUUID), "players").split(",");
            String[] invs = connection.hget(String.format("party.%s", partyUUID), "invites").split(",");
            for (String mem : mems) {
                if (mem.equalsIgnoreCase("")) {
                    continue;
                }
                UUID uuid = UUID.fromString(mem);
                int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                String name = AuroraMCAPI.getDbManager().getNameFromID(id);
                Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(uuid);
                PartyPlayer player = new PartyPlayer(id, name, uuid, ProxyAPI.getPlayer(id), proxyUUID, rank);
                members.add(player);
            }
            for (String invite : invs) {
                if (invite.equalsIgnoreCase("")) {
                    continue;
                }
                String[] inv = invite.split(";");
                UUID uuid = UUID.fromString(inv[0]);
                int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                String name = AuroraMCAPI.getDbManager().getNameFromID(id);
                Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(uuid);
                PartyPlayer player = new PartyPlayer(id, name, uuid, null, proxyUUID, rank);
                long expire = Long.parseLong(inv[1]);
                PartyInvite partyInvite = new PartyInvite(null, player, expire);
                invites.add(partyInvite);
            }
            UUID leader = UUID.fromString(connection.hget(String.format("party.%s", partyUUID), "leader"));
            int id = AuroraMCAPI.getDbManager().getAuroraMCID(leader);
            String name = AuroraMCAPI.getDbManager().getNameFromID(id);
            Rank rank = AuroraMCAPI.getDbManager().getRank(leader);
            UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(leader);
            PartyPlayer player = new PartyPlayer(id, name, leader, null, proxyUUID, rank);
            return new Party(partyUUID, player, members, invites);
        }
    }

    public static long getInviteExpire(UUID partyUUID, UUID playerUUID) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            String[] invites = connection.hget(String.format("party.%s", partyUUID), "invites").split(",");
            for (String invite : invites) {
                if (invite.startsWith(playerUUID.toString() + ";")) {
                    return Long.parseLong(invite.split(";")[1]);
                }
            }
        }
        return -1;
    }

    public static UUID getPartyLeader(UUID partyUUID) {
        try (Jedis connection = AuroraMCAPI.getDbManager().getRedisConnection()) {
            return UUID.fromString(connection.hget(String.format("party.%s", partyUUID), "leader"));
        }
    }

    public static ProxySettings getProxySettings(Info.Network network) {
        try (Connection connection = AuroraMCAPI.getDbManager().getMySQLConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM proxy_settings WHERE network = ?");
            statement.setString(1, network.name());
            ResultSet set = statement.executeQuery();

            set.next();

            return new ProxySettings(set.getBoolean(2), set.getString(3),set.getString(4), ((set.getString(5) == null)?null: MaintenanceMode.valueOf(set.getString(5))));
        } catch (SQLException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            return null;
        }
    }

    public static void createPanelAccount(String name, String password, UUID uuid, PanelAccountType type) {
        try (Connection connection = AuroraMCAPI.getDbManager().getMySQLConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts(username, password, uuid, account_type) VALUES (?,?,?,?)");
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, uuid.toString());
            statement.setString(4, type.name());

            statement.execute();
        } catch (SQLException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

    public static void updatePanelPassword(UUID uuid, String password) {
        try (Connection connection = AuroraMCAPI.getDbManager().getMySQLConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET password = ? WHERE uuid = ?");
            statement.setString(1, password);
            statement.setString(2, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

    public static void updatePanelAccountType(UUID uuid, PanelAccountType type) {
        try (Connection connection = AuroraMCAPI.getDbManager().getMySQLConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET account_type = ? WHERE uuid = ?");
            statement.setString(1, type.name());
            statement.setString(2, uuid.toString());

            statement.execute();
        } catch (SQLException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

    public static void updateProfiles(ProxiedPlayer player, int amcId) {
        try (Connection connection = AuroraMCAPI.getDbManager().getMySQLConnection()) {
            String ip = AuroraMCAPI.getDbManager().hashIP(player.getSocketAddress().toString().replace("/", "").split(":")[0]);
            //Updating IP profiles.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ip_profile WHERE ip = ?");
            statement.setString(1, ip);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement = connection.prepareStatement("UPDATE ip_profile SET last_used_by = ?, last_used_at = CURRENT_TIMESTAMP WHERE ip = ?");
                statement.setInt(1, amcId);
                statement.setString(2, ip);
            } else {
                statement = connection.prepareStatement("INSERT INTO ip_profile(ip, last_used_by, last_used_at) VALUES (?,?,CURRENT_TIMESTAMP)");
                statement.setString(1, ip);
                statement.setString(2, "" + amcId);
            }
            statement.execute();

            //Adding this IP Profile to the list of profiles if it has not already been added.
            statement = connection.prepareStatement("SELECT * FROM ip_profile WHERE ip = ?");
            statement.setString(1, ip);
            set = statement.executeQuery();
            set.next();

            statement = connection.prepareStatement("SELECT * FROM auroramc_players WHERE id = ?");
            statement.setInt(1, amcId);
            ResultSet set2 = statement.executeQuery();
            set2.next();

            //Updating name profile.
            statement = connection.prepareStatement("UPDATE auroramc_players SET name = ?, last_used_profile = ? WHERE id = ?");
            statement.setString(1, player.getName());
            statement.setInt(2, set.getInt(1));
            statement.setInt(3, amcId);
            statement.execute();

            //Updating IP logs
            statement = connection.prepareStatement("SELECT * FROM ip_logs WHERE profile_id = ? AND amc_id = ?");
            statement.setInt(1, set.getInt(1));
            statement.setInt(2, amcId);
            set2 = statement.executeQuery();
            if (set2.next()) {
                statement = connection.prepareStatement("UPDATE ip_logs SET last_used = CURRENT_TIMESTAMP , total_usages = total_usages + 1 WHERE profile_id = ? AND amc_id = ?");
                statement.setInt(1, set.getInt(1));
                statement.setInt(2, amcId);
                statement.execute();
            } else {
                statement = connection.prepareStatement("INSERT INTO ip_logs values (?, ?, CURRENT_TIMESTAMP, 1)");
                statement.setInt(1, amcId);
                statement.setInt(2, set.getInt(1));
                statement.execute();
            }
        } catch (SQLException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

}
