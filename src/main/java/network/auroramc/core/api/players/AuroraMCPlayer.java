package network.auroramc.core.api.players;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.database.DatabaseManager;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.permissions.UltimateSubscription;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.PunishmentHistory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuroraMCPlayer {

    private int id;
    private final Player player;
    private final String name;
    private Rank rank;
    private ArrayList<SubRank> subranks;
    private Disguise activeDisguise;
    private UltimateSubscription activeSubscription;
    private Team team;
    private String linkedDiscord;
    private boolean discordCodeGenerated;
    private PunishmentHistory history;
    private List<Punishment> activeMutes;
    private List<BukkitTask> expiryTasks;

    public AuroraMCPlayer(Player player) {
        AuroraMCPlayer pl = this;
        this.player = player;
        name = player.getName();

        new BukkitRunnable(){
            @Override
            public void run() {
                Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(pl);
                if (disguise != null) {
                    activeDisguise = disguise;
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            activeDisguise.apply();
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());

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
                subranks = AuroraMCAPI.getDbManager().getSubRanks(pl);
                if (rank.hasPermission("all")) {
                    activeSubscription = new UltimateSubscription(pl);
                }

                linkedDiscord = AuroraMCAPI.getDbManager().getDiscord(id);
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

    public UltimateSubscription getActiveSubscription() {
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

        activeDisguise = new Disguise(this, skin, name, rank);
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().setDisguise(activeDisguise.getPlayer(), activeDisguise);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        return activeDisguise.apply();
    }

    public boolean applyDisguise() {
        if (activeDisguise != null) {
            return activeDisguise.apply();
        }
        return true;
    }

    public boolean undisguise() {
        if (activeDisguise != null) {
            Disguise activeDisguise = this.activeDisguise;
            this.activeDisguise = null;
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
}
