package network.auroramc.core.api.players;

import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.permissions.UltimateSubscription;
import org.bukkit.entity.Player;

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

    public AuroraMCPlayer(Player player) {
        this.player = player;
        name = player.getName();

        //TODO: Load AuroraMCID


        //TODO: Load disguise, this is the priority. Rank can be gotten at any point.


        //TODO: Load rank and player statistics.


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
        return rank.hasPermission(string);
    }

    public boolean hasPermission(int id) {
        return rank.hasPermission(id);
    }
}
