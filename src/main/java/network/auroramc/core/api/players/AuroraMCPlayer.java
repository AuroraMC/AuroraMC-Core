package network.auroramc.core.api.players;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.database.DatabaseManager;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.permissions.UltimateSubscription;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AuroraMCPlayer {

    private long id;
    private final Player player;
    private final String name;
    private Rank rank;
    private ArrayList<SubRank> subranks;
    private Disguise activeDisguise;
    private UltimateSubscription activeSubscription;
    private Team team;
    private String linkedDiscord;

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
                rank = AuroraMCAPI.getDbManager().getRank(pl);
                subranks = AuroraMCAPI.getDbManager().getSubRanks(pl);
                if (rank.hasPermission("all")) {
                    activeSubscription = new UltimateSubscription(pl);
                }

                linkedDiscord = AuroraMCAPI.getDbManager().getDiscord(id);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());

    }

    public Rank getRank() {
        return rank;
    }

    public boolean isDisguised() {return activeDisguise != null;}

    public Disguise getActiveDisguise() {
        return activeDisguise;
    }

    public List<SubRank> getSubranks() {
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

    public long getId() {
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
}
