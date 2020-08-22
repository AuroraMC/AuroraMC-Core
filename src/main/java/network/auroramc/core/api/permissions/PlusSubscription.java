package network.auroramc.core.api.permissions;

import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.permissions.permissions.Ultimate;
import network.auroramc.core.permissions.ranks.Player;

import java.util.Date;

public final class PlusSubscription {

    private final AuroraMCPlayer player;
    private static final char ultimateIcon = '✦';
    private static final String hoverText = "&%s&l+ Plus\n" +
            "\n" +
            "&fPlus is a subscription based\n" +
            "&fperk where you recieve exclusive\n" +
            "&fin-game benefits and offers.\n" +
            "\n" +
            "&aClick to visit the store!";
    private static final String clickURL = "http://store.auroramc.block2block.me/";
    private Character color;
    private Character leveLColor;
    private Date endDate;
    private int monthsSubscribed;
    private int subscriptionStreak;
    private final Permission permission = new Ultimate();

    public PlusSubscription(AuroraMCPlayer player) {
        this.player = player;

        //TODO: load stuff from DB.
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

    public Character getLeveLColor() {
        return leveLColor;
    }

    public char getUltimateIcon() {
        return ultimateIcon;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getMonthsSubscribed() {
        return monthsSubscribed;
    }

    public int getSubscriptionStreak() {
        return subscriptionStreak;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public String getClickURL() {
        return clickURL;
    }

    public String getHoverText() {
        return hoverText;
    }
}
