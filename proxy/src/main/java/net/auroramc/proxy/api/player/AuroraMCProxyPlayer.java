/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.api.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.utils.DiscordWebhook;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.ProxyDatabaseManager;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.party.Party;
import net.auroramc.proxy.api.player.party.PartyInvite;
import net.auroramc.proxy.api.player.party.PartyPlayer;
import net.auroramc.api.backend.info.ServerInfo;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.awt.*;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class AuroraMCProxyPlayer extends AuroraMCPlayer {

    private ProxiedPlayer player;
    private Map<String, ScheduledTask> expiryTasks;
    private Session session;
    private UUID lastMessaged;

    private Party party;
    private PartyPlayer partyPlayer;
    private ScheduledTask partyTask;
    private final List<PartyInvite> partyInvites;


    public AuroraMCProxyPlayer(ProxiedPlayer player) {
        super(player.getUniqueId(), player.getName(), player);
        this.player = player;
        this.partyInvites = new ArrayList<>();
        this.expiryTasks = new HashMap<>();
    }

    @Override
    public void loadBefore(Object playerObject) {

    }

    @Override
    public void loadExtra() {
        this.session = new Session(this);
        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
            switch (getRank().getId()) {
                case 6:
                case 5: {
                    DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/910317304154300466/SDREjU5XRcHowdX5EhiRxiFnvngixkNiqYtgN5X785ZPHwV2IRhSm7aoAZGXTu5sHbOc");
                    webhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Media Rank Login Alert").setDescription(String.format("**«%s»** %s has joined the network.", getRank().getPrefixAppearance().toUpperCase(), getName())).setColor(((getRank().getId() == 5)?new Color(255, 170, 0):new Color(170, 0, 170))));
                    try {
                        webhook.execute();
                    } catch (IOException e) {
                        AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                    }
                    ProtocolMessage message = new ProtocolMessage(Protocol.MEDIA_RANK_JOIN_LEAVE, "Mission Control", "join", getName(), getRank().name() + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                    CommunicationUtils.sendMessage(message);
                    break;
                }
                case 9:
                case 10:
                    String url;
                    if (getRank().getId() == 9) {
                        url = "https://discord.com/api/webhooks/892873021180764230/RqroaH2riV8YiqJ8vWQwzY1U9ur6OwNiIv0SmWMleZPS_vrjLxij_zylgkcP3Ztfe7AX";
                        AuroraMCAPI.getDbManager().jrModJoin(getName());
                    } else {
                        url = "https://discord.com/api/webhooks/892872842650193941/GHuw85VsE20ukVVHXWlU2_I-ZsqZpL2bgWbOpQ0hCiDabs7pGOIyF0marHAI3PYdZlow";
                        AuroraMCAPI.getDbManager().modJoin(getName());
                    }
                    DiscordWebhook  webhook = new DiscordWebhook(url);
                    webhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Moderation Staff Login Alert").setDescription(String.format("**«%s»** %s has joined the network.", getRank().getPrefixAppearance().toUpperCase(), getName())).setColor(new Color(85, 85, 255)));
                    webhook.setUsername(getRank().getName() + " Join Notifications");
                    try {
                        webhook.execute();
                    } catch (IOException e) {
                        AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
                    }
                    ProtocolMessage message = new ProtocolMessage(Protocol.STAFF_RANK_JOIN_LEAVE, "Mission Control", "join", getName(), getRank().name() + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                    CommunicationUtils.sendMessage(message);
                    break;
            }
        });

        for (Punishment punishment : getHistory().getPunishments()) {
            if (punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) {
                //This is an active punishment.
                if (punishment.getExpire() > System.currentTimeMillis()) {
                    //Active mute. Add to the scheduler.
                    ScheduledTask task = ProxyAPI.getCore().getProxy().getScheduler().schedule(ProxyAPI.getCore(), () -> {
                        AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                        removeMute(punishment);
                        expiryTasks.remove(punishment.getPunishmentCode());
                    }, punishment.getExpire() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    expiryTasks.put(punishment.getPunishmentCode(), task);
                }
            }
        }

        for (Friend friend : getFriendsList().getFriends().values()) {
            ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(friend.getUuid());
            if (proxiedPlayer != null) {
                //This player is online, load their settings and update this player for them.
                AuroraMCProxyPlayer auroraMCPlayer = ProxyAPI.getPlayer(proxiedPlayer);
                if (auroraMCPlayer != null) {
                    if (auroraMCPlayer.getFriendsList() != null) {
                        switch (auroraMCPlayer.getFriendsList().getVisibilityMode()) {
                            case FAVOURITE_FRIENDS_ONLY:
                                if (auroraMCPlayer.getFriendsList().getFriends().get(player.getUniqueId()).getType() != Friend.FriendType.FAVOURITE) {
                                    friend.loggedOn(null, auroraMCPlayer.getFriendsList().getCurrentStatus(), false);
                                    break;
                                }
                            case ALL:
                                if (auroraMCPlayer.getFriendsList().getCurrentStatus().equals(AuroraMCAPI.getCosmetics().get(101))) {
                                    friend.loggedOn(null, (FriendStatus) AuroraMCAPI.getCosmetics().get(101), false);
                                    break;
                                }
                                try {
                                    friend.loggedOn(auroraMCPlayer.getServer().getName(), auroraMCPlayer.getFriendsList().getCurrentStatus(), false);
                                } catch (Exception ignored){
                                    //Send a null server, means a server has yet to be connected to, and will update when they have actually connected to the server so no need to worry about it here.
                                    friend.loggedOn(null, auroraMCPlayer.getFriendsList().getCurrentStatus(), false);
                                }
                                break;
                            default:
                                friend.loggedOn(null, auroraMCPlayer.getFriendsList().getCurrentStatus(), false);
                                break;
                        }
                    }
                }
            } else {
                if (AuroraMCAPI.getDbManager().hasActiveSession(friend.getUuid())) {
                    UUID proxy = AuroraMCAPI.getDbManager().getProxy(friend.getUuid());
                    ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "loggedon", player.getUniqueId().toString(), friend.getUuid().toString());
                    CommunicationUtils.sendMessage(message);
                    FriendsList.VisibilityMode mode = AuroraMCAPI.getDbManager().getVisibilityMode(friend.getUuid());
                    FriendStatus status1 = AuroraMCAPI.getDbManager().getFriendStatus(friend.getUuid());
                    String server = AuroraMCAPI.getDbManager().getCurrentServer(friend.getUuid());
                    switch (mode) {
                        case FAVOURITE_FRIENDS_ONLY:

                            if (!AuroraMCAPI.getDbManager().isFavouriteFriend(friend.getAmcId(), this.getId())) {
                                friend.loggedOn(null, status1, false);
                                break;
                            }
                        case ALL:
                            if (status1.equals(AuroraMCAPI.getCosmetics().get(101))) {
                                friend.loggedOn(null, (FriendStatus) AuroraMCAPI.getCosmetics().get(101), false);
                                break;
                            }
                            try {
                                friend.loggedOn(server, status1, false);
                            } catch (Exception ignored){
                                //Send a null server, means a server has yet to be connected to, and will update when they have actually connected to the server so no need to worry about it here.
                                friend.loggedOn(null, status1, false);
                            }
                            break;
                        default:
                            friend.loggedOn(null, status1, false);
                            break;
                    }
                }
            }
        }

        if (isNewPlayer()) {
            TextComponent component = new TextComponent("");
            TextComponent welcome = new TextComponent("Welcome to the AuroraMC Network!");
            welcome.setBold(true);
            welcome.setColor(ChatColor.AQUA);
            welcome.setUnderlined(true);

            component.addExtra(welcome);

            TextComponent joinUs = new TextComponent(" We're glad you've decided to join us!\n\nBy playing on AuroraMC, you confirm you will comply with our ");
            joinUs.setUnderlined(false);
            joinUs.setBold(false);
            joinUs.setColor(ChatColor.RESET);
            component.addExtra(joinUs);


            TextComponent rules = new TextComponent("Rules");
            rules.setColor(ChatColor.AQUA);
            rules.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append("Click here to view our rules!").color(ChatColor.AQUA).bold(true).create()));
            rules.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/rules"));
            component.addExtra(rules);

            component.addExtra(", ");

            TextComponent terms = new TextComponent("Terms of Service");
            terms.setColor(ChatColor.AQUA);
            terms.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append("Click here to view our Terms of Service!").color(ChatColor.AQUA).bold(true).create()));
            terms.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/terms"));
            component.addExtra(terms);

            component.addExtra(", and ");

            TextComponent privacy = new TextComponent("Privacy Policies");
            privacy.setColor(ChatColor.AQUA);
            privacy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append("Click here to view our Privacy Policy!").color(ChatColor.AQUA).bold(true).create()));
            privacy.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auroramc.net/privacy"));
            component.addExtra(privacy);

            component.addExtra(". For more information on our policies, please click on the applicable text above.\n\n" +
                    "To get started, click on the compass and choose a game!\n\n" +
                    "Happy Gaming!\n");

            TextComponent leadership = new TextComponent("~The AuroraMC Leadership Team");
            leadership.setColor(ChatColor.RED);
            leadership.setBold(true);
            leadership.setUnderlined(true);
            component.addExtra(leadership);

            player.sendMessage(component);
            Title title = ProxyServer.getInstance().createTitle();
            title.fadeIn(10);
            title.stay(100);
            title.fadeOut(10);

            title.title(new ComponentBuilder("WELCOME TO AURORAMC!").bold(true).color(ChatColor.AQUA).create());
            title.subTitle(new TextComponent("We're glad you've decided to join us!"));
            player.sendTitle(title);
        } else {
            Rank rank = AuroraMCAPI.getDbManager().getPreloadMessage(getUniqueId());
            if (rank != null) {
                AuroraMCAPI.getDbManager().removePreloadMessage(getUniqueId());
                TextComponent component = new TextComponent("");
                TextComponent welcome = new TextComponent("Hello " + player.getName() + ", Welcome to the AuroraMC Network!");
                welcome.setBold(true);
                welcome.setColor(ChatColor.AQUA);
                welcome.setUnderlined(true);

                component.addExtra(welcome);
                TextComponent joinUs = new TextComponent(" We're glad you've decided to join us!\n\nThe AuroraMC Leadership Team have decided to pre-load your account with the ");
                joinUs.setUnderlined(false);
                joinUs.setBold(false);
                joinUs.setColor(ChatColor.RESET);
                component.addExtra(joinUs);

                TextComponent rankName = new TextComponent(rank.getName());
                rankName.setColor(rank.getPrefixColor());
                rankName.setBold(true);
                component.addExtra(rankName);

                TextComponent extraStuff = new TextComponent(" Rank so your permissions are ready to go, and you can get on with playing games!\n" +
                        "If you need any help, please don't hesitate to contact one of us on Discord. If you link your Minecraft to Discord, you will automatically get invited to the Social Media discord where you can contact the Leadership Team directly." +
                        "\n\nTo get started, click on the compass and choose a game!\n\nHappy Gaming!");
                extraStuff.setUnderlined(false);
                extraStuff.setBold(false);
                extraStuff.setColor(ChatColor.RESET);
                component.addExtra(extraStuff);

            }
        }

        //Update the user's Name and IP profiles.
        ProxyDatabaseManager.updateProfiles(player, getId());
    }

    @Override
    public boolean isOnline() {
        return player.isConnected();
    }

    @Override
    public void sendPluginMessage(byte[] byteArray) {
        player.getServer().sendData("auroramc:server", byteArray);
    }

    @Override
    public void sendTitle(BaseComponent title2, BaseComponent subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        Title title = ProxyServer.getInstance().createTitle();
        title.fadeIn(fadeInTime);
        title.stay(showTime);
        title.fadeOut(fadeOutTime);
        title.title(title2);
        title.subTitle(subtitle);
        player.sendTitle(title);
    }

    public void sendTitle(Title title) {
        player.sendTitle(title);
    }

    @Override
    public void sendHotBar(BaseComponent message) {
        player.sendMessage(ChatMessageType.ACTION_BAR, message);
    }

    @Override
    public void sendMessage(BaseComponent message) {
        player.sendMessage(message);
    }

    public void connect(ServerInfo serverInfo) {
        player.connect(ProxyAPI.getServers().get(serverInfo.getName()));
    }

    @Override
    public void removeMute(Punishment mute) {
        super.removeMute(mute);
        Punishment oldMute = getHistory().getPunishment(mute.getPunishmentCode());
        if (oldMute != null) {
            if (expiryTasks.get(oldMute.getPunishmentCode()) != null) {
                expiryTasks.get(oldMute.getPunishmentCode()).cancel();
            }
            expiryTasks.remove(oldMute.getPunishmentCode());
            oldMute.setStatus(mute.getStatus());
        }
    }

    public void connect(ServerInfo serverInfo, ServerConnectEvent.Reason reason) {
        player.connect(ProxyAPI.getServers().get(serverInfo.getName()), reason);
    }

    public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {
        player.connect(ProxyAPI.getServers().get(serverInfo.getName()), callback);
    }

    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        player.connect(ProxyAPI.getServers().get(serverInfo.getName()), callback, reason);
    }

    public void connect(ServerConnectRequest serverConnectRequest) {
        player.connect(serverConnectRequest);
    }

    public ServerInfo getServer() {
        if (player.getServer() != null) {
            return ProxyAPI.getAmcServers().get(player.getServer().getInfo().getName());
        }
        return null;
    }

    public int getPing() {
        return player.getPing();
    }

    public PendingConnection getPendingConnection() {
        return player.getPendingConnection();
    }

    public void chat(String s) {
        player.chat(s);
    }

    public ServerInfo getReconnectServer() {
        return ProxyAPI.getAmcServers().get(player.getReconnectServer().getName());
    }

    public void setReconnectServer(ServerInfo serverInfo) {
        player.setReconnectServer(ProxyAPI.getServers().get(serverInfo.getName()));
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public Locale getLocale() {
        return player.getLocale();
    }

    public byte getViewDistance() {
        return player.getViewDistance();
    }

    public ProxiedPlayer.ChatMode getChatMode() {
        return player.getChatMode();
    }

    public boolean hasChatColors() {
        return player.hasChatColors();
    }

    public SkinConfiguration getSkinParts() {
        return player.getSkinParts();
    }

    public ProxiedPlayer.MainHand getMainHand() {
        return player.getMainHand();
    }

    public void setTabHeader(BaseComponent baseComponent, BaseComponent baseComponent1) {
        player.setTabHeader(baseComponent, baseComponent1);
    }

    public void setTabHeader(BaseComponent[] baseComponents, BaseComponent[] baseComponents1) {
        player.setTabHeader(baseComponents, baseComponents1);
    }

    public void resetTabHeader() {
        player.resetTabHeader();
    }

    public boolean isForgeUser() {
        return player.isForgeUser();
    }

    public Map<String, String> getModList() {
        return player.getModList();
    }

    public void updateServer(ServerInfo server) {
        if (session != null) {
            session.currentServer(server.getName());
        }
    }

    public void stopTimers() {
        if (expiryTasks != null) {
            for (ScheduledTask task : expiryTasks.values()) {
                task.cancel();
            }
        }
    }

    public void endSession() {
        if (session != null) {
            session.endSession();
        }
    }

    public UUID getLastMessaged() {
        return lastMessaged;
    }

    public void setLastMessaged(UUID lastMessaged) {
        this.lastMessaged = lastMessaged;
    }

    public List<PartyInvite> getPartyInvites() {
        return new ArrayList<>(partyInvites);
    }

    public Party getParty() {
        return party;
    }

    public void newInvite(PartyInvite invite) {
        this.partyInvites.add(invite);
        if (partyPlayer == null) {
            partyPlayer = invite.getPlayer();
        }
    }

    public void inviteAccepted(PartyInvite invite) {
        this.partyInvites.remove(invite);
        if (party != null) {
            party.leave(partyPlayer, false);
            if (partyTask != null) {
                partyTask.cancel();
            }
            if (!getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(41))) {
                getStats().achievementGained(AuroraMCAPI.getAchievement(41), 1, true);
            }
        }
        if (!getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(36))) {
            getStats().achievementGained(AuroraMCAPI.getAchievement(36), 1, true);
        }
        party = invite.getParty();
        if (party.getPlayers().size() >= 5) {
            if (!getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(38))) {
                getStats().achievementGained(AuroraMCAPI.getAchievement(38), 1, true);
            }
        }
        if (!getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(40))) {
            partyTask = ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), () -> {
                getStats().achievementGained(AuroraMCAPI.getAchievement(40), 1, true);
            }, 3, TimeUnit.HOURS);
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PartySet");
        out.writeUTF(player.getName());
        out.writeUTF(party.getUuid().toString());
        sendPluginMessage(out.toByteArray());
    }

    public void inviteDeclined(PartyInvite invite) {
        this.partyInvites.remove(invite);
    }

    public void leftParty(boolean sendToServer) {
        party = null;
        if (partyTask != null) {
            partyTask.cancel();
            partyTask = null;
        }
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PartySet");
            out.writeUTF(player.getName());
            out.writeUTF("null");
            sendPluginMessage(out.toByteArray());
        }
    }

    public void createdParty(PartyPlayer invitee) {
        if (!getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(35))) {
            getStats().achievementGained(AuroraMCAPI.getAchievement(35), 1, true);
        }
        if (partyPlayer == null) {
            partyPlayer = new PartyPlayer(getId(), getName(), player.getUniqueId(), this, UUID.fromString(AuroraMCAPI.getInfo().getName()), getRank());
        }
        if (!getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(40))) {
            partyTask = ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), () -> {
                getStats().achievementGained(AuroraMCAPI.getAchievement(40), 1, true);
            }, 3, TimeUnit.HOURS);
        }
        party = new Party(partyPlayer, invitee);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PartySet");
        out.writeUTF(player.getName());
        out.writeUTF(party.getUuid().toString());
        sendPluginMessage(out.toByteArray());
    }

    public PartyPlayer getPartyPlayer() {
        return partyPlayer;
    }

    @Override
    public void applyMute(Punishment punishment) {
        super.applyMute(punishment);
        if (punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) {
            //This is an active punishment.
            if (punishment.getExpire() > System.currentTimeMillis()) {
                //Active mute. Add to the scheduler.
                ScheduledTask task = ProxyAPI.getCore().getProxy().getScheduler().schedule(ProxyAPI.getCore(), () -> {
                    AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                    removeMute(punishment);
                    expiryTasks.remove(punishment.getPunishmentCode());
                }, punishment.getExpire() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                expiryTasks.put(punishment.getPunishmentCode(), task);
            }
        }
    }

    public void punishmentApproved(Punishment punishment) {
        Punishment oldMute = getHistory().getPunishment(punishment.getPunishmentCode());
        if (oldMute != null) {
            if (oldMute.getStatus() == 2) {
                oldMute.setStatus(3);
            } else {
                oldMute.setStatus(6);
            }
        }
    }

    public SocketAddress getSocketAddress() {
        return player.getSocketAddress();
    }

    public void disconnect(BaseComponent... baseComponents) {
        player.disconnect(baseComponents);
    }

    public void disconnect(BaseComponent baseComponent) {
        player.disconnect(baseComponent);
    }

    public Connection.Unsafe unsafe() {
        return player.unsafe();
    }
}
