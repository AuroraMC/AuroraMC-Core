/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.player.team;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.player.SMPPlayer;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SMPTeam {

    private UUID uuid;
    private String name;
    private String prefix;
    private SMPPlayer leader;
    private List<SMPPlayer> members;
    private SMPLocation home;

    public SMPTeam(UUID uuid, String name, String prefix, SMPPlayer leader) {
        this.uuid = uuid;
        this.name = name;
        this.prefix = prefix;
        this.leader = leader;
        this.members = new ArrayList<>();
        this.home = leader.getPlayer().getHome();


        AuroraMCAPI.getDbManager().setSMPTeamHomeLocation(uuid, home);
        AuroraMCAPI.getDbManager().setSMPTeamName(uuid, name);
        AuroraMCAPI.getDbManager().setSMPTeamPrefix(uuid, prefix);
        AuroraMCAPI.getDbManager().setSMPTeamLeader(uuid, leader.getUuid());
        AuroraMCAPI.getDbManager().setSMPTeamMembers(uuid, Collections.emptyList());
    }

    public SMPTeam(UUID uuid) {
        this.uuid = uuid;
        this.name = AuroraMCAPI.getDbManager().getSMPTeamName(uuid);
        this.prefix = AuroraMCAPI.getDbManager().getSMPTeamPrefix(uuid);
        UUID leader = AuroraMCAPI.getDbManager().getSMPTeamLeader(uuid);
        AuroraMCServerPlayer player = ServerAPI.getPlayer(leader);
        if (player != null) {
            this.leader = new SMPPlayer(player.getId(), player.getName(), player.getUuid(), player, player.getRank());
        } else {
            int id = AuroraMCAPI.getDbManager().getAuroraMCID(leader);
            String name = AuroraMCAPI.getDbManager().getNameFromID(id);
            Rank rank = AuroraMCAPI.getDbManager().getRank(id);
            this.leader = new SMPPlayer(id, name, leader, null, rank);
        }
        List<UUID> uuids = AuroraMCAPI.getDbManager().getSMPTeamMembers(uuid);
        this.members = new ArrayList<>();
        for (UUID uuid1 : uuids) {
            AuroraMCServerPlayer player2 = ServerAPI.getPlayer(uuid1);
            if (player2 != null) {
                this.members.add(new SMPPlayer(player2.getId(), player2.getName(), player2.getUuid(), player2, player2.getRank()));
            } else {
                int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid1);
                String name = AuroraMCAPI.getDbManager().getNameFromID(id);
                Rank rank = AuroraMCAPI.getDbManager().getRank(id);
                this.members.add(new SMPPlayer(id, name, uuid1, null, rank));
            }
        }
        this.home = AuroraMCAPI.getDbManager().getSMPTeamHomeLocation(uuid);
    }

    public SMPPlayer getLeader() {
        return leader;
    }

    public void setLeader(SMPPlayer leader, boolean send) {
        members.add(this.leader);
        members.remove(leader);
        this.leader = leader;
        for (SMPPlayer player : members) {
            if (player.getPlayer() != null && player.getPlayer().isOnline()) {
                AuroraMCServerPlayer p = player.getPlayer();
                p.sendMessage(TextFormatter.pluginMessage("Teams","Team Leadership has been transferred to **" + leader.getName() + "**."));
            }
        }
        if (leader.getPlayer() != null && leader.getPlayer().isOnline()) {
            AuroraMCServerPlayer p = leader.getPlayer();
            p.sendMessage(TextFormatter.pluginMessage("Teams","Team Leadership has been transferred to **" + leader.getName() + "**."));
        }
        if (send) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> destinations = new ArrayList<>();
                    String sender = "SMP-Overworld";
                    switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                        case "OVERWORLD": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-End");
                            break;
                        }
                        case "END": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-Overworld");
                            sender = "SMP-End";
                            break;
                        }
                        case "NETHER": {
                            destinations.add("SMP-Overworld");
                            destinations.add("SMP-End");
                            sender = "SMP-Nether";
                            break;
                        }
                    }
                    for (String destination : destinations) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAPS, destination, "setleader", sender, uuid + ";" + leader.getUuid());
                        CommunicationUtils.sendMessage(message);
                    }
                    AuroraMCAPI.getDbManager().setSMPTeamLeader(uuid, leader.getUuid());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }

    }

    public boolean isMember(AuroraMCServerPlayer p) {
        if (p.getSmpTeam().getUuid() == uuid) {
            return true;
        }
        return false;
    }

    public SMPPlayer getMember(UUID uuid) {
        for (SMPPlayer member : members) {
            if (member.getUuid().equals(uuid)) {
                return member;
            }
        }
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addMember(SMPPlayer p, boolean send) {
        members.add(p);
        for (SMPPlayer player : members) {
            if (player.getPlayer() != null && player.getPlayer().isOnline()) {
                AuroraMCServerPlayer pl = player.getPlayer();
                assert pl != null;
                pl.sendMessage(TextFormatter.pluginMessage("Teams","**" + p.getName() + "** has joined the team."));
            }
        }
        if (leader.getPlayer() != null && leader.getPlayer().isOnline()) {
            AuroraMCServerPlayer pl = leader.getPlayer();
            assert pl != null;
            pl.sendMessage(TextFormatter.pluginMessage("Teams","**" + p.getName() + "** has joined the team."));
        }
            if (p.getPlayer() != null && p.getPlayer().isOnline()) {
                for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                    player.updateNametag(p.getPlayer());
                }
                p.getPlayer().setHome(null);
            }
        if (send) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> destinations = new ArrayList<>();
                    String sender = "SMP-Overworld";
                    switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                        case "OVERWORLD": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-End");
                            break;
                        }
                        case "END": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-Overworld");
                            sender = "SMP-End";
                            break;
                        }
                        case "NETHER": {
                            destinations.add("SMP-Overworld");
                            destinations.add("SMP-End");
                            sender = "SMP-Nether";
                            break;
                        }
                    }
                    for (String destination : destinations) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAPS, destination, "addmember", sender, uuid + ";" + p.getUuid());
                        CommunicationUtils.sendMessage(message);
                    }
                    List<String> uuids = new ArrayList<>();
                    for (SMPPlayer player : members) {
                        uuids.add(player.getUuid().toString());
                    }
                    AuroraMCAPI.getDbManager().setSMPTeamMembers(uuid, uuids);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    public void removeMember(UUID p, boolean send) {
        if (getMember(p) == null) {
            return;
        }
        for (SMPPlayer player : members) {
            if (player.getPlayer() != null && player.getPlayer().isOnline()) {
                AuroraMCServerPlayer pl = player.getPlayer();
                assert pl != null;
                pl.sendMessage(TextFormatter.pluginMessage("Teams","**" + getMember(p).getName() + "** has left the team."));
                if (pl.getUniqueId().equals(p)) {
                    pl.setSmpTeam(null);
                    for (AuroraMCServerPlayer player2 : ServerAPI.getPlayers()) {
                        player2.updateNametag(pl);
                    }
                }
            }
        }

        if (leader.getPlayer() != null && leader.getPlayer().isOnline()) {
            AuroraMCServerPlayer pl = leader.getPlayer();
            assert pl != null;
            pl.sendMessage(TextFormatter.pluginMessage("Teams","**" + getMember(p).getName() + "** has left the team."));
        }
        members.remove(getMember(p));
        if (send) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> destinations = new ArrayList<>();
                    String sender = "SMP-Overworld";
                    switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                        case "OVERWORLD": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-End");
                            break;
                        }
                        case "END": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-Overworld");
                            sender = "SMP-End";
                            break;
                        }
                        case "NETHER": {
                            destinations.add("SMP-Overworld");
                            destinations.add("SMP-End");
                            sender = "SMP-Nether";
                            break;
                        }
                    }
                    for (String destination : destinations) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAPS, destination, "removemember", sender, uuid + ";" + p);
                        CommunicationUtils.sendMessage(message);
                    }
                    List<String> uuids = new ArrayList<>();
                    for (SMPPlayer player : members) {
                        uuids.add(player.getUuid().toString());
                    }
                    AuroraMCAPI.getDbManager().setSMPTeamMembers(uuid, uuids);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name, boolean send) {
        this.name = name;


        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            for (SMPPlayer pl : members) {
                if (pl.getPlayer() != null && pl.getPlayer().isOnline()) {
                    player.updateNametag(pl.getPlayer());
                }
            }
            if (leader.getPlayer() != null && leader.getPlayer().isOnline()) {
                player.updateNametag(leader.getPlayer());
            }
        }
        if (send) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> destinations = new ArrayList<>();
                    String sender = "SMP-Overworld";
                    switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                        case "OVERWORLD": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-End");
                            break;
                        }
                        case "END": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-Overworld");
                            sender = "SMP-End";
                            break;
                        }
                        case "NETHER": {
                            destinations.add("SMP-Overworld");
                            destinations.add("SMP-End");
                            sender = "SMP-Nether";
                            break;
                        }
                    }
                    for (String destination : destinations) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAPS, destination, "setname", sender, uuid + ";" + name);
                        CommunicationUtils.sendMessage(message);
                    }
                    AuroraMCAPI.getDbManager().setSMPTeamName(uuid, name);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    public List<SMPPlayer> getMembers() {
        return members;
    }

    public SMPLocation getHome() {
        return home;
    }

    public void setHome(Location home, boolean send) {
        this.home = new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), home.getX(), home.getY(), home.getZ(), home.getPitch(), home.getYaw(), SMPLocation.Reason.HOME);
        if (send) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> destinations = new ArrayList<>();
                    String sender = "SMP-Overworld";
                    switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                        case "OVERWORLD": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-End");
                            break;
                        }
                        case "END": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-Overworld");
                            sender = "SMP-End";
                            break;
                        }
                        case "NETHER": {
                            destinations.add("SMP-Overworld");
                            destinations.add("SMP-End");
                            sender = "SMP-Nether";
                            break;
                        }
                    }
                    for (String destination : destinations) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAPS, destination, "sethome", sender, uuid + ";" + SMPTeam.this.home.toJSON());
                        CommunicationUtils.sendMessage(message);
                    }
                    AuroraMCAPI.getDbManager().setSMPTeamHomeLocation(uuid, SMPTeam.this.home);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    public void disband(boolean send) {
        ServerAPI.getLoadedTeams().remove(uuid);

        if (send) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> destinations = new ArrayList<>();
                    String sender = "SMP-Overworld";
                    switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
                        case "OVERWORLD": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-End");
                            break;
                        }
                        case "END": {
                            destinations.add("SMP-Nether");
                            destinations.add("SMP-Overworld");
                            sender = "SMP-End";
                            break;
                        }
                        case "NETHER": {
                            destinations.add("SMP-Overworld");
                            destinations.add("SMP-End");
                            sender = "SMP-Nether";
                            break;
                        }
                    }
                    for (String destination : destinations) {
                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_MAPS, destination, "disband", sender, uuid.toString());
                        CommunicationUtils.sendMessage(message);
                    }
                    List<UUID> uuids = new ArrayList<>();
                    for (SMPPlayer player : members) {
                        uuids.add(player.getUuid());
                    }
                    uuids.add(leader.getUuid());
                    AuroraMCAPI.getDbManager().disbandSMPTeam(uuid, uuids);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
        for (SMPPlayer player : members) {
            if (player.getPlayer() != null && player.getPlayer().isOnline()) {
                player.getPlayer().setSmpTeam(null);
            }
        }
        if (leader.getPlayer() != null && leader.getPlayer().isOnline()) {
            leader.getPlayer().setSmpTeam(null);
        }

        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            for (SMPPlayer pl : members) {
                if (pl.getPlayer() != null && pl.getPlayer().isOnline()) {
                    player.updateNametag(pl.getPlayer());
                }
            }
            if (leader.getPlayer() != null && leader.getPlayer().isOnline()) {
                player.updateNametag(leader.getPlayer());
            }
        }
    }

    public boolean isOffline() {
        for (SMPPlayer member : members) {
            if (member.getPlayer().isOnline()) {
                return false;
            }
        }
        return true;
    }

}
