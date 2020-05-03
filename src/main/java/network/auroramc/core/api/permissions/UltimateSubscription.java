package network.auroramc.core.api.permissions;

import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.permissions.permissions.Ultimate;

import java.util.Date;

public final class UltimateSubscription {

    private final AuroraMCPlayer player;
    private static final char ultimateIcon = '✦';
    private static final String hoverText = "&%s&l✦ Ultimate\n" +
            "\n" +
            "&fUltimate is a subscription based\n" +
            "&fperk where you recieve exclusive\n" +
            "&fin-game benefits and offers.";
    private static final String clickURL = "http://store.block2block.me/";
    private Character color;
    private Date endDate;
    private int monthsSubscribed;
    private int subscriptionStreak;
    private final Permission permission = new Ultimate();

    public UltimateSubscription(AuroraMCPlayer player) {
        this.player = player;

        //TODO: load stuff from DB.
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public char getColor() {
        return ((color == null)?'6':color);
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
