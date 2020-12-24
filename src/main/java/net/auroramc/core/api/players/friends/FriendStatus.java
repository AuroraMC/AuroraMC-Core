package net.auroramc.core.api.players.friends;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Permission;

public enum FriendStatus {

    ONLINE("Online", "Online", 'a', null, null, true),
    OFFLINE("Offline", "Offline", '7', null, null, true),
    IDLE("Idle", "Idle", 'e', null, null, true),
    DO_NOT_DISTURB("Do Not Disturb", "Do Not Disturb", 'c', null, null, true),
    AFK("Away From Keyboard", "AFK", '6', null, null, true),
    COUNTING_MONEY("Counting Money", "Counting Money", 'b', AuroraMCAPI.getPermissions().get("elite"), "Purchase Elite at store.auroramc.net to gain access to this status.", true),
    MASTERING_ALL_THE_GAMES("Mastering all the games", "Mastering all the games", 'd', AuroraMCAPI.getPermissions().get("master"), "Purchase Master at store.auroramc.net to gain access to this status.", true),
    RECORDING("Recording a video", "Recording a video", '6', AuroraMCAPI.getPermissions().get("social"), null, false),
    LIVE_STREAMING("Live Streaming", "Live Streaming", '5', AuroraMCAPI.getPermissions().get("social"), null, false),
    CREATING_NEW_MAPS("Creating New Maps", "Creating New Maps", 'a', AuroraMCAPI.getPermissions().get("build"), null, false),
    PROCESSING_REPORTS("&6&k0&r &9&lProcessing Reports &6&k0", "&6&k0&r &9Processing Reports &6&k0", '9', AuroraMCAPI.getPermissions().get("moderation"), null, false),
    SWINGING_THE_BAN_HAMMER("&6&k0&r &9&lSwinging the Ban Hammer &6&k0", "&6&k0&r &9Swinging the Ban Hammer &6&k0", '9', AuroraMCAPI.getPermissions().get("approval.bypass"), null, false),
    PROGRAMMING("&2&k0&r &a&ldeveloper.setProgramming(true); &2&k0", "&2&k0&r &adeveloper.setProgramming(true) &2&k0", 'a', AuroraMCAPI.getPermissions().get("debug.info"), null, false),
    RUINING_LIVES("&d&l&k0&r &e&lR&b&lu&e&li&b&ln&e&li&b&ln&e&lg &b&lL&e&li&b&lv&e&le&b&ls &d&l&k0", "&d&k0&r &eR&bu&ei&bn&ei&bn&eg &bL&ei&bv&ee&bs &d&k0", 'f', AuroraMCAPI.getPermissions().get("all"), null, false);
    private final String title;
    private final String name;
    private final char colour;
    private final Permission permission;
    private final String customPermissionText;
    private final boolean showInGUI;

    FriendStatus(String title, String name, char colour, Permission permission, String customPermissionText, boolean showInGUI) {
        this.colour = colour;
        this.name = name;
        this.title = title;
        this.permission = permission;
        this.customPermissionText = customPermissionText;
        this.showInGUI = showInGUI;
    }

    public String getName() {
        return name;
    }

    public char getColour() {
        return colour;
    }

    public String getTitle() {
        return title;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getCustomPermissionText() {
        return customPermissionText;
    }

    public boolean showInGUI() {
        return showInGUI;
    }
}
