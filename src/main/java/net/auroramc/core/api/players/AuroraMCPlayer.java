package net.auroramc.core.api.players;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.events.player.PlayerObjectCreationEvent;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.players.friends.FriendsList;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentHistory;
import net.auroramc.core.api.stats.PlayerBank;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.permissions.SubRank;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AuroraMCPlayer {

    private int id;
    private final Player player;
    private final String name;
    private Rank rank;
    private ArrayList<SubRank> subranks;
    private Disguise activeDisguise;
    private PlusSubscription activeSubscription;
    private Team team;
    private String linkedDiscord;
    private boolean discordCodeGenerated;
    private PunishmentHistory history;
    private List<Punishment> activeMutes;
    private List<BukkitTask> expiryTasks;
    private final PlayerScoreboard scoreboard;
    private boolean vanished;
    private PlayerStatistics statistics;
    private PlayerBank bank;
    private UUID partyUUID;
    private long lastMessageSent;

    private UUID lastAdminMessaged;

    private FriendsList friendsList;
    private ChatChannel channel;
    private PlayerPreferences preferences;
    private List<IgnoredPlayer> ignoredPlayers;

    //Staff objects
    private PlayerReport activeReport;
    private BukkitTask activeReportTask;

    //Cosmetic objects
    private List<Cosmetic> unlockedCosmetics;
    private HashMap<Cosmetic.CosmeticType, Cosmetic> activeCosmetics;
    private HashMap<Cosmetic, BukkitTask> runningCosmeticTasks;

    //Just a variable so other systems knows when a player has been fully loaded.
    private boolean loaded;


    public AuroraMCPlayer(Player player) {
        loaded = false;
        scoreboard = new PlayerScoreboard(this, Bukkit.getScoreboardManager().getNewScoreboard());
        AuroraMCPlayer pl = this;
        this.player = player;
        name = player.getName();

        lastMessageSent = -1;

        Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(pl);
        if (disguise != null) {
            activeDisguise = disguise;
            activeDisguise.apply(false);
        }

        this.vanished = AuroraMCAPI.getDbManager().isVanished(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                int id = AuroraMCAPI.getDbManager().getAuroraMCID(player.getUniqueId());
                if (id == -1) {
                    //
                    id = AuroraMCAPI.getDbManager().newUser(pl);
                }
                pl.id = id;
                //Get Punishment History and active mutes.
                List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(id);
                history = new PunishmentHistory(id);
                activeMutes = new ArrayList<>();
                expiryTasks = new ArrayList<>();
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
                            BukkitTask task = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                                    activeMutes.remove(punishment);
                                    expiryTasks.remove(punishment.getTask());
                                    punishment.setTask(null);
                                }
                            }.runTaskLaterAsynchronously(AuroraMCAPI.getCore(), (punishment.getExpire() - System.currentTimeMillis()) / 50);
                            expiryTasks.add(task);
                            punishment.setTask(task);
                        } else {
                            //Expire this punishment.
                            AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                        }
                    }
                }
                preferences = AuroraMCAPI.getDbManager().getPlayerPreferences(pl);
                rank = AuroraMCAPI.getDbManager().getRank(pl);
                if (rank.hasPermission("all")) {
                    activeSubscription = new PlusSubscription(pl);
                }  else {
                    long endTimestamp = AuroraMCAPI.getDbManager().getExpire(pl);
                    if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
                        activeSubscription = new PlusSubscription(pl);
                    }
                }

                subranks = AuroraMCAPI.getDbManager().getSubRanks(pl);

                //Load the friends list.
                friendsList = AuroraMCAPI.getDbManager().getFriendsList(pl);


                //If they have a rank-exclusive status set, check if they still have permission to use it.
                if (!friendsList.getCurrentStatus().hasUnlocked(pl)) {
                        //They no longer have permission, default to Online.
                        friendsList.setCurrentStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(101), true);
                }

                //Get the bungee to send all of the friend data to the server
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("UpdateFriendsList");
                out.writeUTF(name);
                player.sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                //Now that ranks are loaded, update everyones tab.
                new BukkitRunnable() {
                    @Override
                    @SuppressWarnings("deprecation")
                    public void run() {
                        //This has to be run on the main thread.
                        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
                            if (!pl.isVanished()) {
                                if (player == bukkitPlayer) {
                                    org.bukkit.scoreboard.Team team = scoreboard.getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                    team.addPlayer(player);
                                    String s;
                                    if (pl.getActiveDisguise() != null) {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getActiveDisguise().getRank(), pl.getActiveSubscription());
                                    } else {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getRank(), pl.getActiveSubscription());
                                    }
                                    if (!s.equals("")) {
                                        s += " ";
                                    }
                                    s += "§" + ((pl.getTeam() == null) ? "r" : pl.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                    team.setPrefix(s);
                                    continue;
                                }
                                AuroraMCPlayer player = AuroraMCAPI.getPlayer(bukkitPlayer);
                                //If player is equal to null at any point, it means they have literally just joined and the player object has yet to be created, and will update this users scoreboard once it is created.
                                if (player != null) {
                                    if (player.getRank() != null) {
                                        //If it is ever null, it again has yet to fetch it from the DB, and so has yet to update every other players teams.
                                        if (player.getScoreboard().getScoreboard().getTeam(pl.getPlayer().getName()) != null) {
                                            player.getScoreboard().getScoreboard().getTeam(pl.getPlayer().getName()).unregister();
                                        }
                                        org.bukkit.scoreboard.Team team = player.getScoreboard().getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                        team.addPlayer(pl.getPlayer());
                                        String s;
                                        if (pl.getActiveDisguise() != null) {
                                            s = AuroraMCAPI.getFormatter().rankFormat(pl.getActiveDisguise().getRank(), pl.getActiveSubscription());
                                        } else {
                                            s = AuroraMCAPI.getFormatter().rankFormat(pl.getRank(), pl.getActiveSubscription());
                                        }
                                        if (!s.equals("")) {
                                            s += " ";
                                        }
                                        s += "§" + ((pl.getTeam() == null) ? "r" : pl.getTeam().getTeamColor());
                                        team.setPrefix(s);

                                        if (scoreboard.getScoreboard().getTeam(player.getPlayer().getName()) != null) {
                                            scoreboard.getScoreboard().getTeam(player.getPlayer().getName()).unregister();
                                        }
                                        team = scoreboard.getScoreboard().registerNewTeam(player.getPlayer().getName());
                                        team.addPlayer(player.getPlayer());
                                        if (player.getActiveDisguise() != null) {
                                            s = AuroraMCAPI.getFormatter().rankFormat(player.getActiveDisguise().getRank(), player.getActiveSubscription());
                                        } else {
                                            s = AuroraMCAPI.getFormatter().rankFormat(player.getRank(), player.getActiveSubscription());
                                        }
                                        if (!s.equals("")) {
                                            s += " ";
                                        }
                                        s += "§" + ((player.getTeam() == null) ? "r" : player.getTeam().getTeamColor());
                                        team.setPrefix(s);
                                    }
                                }
                            } else {
                                if (player == bukkitPlayer) {
                                    org.bukkit.scoreboard.Team team = scoreboard.getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                    team.addPlayer(player);
                                    String s;
                                    if (pl.getActiveDisguise() != null) {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getActiveDisguise().getRank(), pl.getActiveSubscription());
                                    } else {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getRank(), pl.getActiveSubscription());
                                    }
                                    if (!s.equals("")) {
                                        s += " ";
                                    }
                                    s += "§" + ((pl.getTeam() == null) ? "r" : pl.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                    team.setPrefix(s);
                                    continue;
                                }
                                if (scoreboard.getScoreboard().getTeam(player.getPlayer().getName()) != null) {
                                    scoreboard.getScoreboard().getTeam(player.getPlayer().getName()).unregister();
                                }
                                AuroraMCPlayer pla = AuroraMCAPI.getPlayer(bukkitPlayer);
                                if (pla == null) {
                                    continue;
                                }

                                if (pla.getRank().getId() >= pl.getRank().getId()) {
                                    bukkitPlayer.showPlayer(pl.getPlayer());
                                    org.bukkit.scoreboard.Team team = pla.getScoreboard().getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                    team.addPlayer(pl.getPlayer());
                                    String s;
                                    if (pl.getActiveDisguise() != null) {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getActiveDisguise().getRank(), pl.getActiveSubscription());
                                    } else {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getRank(), pl.getActiveSubscription());
                                    }
                                    if (!s.equals("")) {
                                        s += " ";
                                    }
                                    s += "§" + ((pl.getTeam() == null) ? "r" : pl.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                }
                                if (pla.getRank().getId() <= pl.getRank().getId()) {
                                    pl.getPlayer().showPlayer(bukkitPlayer);
                                    org.bukkit.scoreboard.Team team = scoreboard.getScoreboard().registerNewTeam(pla.getPlayer().getName());
                                    team.addPlayer(pla.getPlayer());
                                    String s;
                                    if (pla.getActiveDisguise() != null) {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pla.getActiveDisguise().getRank(), pla.getActiveSubscription());
                                    } else {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pla.getRank(), pla.getActiveSubscription());
                                    }
                                    if (!s.equals("")) {
                                        s += " ";
                                    }
                                    s += "§" + ((pla.getTeam() == null) ? "r" : pla.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                }
                            }
                        }
                    }
                }.runTask(AuroraMCAPI.getCore());

                linkedDiscord = AuroraMCAPI.getDbManager().getDiscord(id);

                statistics = AuroraMCAPI.getDbManager().getStatistics(pl);


                bank = AuroraMCAPI.getDbManager().getBank(pl);

                channel = AuroraMCAPI.getDbManager().getChannel(pl);

                activeReport = AuroraMCAPI.getDbManager().getActiveReport(id);

                if (activeReport != null) {
                    activeReportTask = new BukkitRunnable(){
                        @Override
                        public void run() {
                            if (activeReport != null) {
                                player.spigot().sendMessage(AuroraMCAPI.getFormatter().formatReportMessage(getActiveReport()));
                            }
                        }
                    }.runTaskTimerAsynchronously(AuroraMCAPI.getCore(), 0, 600);
                }

                if (preferences.isReportNotificationsEnabled()) {
                    int offlineReports = AuroraMCAPI.getDbManager().getOfflineReports(id);
                    if (offlineReports > 0) {
                        player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("While you were offline, **%s** of your reports were handled by our Staff Team. Use /viewreports to see the individual outcomes of each report.", offlineReports)));
                    }
                }

                ignoredPlayers = AuroraMCAPI.getDbManager().getIgnoredPlayers(id);

                unlockedCosmetics = AuroraMCAPI.getDbManager().getUnlockedCosmetics(player.getUniqueId());
                runningCosmeticTasks = new HashMap<>();
                activeCosmetics = AuroraMCAPI.getDbManager().getActiveCosmetics(player.getUniqueId());
                for (Cosmetic cosmetic : activeCosmetics.values()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cosmetic.onEquip(pl);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }

                //To ensure that this is being called after everything has been retrived, it is called here and then replaces the object already in the cache.
                PlayerObjectCreationEvent creationEvent = new PlayerObjectCreationEvent(pl);
                Bukkit.getPluginManager().callEvent(creationEvent);
                if (pl != creationEvent.getPlayer()) {
                    AuroraMCAPI.newPlayer(creationEvent.getPlayer());
                }
                loaded = true;
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UpdateParty");
        out.writeUTF(name);
        player.sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public AuroraMCPlayer(AuroraMCPlayer oldPlayer) {
        id = oldPlayer.getId();
        player = oldPlayer.getPlayer();
        name = oldPlayer.getName();
        rank = oldPlayer.getRank();
        subranks = oldPlayer.getSubranks();
        activeDisguise = oldPlayer.getActiveDisguise();
        activeSubscription = oldPlayer.getActiveSubscription();
        team = oldPlayer.getTeam();
        linkedDiscord = oldPlayer.getLinkedDiscord();
        discordCodeGenerated = oldPlayer.discordCodeGenerated;
        history = oldPlayer.history;
        activeMutes = oldPlayer.getActiveMutes();
        expiryTasks = oldPlayer.expiryTasks;
        scoreboard = oldPlayer.scoreboard;
        vanished = oldPlayer.vanished;
        statistics = oldPlayer.statistics;
        bank = oldPlayer.bank;
        friendsList = oldPlayer.friendsList;
        channel = oldPlayer.channel;
        partyUUID = oldPlayer.partyUUID;
        preferences = oldPlayer.preferences;
        activeReport = oldPlayer.activeReport;
        ignoredPlayers = oldPlayer.ignoredPlayers;
        lastMessageSent = oldPlayer.lastMessageSent;
        activeReportTask = oldPlayer.activeReportTask;
        unlockedCosmetics = oldPlayer.unlockedCosmetics;
        activeCosmetics = oldPlayer.activeCosmetics;
        runningCosmeticTasks = oldPlayer.runningCosmeticTasks;
        loaded = oldPlayer.loaded;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isDisguised() {return activeDisguise != null;}

    public Disguise getActiveDisguise() {
        return activeDisguise;
    }

    public ArrayList<SubRank> getSubranks() {
        return new ArrayList<>(subranks);
    }

    public Player getPlayer() {
        return player;
    }

    public PlusSubscription getActiveSubscription() {
        return activeSubscription;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public boolean disguise(String skin, String name, Rank rank) {
        if (this.activeDisguise != null) {
            activeDisguise.switchDisguise();
        }

        activeDisguise = new Disguise(this, name, skin, rank);
        return activeDisguise.apply(true);
    }

    public boolean applyDisguise() {
        if (activeDisguise != null) {
            return activeDisguise.apply(true);
        }
        return true;
    }

    public boolean undisguise() {
        if (activeDisguise != null) {
            Disguise activeDisguise = this.activeDisguise;
            if (!activeDisguise.getName().equals(name)) {
                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                    player.getScoreboard().getScoreboard().getTeam(activeDisguise.getName()).unregister();
                }
            }
            this.activeDisguise = null;
            AuroraMCPlayer pl = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().undisguise(pl);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
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
        new BukkitRunnable() {
            @Override
            public void run() {
                discordCodeGenerated = false;
            }
        }.runTaskLater(AuroraMCAPI.getCore(), 1200);
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
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
                        activeMutes.remove(punishment);
                        expiryTasks.remove(punishment.getTask());
                        punishment.setTask(null);
                    }
                }.runTaskLaterAsynchronously(AuroraMCAPI.getCore(), (punishment.getExpire() - System.currentTimeMillis()) / 50);
                expiryTasks.add(task);
                punishment.setTask(task);
            } else {
                //Expire this punishment.
                AuroraMCAPI.getDbManager().expirePunishment(punishment.getPunishmentCode(), punishment.getStatus());
            }
        }
    }

    public PlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard() {
        player.setScoreboard(scoreboard.getScoreboard());
    }

    public void clearScoreboard() {
        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            AuroraMCPlayer player = AuroraMCAPI.getPlayer(bukkitPlayer);
            if (player != null) {
                org.bukkit.scoreboard.Team team = player.getScoreboard().getScoreboard().getTeam(name);
                if (team != null) {
                    team.unregister();
                }
            }
        }
    }

    public void updateNametag(AuroraMCPlayer player) {
        String s;
        if (player.getActiveDisguise() != null) {
            //They are disguised, update their nametag.
            s = AuroraMCAPI.getFormatter().rankFormat(player.getActiveDisguise().getRank(), player.getActiveSubscription());
            if (this.scoreboard.getScoreboard().getTeam(player.getName()) != null) {
                this.scoreboard.getScoreboard().getTeam(player.getName()).unregister();

            }
            if (this.scoreboard.getScoreboard().getTeam(player.getPlayer().getName()) == null) {
                this.getScoreboard().getScoreboard().registerNewTeam(player.getPlayer().getName());
            }
        } else {
            s = AuroraMCAPI.getFormatter().rankFormat(player.getRank(), player.getActiveSubscription());
            if (this.getScoreboard().getScoreboard().getTeam(player.getName()) == null) {
                this.getScoreboard().getScoreboard().registerNewTeam(player.getName());
            }
        }
        if (!s.equals("")) {
            s += " ";
        }
        s += "§" + ((player.getTeam() == null)?"r":player.getTeam().getTeamColor());

        this.getScoreboard().getScoreboard().getTeam(player.getPlayer().getName()).setPrefix(s);
        if (!this.getScoreboard().getScoreboard().getTeam(player.getPlayer().getName()).hasEntry(player.getPlayer().getName())) {
            for (String old : this.scoreboard.getScoreboard().getTeam(player.getPlayer().getName()).getEntries()) {
                this.scoreboard.getScoreboard().getTeam(player.getPlayer().getName()).removeEntry(old);
            }
            this.scoreboard.getScoreboard().getTeam(player.getPlayer().getName()).addEntry(player.getPlayer().getName());
        }
    }

    public boolean isVanished() {
        return vanished;
    }

    public void vanish() {
        this.vanished = true;
        AuroraMCPlayer pl = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().vanish(pl);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            if (player==this) {
                continue;
            }
            if (player.getRank().getId() < this.getRank().getId()) {
                player.getPlayer().hidePlayer(this.player);
                if (player.getScoreboard().getScoreboard().getTeam(this.player.getName()) != null) {
                    player.getScoreboard().getScoreboard().getTeam(this.player.getName()).unregister();
                }
            }
        }
    }

    public void unvanish() {
        this.vanished = false;
        AuroraMCPlayer pl = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().unvanish(pl);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            if (player==this) {
                continue;
            }
            player.getPlayer().showPlayer(this.player);
            player.updateNametag(this);
        }
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
        if (subscription != null) {
            subscription.expire();
        }
    }

    public void newSubscription() {
        AuroraMCPlayer pl = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (rank.hasPermission("all")) {
                    activeSubscription = new PlusSubscription(pl);
                } else {
                    long endTimestamp = AuroraMCAPI.getDbManager().getExpire(pl);
                    if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
                        activeSubscription = new PlusSubscription(pl);
                    }
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public FriendsList getFriendsList() {
        return friendsList;
    }

    public void setChannel(ChatChannel channel) {
        this.channel = channel;
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

        if (activeReport == null) {
            if (this.activeReportTask != null) {
                this.activeReportTask.cancel();
                this.activeReportTask = null;
            }
            this.activeReport = null;
        } else {
            this.activeReport = activeReport;
            this.activeReportTask = new BukkitRunnable(){
                @Override
                public void run() {
                    if (activeReport != null) {
                        player.spigot().sendMessage(AuroraMCAPI.getFormatter().formatReportMessage(getActiveReport()));
                    }
                }
            }.runTaskTimerAsynchronously(AuroraMCAPI.getCore(), 0, 600);
        }

    }

    public BukkitTask getActiveReportTask() {
        return activeReportTask;
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

    public void addIgnored(IgnoredPlayer user) {
        ignoredPlayers.add(user);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IgnoreAdded");
        out.writeUTF(name);
        out.writeInt(user.getId());
        out.writeUTF(user.getName());
        player.sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().setIgnoredPlayers(id, ignoredPlayers);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public void removeIgnored(IgnoredPlayer user) {
        ignoredPlayers.remove(user);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IgnoreRemoved");
        out.writeUTF(name);
        out.writeInt(user.getId());
        player.sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());


        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().setIgnoredPlayers(id, ignoredPlayers);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public long getLastMessageSent() {
        return lastMessageSent;
    }

    public void messageSent() {
        lastMessageSent = System.currentTimeMillis();
    }

    public HashMap<Cosmetic, BukkitTask> getRunningCosmeticTasks() {
        return runningCosmeticTasks;
    }

    public HashMap<Cosmetic.CosmeticType, Cosmetic> getActiveCosmetics() {
        return activeCosmetics;
    }

    public List<Cosmetic> getUnlockedCosmetics() {
        return unlockedCosmetics;
    }



    public void sendTitle(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime, ChatColor titleColor, ChatColor subtitleColor, boolean titleBold, boolean subTitleBold) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\",\"color\":\"" + titleColor.name().toLowerCase() + "\",\"bold\":\"" + ((titleBold) ? "true" : "false") + "\"}");
        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\",\"color\":\"" + subtitleColor.name().toLowerCase() + "\",\"bold\":\"" + ((subTitleBold) ? "true" : "false") + "\"}");

        if (subtitle != null) {
            PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle, fadeInTime, showTime, fadeOutTime);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitlePacket);
        }

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle, fadeInTime, showTime, fadeOutTime);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);

    }

    public void sendHotBar(String message, ChatColor color, boolean bold) {
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\",\"color\":\"" + color.name().toLowerCase() + "\",\"bold\":\"" + ((bold) ? "true" : "false") + "\"}");

        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLastAdminMessage(UUID lastMessaged) {
        this.lastAdminMessaged = lastMessaged;
    }

    public UUID getLastAdminMessaged() {
        return lastAdminMessaged;
    }
}
