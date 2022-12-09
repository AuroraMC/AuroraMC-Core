/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ServerInfo;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.events.player.PlayerObjectCreationEvent;
import net.auroramc.core.api.events.player.PlayerShowEvent;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.api.players.friends.Friend;
import net.auroramc.core.api.players.friends.FriendsList;
import net.auroramc.core.api.players.scoreboard.PlayerScoreboard;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentHistory;
import net.auroramc.core.api.stats.PlayerBank;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.Pronoun;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import us.myles.ViaVersion.api.Via;

import java.util.*;
import java.util.stream.Collectors;

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

    protected boolean dead;
    protected boolean hidden;
    protected boolean moved;

    //Just a variable so other systems knows when a player has been fully loaded.
    private boolean loaded;

    private Map<String, Hologram> holograms;


    public AuroraMCPlayer(Player player) {
        loaded = false;
        dead = false;
        hidden = false;
        moved = false;
        holograms = new HashMap<>();
        scoreboard = new PlayerScoreboard(this, Bukkit.getScoreboardManager().getNewScoreboard());
        AuroraMCPlayer pl = this;
        this.player = player;
        name = player.getName();

        lastMessageSent = -1;

        Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(pl);
        if (disguise != null) {
            activeDisguise = disguise;
            if (!AuroraMCAPI.getDbManager().isHideDisguiseName(player.getUniqueId())) {
                activeDisguise.apply(false);
            }
        }

        this.vanished = AuroraMCAPI.getDbManager().isVanished(this);
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            player1.hidePlayer(player);
            player.hidePlayer(player1);
        }

        AuroraMCAPI.newPlayer(this);

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

                linkedDiscord = AuroraMCAPI.getDbManager().getDiscord(id);

                statistics = AuroraMCAPI.getDbManager().getStatistics(pl);

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

                int offlineReports = AuroraMCAPI.getDbManager().getOfflineReports(id);
                if (preferences.isReportNotificationsEnabled()) {
                    if (offlineReports > 0) {
                        player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("While you were offline, **%s** of your reports were handled by our Staff Team. Use /viewreports to see the individual outcomes of each report.", offlineReports)));
                    }
                }
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

                unlockedCosmetics = AuroraMCAPI.getDbManager().getUnlockedCosmetics(player.getUniqueId());
                runningCosmeticTasks = new HashMap<>();
                activeCosmetics = AuroraMCAPI.getDbManager().getActiveCosmetics(player.getUniqueId());

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
                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            player1.hidePlayer(player);
                            player.hidePlayer(player1);
                        }
                        //This has to be run on the main thread.
                        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
                            if (!pl.isVanished()) {
                                if (player == bukkitPlayer) {
                                    org.bukkit.scoreboard.Team team = scoreboard.getScoreboard().getTeam(pl.getPlayer().getName());
                                    if (team == null) {
                                        try {
                                            team = scoreboard.getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                        } catch (IllegalArgumentException ignored) {
                                            team = scoreboard.getScoreboard().getTeam(pl.getPlayer().getName());
                                        }
                                    }
                                    team.addPlayer(player);
                                    String s;
                                    if (pl.getActiveDisguise() != null && !pl.getPreferences().isHideDisguiseNameEnabled()) {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getActiveDisguise().getRank(), pl.getActiveSubscription());
                                    } else {
                                        s = AuroraMCAPI.getFormatter().rankFormat(pl.getRank(), pl.getActiveSubscription());
                                    }
                                    if (!s.equals("")) {
                                        s += " ";
                                    }

                                    s += "§" + ((pl.getTeam() == null) ? "r" + ((Via.getAPI().getPlayerVersion(player.getUniqueId()) >= 393)?"§f":"") : ((Via.getAPI().getPlayerVersion(player.getUniqueId()) >= 393)?"r§":"") + pl.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                    String suffix = "";
                                    //Check for a valid subscription and give suffix if they have one enabled.
                                    if (pl.getActiveSubscription() != null) {
                                        if (pl.isLoaded() && pl.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                                            if (pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                                                PlusSymbol symbol = (PlusSymbol) pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
                                                suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", pl.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));

                                            } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                            }
                                        } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                            suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                        }
                                    } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                        suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                    }

                                    team.setSuffix(suffix);
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
                                        PlayerShowEvent event = new PlayerShowEvent(pl);
                                        Bukkit.getPluginManager().callEvent(event);
                                        hidden = event.isHidden();
                                        if (!hidden) {
                                            bukkitPlayer.showPlayer(pl.getPlayer());
                                        }
                                        org.bukkit.scoreboard.Team team = player.getScoreboard().getScoreboard().getTeam(pl.getPlayer().getName());
                                        if (team == null) {
                                            try {
                                                team = player.getScoreboard().getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                            } catch (IllegalArgumentException ignored) {
                                                team = player.getScoreboard().getScoreboard().getTeam(pl.getPlayer().getName());
                                            }
                                        }
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
                                        s += "§" + ((pl.getTeam() == null) ? "r" + ((Via.getAPI().getPlayerVersion(player.getPlayer().getUniqueId()) >= 393)?"§f":"") : ((Via.getAPI().getPlayerVersion(player.getPlayer().getUniqueId()) >= 393)?"r§":"") + pl.getTeam().getTeamColor());
                                        team.setPrefix(s);

                                        String suffix = "";
                                        if (pl.getActiveSubscription() != null) {
                                            if (pl.isLoaded() && pl.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                                                if (pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                                                    PlusSymbol symbol = (PlusSymbol) pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
                                                    suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", pl.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
                                                } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                    suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                                }
                                            } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                            }
                                        } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                            suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                        }

                                        team.setSuffix(suffix);

                                        if (!player.isVanished() || player.getRank().getId() <= pl.getRank().getId()) {
                                            pl.getPlayer().showPlayer(bukkitPlayer);

                                            team = scoreboard.getScoreboard().getTeam(player.getPlayer().getName());
                                            if (team == null) {
                                                try {
                                                    team = scoreboard.getScoreboard().registerNewTeam(player.getPlayer().getName());
                                                } catch (IllegalArgumentException e) {
                                                    team = scoreboard.getScoreboard().getTeam(player.getPlayer().getName());
                                                }
                                            }
                                            team.addPlayer(player.getPlayer());
                                            if (player.getActiveDisguise() != null) {
                                                s = AuroraMCAPI.getFormatter().rankFormat(player.getActiveDisguise().getRank(), player.getActiveSubscription());
                                            } else {
                                                s = AuroraMCAPI.getFormatter().rankFormat(player.getRank(), player.getActiveSubscription());
                                            }
                                            if (!s.equals("")) {
                                                s += " ";
                                            }
                                            s += "§" + ((player.getTeam() == null) ? "r" + ((Via.getAPI().getPlayerVersion(scoreboard.getPlayer().getPlayer().getUniqueId()) >= 393)?"§f":"") :((Via.getAPI().getPlayerVersion(scoreboard.getPlayer().getPlayer().getUniqueId()) >= 393)?"r§":"") +  player.getTeam().getTeamColor());
                                            team.setPrefix(s);

                                            suffix = "";
                                            if (player.getActiveSubscription() != null) {
                                                if (player.isLoaded() && player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                                                    if (player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                                                        PlusSymbol symbol = (PlusSymbol) player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
                                                        suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", player.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
                                                    } else if (player.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                        suffix = " §7" + player.preferences.getPreferredPronouns().getDisplay() + suffix;
                                                    }
                                                } else if (player.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                    suffix = " §7" + player.preferences.getPreferredPronouns().getDisplay() + suffix;
                                                }
                                            } else if (player.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                suffix = " §7" + player.preferences.getPreferredPronouns().getDisplay() + suffix;
                                            }

                                            team.setSuffix(suffix);
                                        } else {
                                            pl.getPlayer().hidePlayer(bukkitPlayer);
                                        }
                                    }
                                }
                            } else {
                                if (player == bukkitPlayer) {
                                    org.bukkit.scoreboard.Team team = scoreboard.getScoreboard().getTeam(pl.getPlayer().getName());
                                    if (team == null) {
                                        try {
                                            team = scoreboard.getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                        } catch (IllegalArgumentException e) {
                                            team = scoreboard.getScoreboard().getTeam(pl.getPlayer().getName());
                                        }
                                    }
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
                                    s += "§" + ((pl.getTeam() == null) ? "r" + ((Via.getAPI().getPlayerVersion(scoreboard.getPlayer().getPlayer().getUniqueId()) >= 393)?"§f":"") : ((Via.getAPI().getPlayerVersion(scoreboard.getPlayer().getPlayer().getUniqueId()) >= 393)?"r§":"") + pl.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                    String suffix = "";
                                    if (pl.getActiveSubscription() != null) {
                                        if (pl.isLoaded() && pl.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                                            if (pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                                                PlusSymbol symbol = (PlusSymbol) pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
                                                suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", pl.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
                                            } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                            }
                                        } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                            suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                        }
                                    } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                        suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                    }
                                    team.setSuffix(suffix);
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
                                    PlayerShowEvent e = new PlayerShowEvent(pl);
                                    Bukkit.getPluginManager().callEvent(e);
                                    hidden = e.isHidden();
                                    if (!hidden) {
                                        bukkitPlayer.showPlayer(pl.getPlayer());
                                    }
                                    org.bukkit.scoreboard.Team team = pla.getScoreboard().getScoreboard().getTeam(pl.getPlayer().getName());
                                    if (team == null) {
                                        try {
                                            team = pla.getScoreboard().getScoreboard().registerNewTeam(pl.getPlayer().getName());
                                        } catch (IllegalArgumentException ignored) {
                                            team = pla.getScoreboard().getScoreboard().getTeam(pl.getPlayer().getName());
                                        }
                                    }
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
                                    s += "§" + ((pl.getTeam() == null) ? "r" + ((Via.getAPI().getPlayerVersion(pla.getPlayer().getUniqueId()) >= 393)?"§f":"") : ((Via.getAPI().getPlayerVersion(pla.getPlayer().getUniqueId()) >= 393)?"r§":"") + pl.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                    String suffix = "";
                                    if (pl.getActiveSubscription() != null) {
                                        if (pl.isLoaded() && pl.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                                            if (pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                                                PlusSymbol symbol = (PlusSymbol) pl.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
                                                suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", pl.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
                                            } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                            }
                                        } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                            suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                        }
                                    } else if (pl.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                        suffix = " §7" + pl.preferences.getPreferredPronouns().getDisplay() + suffix;
                                    }
                                    team.setSuffix(suffix);
                                } else {
                                    bukkitPlayer.hidePlayer(pl.getPlayer());
                                }
                                if (pla.getRank().getId() <= pl.getRank().getId()) {
                                    pl.getPlayer().showPlayer(bukkitPlayer);
                                    org.bukkit.scoreboard.Team team = scoreboard.getScoreboard().getTeam(pla.getPlayer().getName());
                                    if (team == null) {
                                        try {
                                            team = scoreboard.getScoreboard().registerNewTeam(pla.getPlayer().getName());
                                        } catch (IllegalArgumentException ignored) {
                                            team = scoreboard.getScoreboard().getTeam(pla.getPlayer().getName());
                                        }
                                    }
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
                                    s += "§" + ((pla.getTeam() == null) ? "r" + ((Via.getAPI().getPlayerVersion(scoreboard.getPlayer().getPlayer().getUniqueId()) >= 393)?"§f":"") : ((Via.getAPI().getPlayerVersion(scoreboard.getPlayer().getPlayer().getUniqueId()) >= 393)?"r§":"") + pla.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                    String suffix = "";
                                    if (pla.getActiveSubscription() != null) {
                                        if (pla.isLoaded() && pla.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                                            if (pla.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                                                PlusSymbol symbol = (PlusSymbol) pla.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);
                                                suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", pla.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
                                            } else if (pla.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                                suffix = " §7" + pla.preferences.getPreferredPronouns().getDisplay() + suffix;
                                            }
                                        } else if (pla.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                            suffix = " §7" + pla.preferences.getPreferredPronouns().getDisplay() + suffix;
                                        }
                                    } else if (pla.preferences.getPreferredPronouns() != Pronoun.NONE) {
                                        suffix = " §7" + pla.preferences.getPreferredPronouns().getDisplay() + suffix;
                                    }
                                    team.setSuffix(suffix);
                                } else {
                                    pl.getPlayer().hidePlayer(bukkitPlayer);
                                }
                            }
                        }
                    }
                }.runTask(AuroraMCAPI.getCore());
                if (AuroraMCAPI.isCosmeticsEnabled()) {
                    for (Cosmetic cosmetic : activeCosmetics.values()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cosmetic.onEquip(pl);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }

                if (disguise != null && preferences.isHideDisguiseNameEnabled()) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            pl.applyDisguise(true);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
                //To ensure that this is being called after everything has been retrived, it is called here and then replaces the object already in the cache.
                if (!player.isOnline()) {
                    return;
                }
                PlayerObjectCreationEvent creationEvent = new PlayerObjectCreationEvent(pl);
                Bukkit.getPluginManager().callEvent(creationEvent);
                if (!player.isOnline()) {
                    return;
                }
                AuroraMCAPI.newPlayer(creationEvent.getPlayer());
                creationEvent.getPlayer().loaded = true;
                 if (AuroraMCAPI.getServerInfo().getNetwork() == ServerInfo.Network.TEST) {

                     creationEvent.getPlayer().getPlayer().sendMessage(AuroraMCAPI.getFormatter().highlight(AuroraMCAPI.getFormatter().convert("" +
                             "&4&l▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆&r\n" +
                             "                     &c&l«MISSION CONTROL»\n" +
                             " \n" +
                             "&fYou are currently connected to the AuroraMC Test\n" +
                             "Network!\n" +
                             " \n" +
                             "&fAll servers in this network will not save data, and are\n" +
                             "all on test versions of our plugins.\n" +
                             " \n" +
                             "&4&l▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆")));
                 } else if (AuroraMCAPI.isTestServer()) {
                    creationEvent.getPlayer().getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Server Manager", "&c&lThis server is in test mode. While test mode is enabled, stats and other core features will not be saved."));
                }
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
        if (activeDisguise != null) {
            activeDisguise.setPlayer(this);
        }
        activeSubscription = oldPlayer.getActiveSubscription();
        if (activeSubscription != null) {
            activeSubscription.setPlayer(this);
        }
        team = oldPlayer.getTeam();
        linkedDiscord = oldPlayer.getLinkedDiscord();
        discordCodeGenerated = oldPlayer.discordCodeGenerated;
        history = oldPlayer.history;
        activeMutes = oldPlayer.getActiveMutes();
        expiryTasks = oldPlayer.expiryTasks;
        scoreboard = oldPlayer.scoreboard;
        scoreboard.setPlayer(this);
        vanished = oldPlayer.vanished;
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
        if (activeReport != null) {
            oldPlayer.activeReportTask.cancel();
            activeReportTask = createReportTask();
        } else {
            activeReportTask = null;
        }
        ignoredPlayers = oldPlayer.ignoredPlayers;
        lastMessageSent = oldPlayer.lastMessageSent;
        unlockedCosmetics = oldPlayer.unlockedCosmetics;
        activeCosmetics = oldPlayer.activeCosmetics;
        runningCosmeticTasks = oldPlayer.runningCosmeticTasks;
        lastAdminMessaged = oldPlayer.lastAdminMessaged;
        loaded = oldPlayer.loaded;
        dead = oldPlayer.dead;
        hidden = oldPlayer.hidden;
        holograms = oldPlayer.holograms;
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

    public void setTeam(Team team) {
        this.team = team;
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

    public boolean undisguise() {
        if (activeDisguise != null) {
            Disguise activeDisguise = this.activeDisguise;
            if (!activeDisguise.getName().equals(name)) {
                for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                    if (player.getScoreboard().getScoreboard().getTeam(activeDisguise.getName()) != null) {
                        player.getScoreboard().getScoreboard().getTeam(activeDisguise.getName()).unregister();
                    }
                    if (player.getScoreboard().getScoreboard().getTeam(name) != null) {
                        player.getScoreboard().getScoreboard().getTeam(name).unregister();
                    }
                }
            }
            this.activeDisguise = null;
            AuroraMCPlayer pl = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().undisguise(pl, activeDisguise);
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
        org.bukkit.scoreboard.Team team;
        if ((player.getActiveDisguise() != null && player != this) || (player.getActiveDisguise() != null && player == this && !player.getPreferences().isHideDisguiseNameEnabled())) {
            //They are disguised, update their nametag.
            s = AuroraMCAPI.getFormatter().rankFormat(player.getActiveDisguise().getRank(), player.getActiveSubscription());
            if (this.scoreboard.getScoreboard().getTeam(player.getName()) != null) {
                this.scoreboard.getScoreboard().getTeam(player.getName()).unregister();

            }
            if (this.scoreboard.getScoreboard().getTeam(player.getPlayer().getName()) == null) {
                this.getScoreboard().getScoreboard().registerNewTeam(player.getPlayer().getName());
            }
            team = this.getScoreboard().getScoreboard().getTeam(player.getPlayer().getName());
        } else {
            s = AuroraMCAPI.getFormatter().rankFormat(player.getRank(), player.getActiveSubscription());
            if (this.getScoreboard().getScoreboard().getTeam(player.getName()) == null) {
                this.getScoreboard().getScoreboard().registerNewTeam(player.getName());
            }
            team = this.getScoreboard().getScoreboard().getTeam(player.getName());
        }
        if (!s.equals("")) {
            s += " ";
        }
        s += "§" + ((player.getTeam() == null)?"r" + ((Via.getAPI().getPlayerVersion(this.player.getUniqueId()) >= 393)?"§f":""):((Via.getAPI().getPlayerVersion(this.player.getUniqueId()) >= 393)?"r§":"") + player.getTeam().getTeamColor());

        team.setPrefix(s);
        String suffix = "";
        if (player.getActiveSubscription() != null) {
            if (player.getActiveCosmetics().containsKey(Cosmetic.CosmeticType.PLUS_SYMBOL)) {
                if (player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL) != null) {
                    PlusSymbol symbol = (PlusSymbol) player.getActiveCosmetics().get(Cosmetic.CosmeticType.PLUS_SYMBOL);

                    suffix += AuroraMCAPI.getFormatter().convert(String.format(" &%s%s", player.getActiveSubscription().getSuffixColor(), symbol.getSymbol()));
                } else if (player.preferences.getPreferredPronouns() != Pronoun.NONE) {
                    suffix = " §7" + player.preferences.getPreferredPronouns().getDisplay() ;
                }
            } else if (player.preferences.getPreferredPronouns() != Pronoun.NONE) {
                suffix = " §7" + player.preferences.getPreferredPronouns().getDisplay() ;
            }
        } else if (player.preferences.getPreferredPronouns() != Pronoun.NONE) {
            suffix = " §7" + player.preferences.getPreferredPronouns().getDisplay() ;
        }
        team.setSuffix(suffix);
        if ((player.getActiveDisguise() != null && player != this) || (player == this && !player.getPreferences().isHideDisguiseNameEnabled())) {
            if (!team.hasEntry(player.getPlayer().getName())) {
                for (String old : team.getEntries()) {
                    team.removeEntry(old);
                }
                team.addEntry(player.getPlayer().getName());
            }
        } else {
            if (!team.hasEntry(player.getName())) {
                for (String old : team.getEntries()) {
                    team.removeEntry(old);
                }
                team.addEntry(player.getName());
            }
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
            if (!player.isLoaded() || player.getRank().getId() < this.getRank().getId()) {
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
            this.activeReportTask = createReportTask();
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
        out.writeUTF(user.getName());
        player.sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

        if (!AuroraMCAPI.isTestServer()) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().setIgnoredPlayers(id, ignoredPlayers);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
    }

    public void removeIgnored(IgnoredPlayer user) {
        ignoredPlayers.remove(user);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IgnoreRemoved");
        out.writeUTF(name);
        out.writeInt(user.getId());
        player.sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());


        if (!AuroraMCAPI.isTestServer()) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().setIgnoredPlayers(id, ignoredPlayers);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
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

    public boolean isDead() {
        return dead;
    }

    private BukkitTask createReportTask() {
        return new BukkitRunnable(){
            @Override
            public void run() {
                if (activeReport != null) {
                    player.spigot().sendMessage(AuroraMCAPI.getFormatter().formatReportMessage(getActiveReport()));
                }
            }
        }.runTaskTimerAsynchronously(AuroraMCAPI.getCore(), 0, 600);
    }

    public boolean hasMoved() {
        return moved;
    }

    public void moved() {
        moved = true;
    }

    public Map<String, Hologram> getHolograms() {
        return holograms;
    }
}
