/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.DisguiseFactory;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.backend.info.Info;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentHistory;
import net.auroramc.api.stats.PlayerBank;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.Pronoun;
import net.auroramc.api.utils.TextFormatter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AuroraMCPlayer {

    private int id;
    private String name;
    private Rank rank;
    private ArrayList<SubRank> subranks;
    private Disguise activeDisguise;
    private PlusSubscription activeSubscription;
    private String linkedDiscord;
    private boolean discordCodeGenerated;
    private PunishmentHistory history;
    private List<Punishment> activeMutes;
    private PlayerStatistics statistics;
    private PlayerBank bank;
    private UUID partyUUID;
    private long lastMessageSent;
    private boolean newPlayer;

    private Team team;

    private FriendsList friendsList;
    private ChatChannel channel;
    private PlayerPreferences preferences;
    private List<IgnoredPlayer> ignoredPlayers;

    //Staff objects
    private PlayerReport activeReport;

    //Cosmetic objects
    private List<Cosmetic> unlockedCosmetics;
    private HashMap<Cosmetic.CosmeticType, Cosmetic> activeCosmetics;

    private UUID uuid;

    //Just a variable so other systems knows when a player has been fully loaded.
    private boolean loaded;

    public AuroraMCPlayer(UUID uuid, String name, Object playerObject) {
        this.uuid = uuid;
        loaded = false;
        lastMessageSent = -1;
        team = null;
        this.name = name;

        loadBefore(playerObject);

        Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(this, uuid);
        if (disguise != null) {
            activeDisguise = disguise;
            if (!AuroraMCAPI.getDbManager().isHideDisguiseName(uuid)) {
                activeDisguise.apply(false);
            }
        }



        ScheduleFactory.scheduleAsync(() -> {
            int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
            if (id == -1) {
                //
                id = AuroraMCAPI.getDbManager().newUser(AuroraMCPlayer.this);
                newPlayer = true;
            }
            AuroraMCPlayer.this.id = id;
            AuroraMCAPI.getDbManager().setLastSeen(id, System.currentTimeMillis());
            //Get Punishment History and active mutes.
            List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(id);
            history = new PunishmentHistory(id);
            activeMutes = new ArrayList<>();

            for (Punishment punishment : punishments) {
                history.registerPunishment(punishment);
                if (punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) {
                    //This is an active punishment.
                    if (punishment.getExpire() == -1) {
                        //This is a permanent mute
                        activeMutes.add(punishment);
                    } else if (punishment.getExpire() > System.currentTimeMillis()) {
                        //Active mute. Add to the scheduler.
                        activeMutes.add(punishment);
                    } else {
                        //Expire this punishment.
                        AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                    }
                }
            }

            preferences = AuroraMCAPI.getDbManager().getPlayerPreferences(AuroraMCPlayer.this);
            rank = AuroraMCAPI.getDbManager().getRank(AuroraMCPlayer.this);
            if (rank.hasPermission("all")) {
                activeSubscription = new PlusSubscription(AuroraMCPlayer.this);
            }  else {
                long endTimestamp = AuroraMCAPI.getDbManager().getExpire(AuroraMCPlayer.this);
                if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
                    activeSubscription = new PlusSubscription(AuroraMCPlayer.this);
                }
            }

            subranks = AuroraMCAPI.getDbManager().getSubRanks(AuroraMCPlayer.this);

            //Load the friends list.
            friendsList = AuroraMCAPI.getDbManager().getFriendsList(AuroraMCPlayer.this);

            linkedDiscord = AuroraMCAPI.getDbManager().getDiscord(id);

            statistics = AuroraMCAPI.getDbManager().getStatistics(AuroraMCPlayer.this);

            if (friendsList.getFriends().values().stream().anyMatch(friend -> friend.getType() == Friend.FriendType.NORMAL || friend.getType() == Friend.FriendType.FAVOURITE)) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(30))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(30), 1, true);
                }
            }

            if (statistics.getLevel() >= 200) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(60))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(60), 1, true);
                }
            }
            if (statistics.getLevel() >= 150) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(59))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(59), 1, true);
                }
            }
            if (statistics.getLevel() >= 125) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(58))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(58), 1, true);
                }
            }
            if (statistics.getLevel() >= 100) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(57))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(57), 1, true);
                }
            }
            if (statistics.getLevel() >= 80) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(56))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(56), 1, true);
                }
            }
            if (statistics.getLevel() >= 60) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(55))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(55), 1, true);
                }
            }
            if (statistics.getLevel() >= 40) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(54))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(54), 1, true);
                }
            }
            if (statistics.getLevel() >= 25) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(53))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(53), 1, true);
                }
            }
            if (statistics.getLevel() >= 10) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(52))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(52), 1, true);
                }
            }
            if (statistics.getLevel() >= 1) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(51))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(51), 1, true);
                }
            }

            bank = AuroraMCAPI.getDbManager().getBank(AuroraMCPlayer.this);

            channel = AuroraMCAPI.getDbManager().getChannel(AuroraMCPlayer.this);

            activeReport = AuroraMCAPI.getDbManager().getActiveReport(id);


            List<PlayerReport> reports = AuroraMCAPI.getDbManager().getSubmittedReports(id).stream().filter(report -> report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED).collect(Collectors.toList());
            if (reports.size() > 0) {
                if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(10))) {
                    statistics.achievementGained(AuroraMCAPI.getAchievement(10), 1, true);
                }
                if (reports.size() >= 100) {
                    if (!statistics.getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(18))) {
                        statistics.achievementGained(AuroraMCAPI.getAchievement(18), 1, true);
                    }
                }
            }

            ignoredPlayers = AuroraMCAPI.getDbManager().getIgnoredPlayers(id);

            unlockedCosmetics = AuroraMCAPI.getDbManager().getUnlockedCosmetics(uuid);
            activeCosmetics = AuroraMCAPI.getDbManager().getActiveCosmetics(uuid);

            //If they have a rank-exclusive status set, check if they still have permission to use it.
            if (!friendsList.getCurrentStatus().hasUnlocked(AuroraMCPlayer.this)) {
                //They no longer have permission, default to Online.
                friendsList.setCurrentStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(101), true);
            }

            if (AuroraMCAPI.isCosmeticsEnabled()) {
                for (Cosmetic cosmetic : activeCosmetics.values()) {
                    ScheduleFactory.scheduleSync(() -> cosmetic.onEquip(AuroraMCPlayer.this));
                }
            } else {
                activeCosmetics.values().stream().filter(Cosmetic::shouldBypassDisabled).forEach((cosmetic) -> cosmetic.onEquip(AuroraMCPlayer.this));
            }

            if (disguise != null && preferences.isHideDisguiseNameEnabled()) {
                ScheduleFactory.scheduleSync(() -> AuroraMCPlayer.this.applyDisguise(true));
            }
            //To ensure that this is being called after everything has been retrived, it is called here and then replaces the object already in the cache.
            if (!isOnline()) {
                return;
            }
            loadExtra();
        });
    }

    public AuroraMCPlayer(AuroraMCPlayer oldPlayer) {
        uuid = oldPlayer.uuid;
        id = oldPlayer.getId();
        name = oldPlayer.getName();
        rank = oldPlayer.getRank();
        subranks = oldPlayer.getSubranks();
        activeDisguise = oldPlayer.getActiveDisguise();
        if (activeDisguise != null) {
            activeDisguise.setPlayer(this);
        }
        activeSubscription = oldPlayer.getActiveSubscription();
        if (activeSubscription != null) {
            activeSubscription.setPlayer(this);
        }
        linkedDiscord = oldPlayer.getLinkedDiscord();
        discordCodeGenerated = oldPlayer.discordCodeGenerated;
        history = oldPlayer.history;
        activeMutes = oldPlayer.getActiveMutes();
        statistics = oldPlayer.statistics;
        statistics.setPlayer(this);
        bank = oldPlayer.bank;
        bank.setPlayer(this);
        friendsList = oldPlayer.friendsList;
        friendsList.setPlayer(this);
        channel = oldPlayer.channel;
        partyUUID = oldPlayer.partyUUID;
        preferences = oldPlayer.preferences;
        preferences.setPlayer(this);
        activeReport = oldPlayer.activeReport;
        ignoredPlayers = oldPlayer.ignoredPlayers;
        lastMessageSent = oldPlayer.lastMessageSent;
        unlockedCosmetics = oldPlayer.unlockedCosmetics;
        activeCosmetics = oldPlayer.activeCosmetics;
        loaded = oldPlayer.loaded;
        team = oldPlayer.team;
        newPlayer = oldPlayer.newPlayer;
    }

    public UUID getUuid() {
        return uuid;
    }

    public abstract void loadBefore(Object playerObject);
    public abstract void loadExtra();

    public abstract boolean isOnline();

    public Rank getRank() {
        return rank;
    }

    public Rank getByDisguiserank() {
        return ((activeDisguise == null)?rank:activeDisguise.getRank());
    }

    public boolean isDisguised() {return activeDisguise != null;}

    public Disguise getActiveDisguise() {
        return activeDisguise;
    }

    public ArrayList<SubRank> getSubranks() {
        return new ArrayList<>(subranks);
    }

    public PlusSubscription getActiveSubscription() {
        return activeSubscription;
    }

    protected void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getName() {
        return name;
    }

    public boolean isNewPlayer() {
        return newPlayer;
    }

    public String getByDisguiseName() {
        return ((activeDisguise == null)?name:activeDisguise.getName());
    }

    public boolean disguise(String skin, String name, Rank rank) {
        if (this.activeDisguise != null) {
            activeDisguise.switchDisguise();
        }

        activeDisguise = DisguiseFactory.newDisguise(this, name, skin, rank);
        return activeDisguise.apply(true);
    }

    public Disguise randomDisguise() {
        if (this.activeDisguise != null) {
            activeDisguise.switchDisguise();
        }

        activeDisguise = Disguise.randomDisguise(this);
        activeDisguise.apply(true);
        return activeDisguise;
    }

    public boolean applyDisguise(boolean update) {
        if (activeDisguise != null) {
            return activeDisguise.apply(update);
        }
        return true;
    }

    public boolean undisguise(boolean save) {
        if (activeDisguise != null) {
            Disguise activeDisguise = this.activeDisguise;
            this.activeDisguise = null;
            AuroraMCPlayer pl = this;
            if (save) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().undisguise(pl, activeDisguise));
            }
            return activeDisguise.undisguise();
        }
        return true;
    }

    public boolean hasPermission(String string) {
        if (rank.hasPermission(string)) {
            return true;
        }
        if (subranks != null) {
            for (SubRank subRank : subranks) {
                if (subRank != null) {
                    if (subRank.hasPermission(string)) {
                        return true;
                    }
                }
            }
        }

        if (activeSubscription != null) {
            return activeSubscription.getPermission().getNode().equalsIgnoreCase(string);
        }

        return false;
    }

    public boolean hasPermission(int id) {
        if (rank.hasPermission(id)) {
            return true;
        }
        if (subranks != null) {
            for (SubRank subRank : subranks) {
                if (subRank != null) {
                    if (subRank.hasPermission(id)) {
                        return true;
                    }
                }
            }
        }

        if (activeSubscription != null) {
            return activeSubscription.getPermission().getId() == id;
        }

        return false;
    }

    public int getId() {
        return id;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void grantSubrank(SubRank rank) {
        if (this.subranks == null) {
            this.subranks = new ArrayList<>();
        }

        this.subranks.add(rank);
    }

    public void revokeSubrank(SubRank rank) {
        if (this.subranks == null) {
            this.subranks = new ArrayList<>();
        } else {
            this.subranks.remove(rank);
        }
    }

    public String getLinkedDiscord() {
        return linkedDiscord;
    }

    public boolean isDiscordCodeGenerated() {
        return discordCodeGenerated;
    }

    public void codeGenerated() {
        discordCodeGenerated = true;
        ScheduleFactory.scheduleAsync(() -> discordCodeGenerated = false);
    }

    public ArrayList<Punishment> getActiveMutes() {
        return new ArrayList<>(activeMutes);
    }

    public void applyMute(Punishment punishment) {
        history.registerPunishment(punishment);
        if (punishment.getStatus() == 1 || punishment.getStatus() == 2 || punishment.getStatus() == 3) {
            //This is an active punishment.
            if (punishment.getExpire() == -1) {
                //This is a permanent mute
                activeMutes.add(punishment);
            } else if (punishment.getExpire() > System.currentTimeMillis()) {
                //Active mute. Add to the scheduler.
                activeMutes.add(punishment);
            } else {
                //Expire this punishment.
                AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
            }
        }
    }

    public void removeMute(Punishment mute) {
        this.activeMutes.remove(mute);
    }

    public PlayerStatistics getStats() {
        return statistics;
    }

    public PlayerBank getBank() {
        return bank;
    }

    public void expireSubscription() {
        PlusSubscription subscription = activeSubscription;
        activeSubscription = null;
    }

    public void newSubscription() {
        AuroraMCPlayer pl = this;

        ScheduleFactory.scheduleAsync(() -> {
            if (rank.hasPermission("all")) {
                activeSubscription = new PlusSubscription(pl);
            } else {
                long endTimestamp = AuroraMCAPI.getDbManager().getExpire(pl);
                if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
                    activeSubscription = new PlusSubscription(pl);
                }
            }
        });
    }

    public FriendsList getFriendsList() {
        return friendsList;
    }

    public void setChannel(ChatChannel channel, boolean send) {
        this.channel = channel;

        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ChatChannelSet");
            out.writeUTF(getName());
            out.writeUTF(channel.name());
            sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> {
                AuroraMCAPI.getDbManager().channelSet(AuroraMCPlayer.this, channel);
            });
        }
    }

    public ChatChannel getChannel() {
        return channel;
    }

    public UUID getPartyUUID() {
        return partyUUID;
    }

    public void setPartyLeader(UUID partyUUID) {
        this.partyUUID = partyUUID;
    }

    public PlayerPreferences getPreferences() {
        return preferences;
    }

    public PlayerReport getActiveReport() {
        return activeReport;
    }

    public void setActiveReport(PlayerReport activeReport) {
        this.activeReport = activeReport;
    }

    public List<IgnoredPlayer> getIgnoredPlayers() {
        return ignoredPlayers;
    }

    public boolean isIgnored(int id) {
        for (IgnoredPlayer player : ignoredPlayers) {
            if (player.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isIgnored(String name) {
        for (IgnoredPlayer player : ignoredPlayers) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public IgnoredPlayer getIgnored(int id) {
        for (IgnoredPlayer player : ignoredPlayers) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public IgnoredPlayer getIgnored(String name) {
        for (IgnoredPlayer player : ignoredPlayers) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public void addIgnored(IgnoredPlayer user, boolean send) {
        ignoredPlayers.add(user);

        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("IgnoreAdded");
            out.writeUTF(name);
            out.writeInt(user.getId());
            out.writeUTF(user.getName());
            out.writeUTF(user.getName());
            sendPluginMessage(out.toByteArray());

            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setIgnoredPlayers(id, ignoredPlayers));
        }
    }

    public void removeIgnored(IgnoredPlayer user, boolean send) {
        ignoredPlayers.remove(user);

        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("IgnoreRemoved");
            out.writeUTF(name);
            out.writeInt(user.getId());
            sendPluginMessage(out.toByteArray());
            if (!AuroraMCAPI.isTestServer()) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setIgnoredPlayers(id, ignoredPlayers));
            }
        }
    }

    public long getLastMessageSent() {
        return lastMessageSent;
    }

    public void messageSent() {
        lastMessageSent = System.currentTimeMillis();
    }

    public abstract void sendPluginMessage(byte[] byteArray);

    public HashMap<Cosmetic.CosmeticType, Cosmetic> getActiveCosmetics() {
        return activeCosmetics;
    }

    public List<Cosmetic> getUnlockedCosmetics() {
        return unlockedCosmetics;
    }

    public abstract void sendTitle(BaseComponent title, BaseComponent subtitle, int fadeInTime, int showTime, int fadeOutTime);

    public abstract void sendHotBar(BaseComponent message);

    public abstract void sendMessage(BaseComponent message);

    public boolean isLoaded() {
        return loaded;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public abstract UUID getUniqueId();

    public PunishmentHistory getHistory() {
        return history;
    }
}
