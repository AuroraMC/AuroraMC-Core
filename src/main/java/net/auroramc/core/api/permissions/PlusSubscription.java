/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.permissions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public final class PlusSubscription {

    private final AuroraMCPlayer player;
    private static final String hoverText = "&%s&l+ Plus\n" +
            "\n" +
            "&fPlus is a subscription based\n" +
            "&fperk where you receive exclusive\n" +
            "&fin-game benefits and offers.\n" +
            "\n" +
            "&aClick to visit the store!";
    private static final String clickURL = "http://store.auroramc.net";
    private Character color;
    private Character levelColor;
    private Character suffixColor;
    private long endTimestamp;
    private int daysSubscribed;
    private final int subscriptionStreak;
    private final long streakStartTimestamp;
    private final Permission permission = Permission.PLUS;
    private BukkitTask expireTask;

    public PlusSubscription(AuroraMCPlayer player) {
        this.player = player;

        this.color = AuroraMCAPI.getDbManager().getPlusColour(player);
        this.levelColor = AuroraMCAPI.getDbManager().getLevelColour(player);
        this. suffixColor = AuroraMCAPI.getDbManager().getSuffixColour(player);

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

    public PlusSubscription(UUID uuid) {
        this.player = null;

        this.color = AuroraMCAPI.getDbManager().getPlusColour(uuid);
        this.levelColor = AuroraMCAPI.getDbManager().getLevelColour(uuid);
        this. suffixColor = AuroraMCAPI.getDbManager().getSuffixColour(uuid);

        this.endTimestamp = AuroraMCAPI.getDbManager().getExpire(uuid);
        this.daysSubscribed = AuroraMCAPI.getDbManager().getDaysSubscribed(uuid);
        this.streakStartTimestamp = AuroraMCAPI.getDbManager().getStreakStartTimestamp(uuid);
        this.subscriptionStreak = AuroraMCAPI.getDbManager().getStreak(uuid);
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public char getColor() {
        if (color == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() == Rank.PLAYER) {
                    return '3';
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() == Rank.PLAYER) {
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
                if (player.getActiveDisguise().getRank() == Rank.PLAYER) {
                    return '3';
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() == Rank.PLAYER) {
                    return '3';
                } else {
                    return player.getRank().getPrefixColor();
                }
            }
        }
        return levelColor;
    }

    public Character getSuffixColor() {
        if (suffixColor == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() == Rank.PLAYER) {
                    return '3';
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() == Rank.PLAYER) {
                    return '3';
                } else {
                    return player.getRank().getPrefixColor();
                }
            }
        }
        return suffixColor;
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

    public void setSuffixColor(Character color) {
        this.suffixColor = color;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SuffixColour");
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

