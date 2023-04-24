/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.api;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.ChatSlowLength;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.utils.DiscordWebhook;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.AuroraMC;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.backend.ProxyDatabaseManager;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.player.party.Party;
import net.auroramc.proxy.api.player.party.PartyInvite;
import net.auroramc.proxy.api.utils.ProxySettings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProxyAPI {

    private static AuroraMC core;
    private static Map<ProxiedPlayer, AuroraMCProxyPlayer> players;
    private static final HashMap<UUID, Party> parties;
    private static ProxySettings proxySettings;

    private static Map<String, net.md_5.bungee.api.config.ServerInfo> servers;
    private static final Map<String, ServerInfo> amcServers;
    private static final List<ServerInfo> lobbyServers;

    private static boolean scheduledForShutdown;
    private static ScheduledTask shutdownTask;

    private static ScheduledTask playerCountTask;
    private static int playerCount;
    private static ScheduledTask silenceTask;

    static {
        players = new HashMap<>();
        amcServers = new HashMap<>();
        lobbyServers = new ArrayList<>();
        parties = new HashMap<>();
        scheduledForShutdown = false;
        shutdownTask = null;
    }

    public static void init(AuroraMC core) {
        AuroraMCAPI.init(core.getLogger(), new ProxyAbstractedMethods(), core.getConfig().getString("mysqlhost"), core.getConfig().getString("mysqlport"), core.getConfig().getString("mysqldb"), core.getConfig().getString("mysqlusername"), core.getConfig().getString("mysqlpassword"), core.getConfig().getString("uuid"), core.getConfig().getString("network"), core.getConfig().getString("redishost"), core.getConfig().getString("redisauth"),true);
        ProxyAPI.core = core;
        players = new HashMap<>();

        playerCountTask = ProxyServer.getInstance().getScheduler().schedule(core, () -> {
            playerCount = AuroraMCAPI.getDbManager().getPlayerCount();
        }, 1, 1, TimeUnit.SECONDS);
        loadProxyInfo();
        CommunicationUtils.init();
        loadServers();
    }

    public static void loadProxyInfo() {
        proxySettings = ProxyDatabaseManager.getProxySettings(AuroraMCAPI.getInfo().getNetwork());
    }

    public static void newPlayer(ProxiedPlayer proxy, AuroraMCProxyPlayer player) {
        players.put(proxy, player);
    }

    public static void playerLeave(AuroraMCProxyPlayer player) {
        player.endSession();
        player.stopTimers();
        if (player.getParty() != null) {
            player.getParty().leave(player.getPartyPlayer(), false);
        }
        for (PartyInvite invite : player.getPartyInvites()) {
            invite.getParty().requestDenied(invite);
        }
        Rank rank = player.getRank();
        switch (rank.getId()) {
            case 6:
            case 5: {
                DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/910317304154300466/SDREjU5XRcHowdX5EhiRxiFnvngixkNiqYtgN5X785ZPHwV2IRhSm7aoAZGXTu5sHbOc");
                webhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Media Rank Leave Alert").setDescription(String.format("**«%s»** %s has left the network.", rank.getPrefixAppearance().toUpperCase(), player.getName())).setColor(((rank.getId() == 5) ? new Color(255, 170, 0) : new Color(170, 0, 170))));
                webhook.setUsername("AuroraMC Media Leave Notifications");
                try {
                    webhook.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ProtocolMessage message = new ProtocolMessage(Protocol.MEDIA_RANK_JOIN_LEAVE, "Mission Control", "leave", player.getName(), rank.name() + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
                break;
            }
            case 9:
            case 10:
                String url;
                if (rank.getId() == 9) {
                    url = "https://discord.com/api/webhooks/892873021180764230/RqroaH2riV8YiqJ8vWQwzY1U9ur6OwNiIv0SmWMleZPS_vrjLxij_zylgkcP3Ztfe7AX";
                    AuroraMCAPI.getDbManager().jrModLeave(player.getName());
                } else {
                    url = "https://discord.com/api/webhooks/892872842650193941/GHuw85VsE20ukVVHXWlU2_I-ZsqZpL2bgWbOpQ0hCiDabs7pGOIyF0marHAI3PYdZlow";
                    AuroraMCAPI.getDbManager().modLeave(player.getName());
                }
                DiscordWebhook webhook = new DiscordWebhook(url);
                webhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Moderation Staff Leave Alert").setDescription(String.format("**«%s»** %s has left the network.", rank.getPrefixAppearance().toUpperCase(), player.getName())).setColor(new Color(85, 85, 255)));
                webhook.setUsername(rank.getName() + " Leave Notifications");
                try {
                    webhook.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ProtocolMessage message = new ProtocolMessage(Protocol.STAFF_RANK_JOIN_LEAVE, "Mission Control", "leave", player.getName(), rank.name() + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
                break;
        }
        for (ProxiedPlayer player1 : new HashSet<>(players.keySet())) {
            if (player1.getUniqueId().equals(player.getUniqueId())) {
                players.remove(player1);
                break;
            }
        }
        if (player.isLoaded()) {
            FriendsList list = player.getFriendsList();
            for (Friend friend : list.getFriends().values()) {
                ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(friend.getUuid());
                if (proxiedPlayer != null) {
                    getPlayer(proxiedPlayer).getFriendsList().getFriends().get(player.getUniqueId()).loggedOff(true);
                } else {
                    ProxyServer.getInstance().getScheduler().runAsync(core, () -> {
                        if (AuroraMCAPI.getDbManager().hasActiveSession(friend.getUuid())) {
                            UUID proxy = AuroraMCAPI.getDbManager().getProxy(friend.getUuid());
                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "loggedoff", player.getUniqueId().toString(), friend.getUuid().toString());
                            CommunicationUtils.sendMessage(message);
                        }
                    });
                }
            }
        }
        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name());
        CommunicationUtils.sendMessage(message);

    }

    public static AuroraMCProxyPlayer getPlayer(ProxiedPlayer player) {
        return players.get(player);
    }

    public static AuroraMCProxyPlayer getPlayer(String name) {
        for (AuroraMCProxyPlayer player : players.values()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public static AuroraMCProxyPlayer getDisguisedPlayer(String name) {
        for (AuroraMCProxyPlayer player : players.values()) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getName().equalsIgnoreCase(name)) {
                    return player;
                }
            }
        }
        return null;
    }

    public static AuroraMCProxyPlayer getPlayer(UUID uuid) {
        for (AuroraMCProxyPlayer player : players.values()) {
            if (player.getUniqueId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public static AuroraMCProxyPlayer getPlayer(int id) {
        for (AuroraMCProxyPlayer player : players.values()) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public static ArrayList<AuroraMCProxyPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public static boolean isMaintenance() {
        return proxySettings.isMaintenance();
    }

    public static void loadServers() {
        //Unregister all default registered servers.
        servers = core.getProxy().getServers();
        amcServers.clear();

        List<String> serverPriority = new ArrayList<>(ProxyServer.getInstance().getConfig().getListeners()).get(0).getServerPriority();
        serverPriority.clear();

        servers.clear();

        for (ServerInfo info : AuroraMCAPI.getDbManager().getServers()) {
            amcServers.put(info.getName(), info);
            servers.put(info.getName(), ProxyServer.getInstance().constructServerInfo(info.getName(), new InetSocketAddress(info.getIp(), info.getPort()), "", false));
            if (info.getServerType().get("type").equals("lobby")) {
                core.getLogger().info("Registered Lobby Server - " + info.getName() + " - IP: " + info.getIp() + ":" + info.getPort());
                lobbyServers.add(info);
                serverPriority.add(info.getName());
                continue;
            }
            core.getLogger().info("Registered Server - " + info.getName());
        }
    }

    public static Map<String, ServerInfo> getAmcServers() {
        return amcServers;
    }

    public static List<ServerInfo> getLobbyServers() {
        return lobbyServers;
    }

    public static void setChatSlow(short chatSlow) {
        AuroraMCAPI.setChatSlow(chatSlow);
        if (chatSlow == -1) {
            for (AuroraMCProxyPlayer player2 : getPlayers()) {
                player2.sendMessage(TextFormatter.pluginMessage("ChatSlow", "The active global chat slow has been disabled."));
            }
        } else {
            ChatSlowLength length = new ChatSlowLength(chatSlow);
            for (AuroraMCProxyPlayer player2 : getPlayers()) {
                if (player2.hasPermission("moderation") || player2.hasPermission("social") ||  player2.hasPermission("debug.info")) {
                    player2.sendMessage(TextFormatter.pluginMessage("ChatSlow", String.format("A global chat slow of **%s** was enabled across the network.", length.getFormatted())));
                } else {
                    player2.sendMessage(TextFormatter.pluginMessage("ChatSlow", String.format("The Administration Team has enabled a chat slow network-wide. You may now only chat every **%s** in any server.", length.getFormatted())));
                }
            }
        }
    }

    public static void enableChatSilence(short seconds, boolean sendMessage) {
        if (silenceTask != null) {
            silenceTask.cancel();
        }
        if (seconds != -1) {
            AuroraMCAPI.enableChatSilence(seconds);
            silenceTask = core.getProxy().getScheduler().schedule(core, () -> {
                if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                    disableSilence();
                }
            }, seconds, TimeUnit.SECONDS);
        }

        ChatSlowLength length = new ChatSlowLength(seconds);
        if (sendMessage) {
            for (AuroraMCProxyPlayer player : getPlayers()) {
                if (player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info")) {
                    player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("The chat has been silenced across the entire network for **%s**.", length.getFormatted())));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Silence", String.format("The chat has been silenced across the entire network for **%s**.", length.getFormatted())));
                }
            }
        }
    }

    public static void disableSilence() {
        AuroraMCAPI.disableSilence();
        for (AuroraMCProxyPlayer player : getPlayers()) {
            player.sendMessage(TextFormatter.pluginMessage("Silence", "Global Chat is no longer silenced."));
        }
        if (silenceTask != null) {
            ScheduledTask task = silenceTask;
            silenceTask = null;
            task.cancel();
        }
    }

    public static HashMap<UUID, Party> getParties() {
        return parties;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static boolean isScheduledForShutdown() {
        return scheduledForShutdown;
    }

    public static Map<String, net.md_5.bungee.api.config.ServerInfo> getServers() {
        return servers;
    }

    public static AuroraMC getCore() {
        return core;
    }

    public static ProxySettings getProxySettings() {
        return proxySettings;
    }

    public static void scheduleShutdown(String command) {
        scheduledForShutdown = true;
        if (players.size() > 0) {
            shutdownTask = ProxyServer.getInstance().getScheduler().schedule(core, new Runnable() {
                private int i = 5;

                @Override
                public void run() {
                    if (i == 0 || players.size() == 0) {
                        shutdownNow(command);
                        return;
                    }
                    for (AuroraMCProxyPlayer player : new ArrayList<>(players.values())) {

                        TextComponent component = new TextComponent("");

                        TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                        lines.setBold(true);
                        lines.setColor(ChatColor.DARK_RED);

                        component.addExtra(lines);
                        component.addExtra("\n                     ");

                        TextComponent cmp = new TextComponent("«MISSION CONTROL»\n \n");
                        cmp.setColor(ChatColor.RED);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent("The connection node that you are connected to is\n" +
                                String.format("scheduled to restart in &c&l%s minute%s&r!\n", i, ((i > 1)?"s":"")) +
                                "\n" +
                                "Please reconnect to the network to continue playing\n" +
                                "without any interruptions!\n \n");
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(false);
                        component.addExtra(cmp);
                        component.addExtra(lines);
                        player.sendMessage(lines);
                    }
                    i--;
                }
            }, 0, 1, TimeUnit.MINUTES);
        } else {
            CommunicationUtils.sendMessage(new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", command, AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name()));
        }
    }

    public static void shutdownNow(String command) {
        scheduledForShutdown = true;
        for (AuroraMCProxyPlayer player : new ArrayList<>(players.values())) {
            TextComponent component = new TextComponent("");


            TextComponent cmp = new TextComponent("«MISSION CONTROL»\n \n");
            cmp.setColor(ChatColor.RED);
            cmp.setBold(true);
            component.addExtra(cmp);

            cmp = new TextComponent("The connection node you were connected to has\n" +
                    "been restarted.\n" +
                    "\n" +
                    "You can reconnect to the network to continue playing!");
            cmp.setColor(ChatColor.WHITE);
            cmp.setBold(false);
            component.addExtra(cmp);

            player.disconnect(component);
        }
        CommunicationUtils.sendMessage(new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", command, AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name()));
        if (shutdownTask != null) {
            shutdownTask.cancel();
        }
    }
}
