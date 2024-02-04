/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.permissions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public final class PlusSubscription {

    private AuroraMCPlayer player;
    private static final TextComponent hoverTextPlus;
    private static final TextComponent hoverText;
    private static final String clickURL = "http://store.auroramc.net";
    private ChatColor color;
    private ChatColor levelColor;
    private ChatColor suffixColor;
    private long endTimestamp;
    private int daysSubscribed;
    private final int subscriptionStreak;
    private final long streakStartTimestamp;
    private final Permission permission = Permission.PLUS;

    static {

        hoverTextPlus = new TextComponent("+ Plus");
        hoverTextPlus.setColor(ChatColor.DARK_AQUA);
        hoverTextPlus.setBold(true);

        hoverText = new TextComponent("\n \nPlus is a subscription based\n" +
                "perk where you receive exclusive\n" +
                "in-game benefits and offers.\n" +
                " \n");
        hoverText.setColor(ChatColor.WHITE);
        hoverText.setBold(false);
    }

    public PlusSubscription(AuroraMCPlayer player) {
        this.player = player;

        this.color = AuroraMCAPI.getDbManager().getPlusColour(player);
        this.levelColor = AuroraMCAPI.getDbManager().getLevelColour(player);
        this. suffixColor = AuroraMCAPI.getDbManager().getSuffixColour(player);

        this.endTimestamp = AuroraMCAPI.getDbManager().getExpire(player);
        this.daysSubscribed = AuroraMCAPI.getDbManager().getDaysSubscribed(player);
        this.streakStartTimestamp = AuroraMCAPI.getDbManager().getStreakStartTimestamp(player);
        this.subscriptionStreak = AuroraMCAPI.getDbManager().getStreak(player);
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

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public ChatColor getColor() {
        if (color == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() == Rank.PLAYER) {
                    return ChatColor.DARK_AQUA;
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() == Rank.PLAYER) {
                    return ChatColor.DARK_AQUA;
                } else {
                    return player.getRank().getPrefixColor();
                }
            }
        }
        return color;
    }

    public ChatColor getLevelColor() {
        if (levelColor == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() == Rank.PLAYER) {
                    return ChatColor.DARK_AQUA;
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() == Rank.PLAYER) {
                    return ChatColor.DARK_AQUA;
                } else {
                    return player.getRank().getPrefixColor();
                }
            }
        }
        return levelColor;
    }

    public ChatColor getSuffixColor() {
        if (suffixColor == null) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getRank() == Rank.PLAYER) {
                    return ChatColor.DARK_AQUA;
                } else {
                    return player.getActiveDisguise().getRank().getPrefixColor();
                }
            } else {
                if (player.getRank() == Rank.PLAYER) {
                    return ChatColor.DARK_AQUA;
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

    public void setColor(ChatColor color, boolean send) {
        this.color = color;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlusColour");
        out.writeUTF(player.getName());
        out.writeUTF(color.getName());
        if (send) {
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void setLevelColor(ChatColor color, boolean send) {
        this.levelColor = color;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LevelColour");
        out.writeUTF(player.getName());
        out.writeUTF(color.getName());
        if (send) {
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void setSuffixColor(ChatColor color, boolean send) {
        this.suffixColor = color;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SuffixColour");
        out.writeUTF(player.getName());
        out.writeUTF(color.getName());
        if (send) {
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public String getClickURL() {
        return clickURL;
    }

    public BaseComponent getHoverText() {
        TextComponent component = new TextComponent(hoverTextPlus);
        component.setColor(color);
        component.addExtra(hoverText);

        if (player.getRank() != Rank.ELITE && player.getRank() != Rank.MASTER && player.getRank().getPrefixHoverURL() == null) {
            TextComponent cmp = new TextComponent("Click to visit the store!");
            cmp.setColor(ChatColor.GREEN);
            cmp.setBold(false);
            component.addExtra(cmp);

        }
        return component;
    }

    public long getStreakStartTimestamp() {
        return streakStartTimestamp;
    }

    public void extend(int days) {
        endTimestamp += (days * 86400000);
        daysSubscribed += days;
    }
}

