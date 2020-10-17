package net.auroramc.core.api.permissions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.permissions.Plus;
import net.auroramc.core.permissions.ranks.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class PlusSubscription {

    private final AuroraMCPlayer player;
    private static final String hoverText = "&%s&l+ Plus\n" +
            "\n" +
            "&fPlus is a subscription based\n" +
            "&fperk where you receive exclusive\n" +
            "&fin-game benefits and offers.\n" +
            "\n" +
            "&aClick to visit the store!";
    private static final String clickURL = "http://store.auroramc.block2block.me/";
    private Character color;
    private Character levelColor;
    private long endTimestamp;
    private int daysSubscribed;
    private final int subscriptionStreak;
    private final long streakStartTimestamp;
    private final Permission permission = new Plus();
    private BukkitTask expireTask;

    public PlusSubscription(AuroraMCPlayer player) {
        this.player = player;

        this.color = AuroraMCAPI.getDbManager().getPlusColour(player);
        this.levelColor = AuroraMCAPI.getDbManager().getLevelColour(player);

        this.endTimestamp = AuroraMCAPI.getDbManager().getExpire(player);
        this.daysSubscribed = AuroraMCAPI.getDbManager().getDaysSubscribed(player);
        this.streakStartTimestamp = AuroraMCAPI.getDbManager().getStreakStartTimestamp(player);
        this.subscriptionStreak = AuroraMCAPI.getDbManager().getStreak(player);
        if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
            expireTask = new BukkitRunnable(){
                @Override
                public void run() {
                    player.expireSubscription();
                }
            }.runTaskLater(AuroraMCAPI.getCore(), (endTimestamp - System.currentTimeMillis())/50);
        }
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public char getColor() {
        if (color == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() instanceof Player) {
                    return '3';
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() instanceof Player) {
                    return '3';
                } else {
                    return player.getRank().getPrefixColor();
                }
            }
        }
        return color;
    }

    public Character getLevelColor() {
        if (levelColor == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() instanceof Player) {
                    return '3';
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() instanceof Player) {
                    return '3';
                } else {
                    return player.getRank().getPrefixColor();
                }
            }
        }
        return levelColor;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public int getDaysSubscribed() {
        return daysSubscribed;
    }

    public int getSubscriptionStreak() {
        return subscriptionStreak;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setColor(Character color) {
        this.color = color;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlusColour");
        out.writeUTF(player.getName());
        out.writeChar(color);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setLevelColor(Character color) {
        this.levelColor = color;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LevelColour");
        out.writeUTF(player.getName());
        out.writeChar(color);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public String getClickURL() {
        return clickURL;
    }

    public String getHoverText() {
        return hoverText;
    }

    public long getStreakStartTimestamp() {
        return streakStartTimestamp;
    }

    public void extend(int days) {
        endTimestamp += (days * 86400000);
        daysSubscribed += days;

        if (expireTask != null) {
            expireTask.cancel();
            expireTask = new BukkitRunnable(){
                @Override
                public void run() {
                    player.expireSubscription();
                }
            }.runTaskLater(AuroraMCAPI.getCore(), (endTimestamp - System.currentTimeMillis())/50);
        }
    }

    public void expire() {
        if (expireTask != null) {
            expireTask.cancel();
        }
    }
}

