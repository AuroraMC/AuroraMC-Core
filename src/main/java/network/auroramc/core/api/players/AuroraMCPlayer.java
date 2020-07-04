package network.auroramc.core.api.players;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.events.player.PlayerObjectCreationEvent;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.permissions.PlusSubscription;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.PunishmentHistory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

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
    private PlayerScoreboard scoreboard;

    public AuroraMCPlayer(Player player) {
        scoreboard = new PlayerScoreboard(this, Bukkit.getScoreboardManager().getNewScoreboard());
        AuroraMCPlayer pl = this;
        this.player = player;
        name = player.getName();

        Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(pl);
        if (disguise != null) {
            activeDisguise = disguise;
            activeDisguise.apply(false);
        }

        //TODO: Load player statistics.
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

                rank = AuroraMCAPI.getDbManager().getRank(pl);
                if (rank.hasPermission("all")) {
                    activeSubscription = new PlusSubscription(pl);
                }
                subranks = AuroraMCAPI.getDbManager().getSubRanks(pl);
                //Now taht ranks are loaded, update everyones tab.
                new BukkitRunnable() {
                    @Override
                    @SuppressWarnings("deprecation")
                    public void run() {
                        //This has to be run on the main thread.
                        for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
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
                                s += "§" + ((pl.getTeam() == null)?"r":pl.getTeam().getTeamColor());
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
                                    s += "§" + ((pl.getTeam() == null)?"r":pl.getTeam().getTeamColor());
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
                                    s += "§" + ((player.getTeam() == null)?"r":player.getTeam().getTeamColor());
                                    team.setPrefix(s);
                                }
                            }
                        }
                    }
                }.runTask(AuroraMCAPI.getCore());

                linkedDiscord = AuroraMCAPI.getDbManager().getDiscord(id);

                //To ensure that this is being called after everything has been retrived, it is called here and then replaces the object already in the cache.
                PlayerObjectCreationEvent creationEvent = new PlayerObjectCreationEvent(pl);
                Bukkit.getPluginManager().callEvent(creationEvent);
                if (pl != creationEvent.getPlayer()) {
                    AuroraMCAPI.newPlayer(creationEvent.getPlayer());
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());

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
            activeDisguise.undisguise();
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


}
