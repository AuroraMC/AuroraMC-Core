/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.events.player.PlayerPreferenceChangeEvent;
import net.auroramc.core.api.utils.Pronoun;
import org.bukkit.Bukkit;

public class PlayerPreferences {

    private AuroraMCPlayer player;

    //Social Prefs
    private boolean friendRequests;
    private boolean partyRequests;
    private MuteInformMode muteInformMode;
    private Pronoun preferredPronouns;

    //Chat Prefs
    private boolean chatVisibility;
    private PrivateMessageMode privateMessageMode;
    private boolean pingOnPrivateMessage;
    private boolean pingOnPartyChat;
    private boolean pingOnChatMention;

    //Game Prefs

    //Misc Prefs
    private boolean hubVisibility;
    private boolean hubSpeed;
    private boolean hubFlight;
    private boolean reportNotifications;

    //Staff Prefs
    private boolean hubInvisibility;
    private boolean ignoreHubKnockback;
    private boolean socialMediaNotifications;

    //Staff Management Prefs
    private boolean staffLoginNotifications;
    private boolean approvalNotifications;
    private boolean approvalProcessedNotifications;

    //Social Media Prefs
    private boolean hubForcefield;
    private boolean hideDisguiseName;

    public PlayerPreferences(AuroraMCPlayer player, boolean friendRequests, boolean partyRequests, MuteInformMode muteInformMode, boolean chatVisibility, PrivateMessageMode privateMessageMode, boolean pingOnPrivateMessage, boolean pingOnPartyChat, boolean hubVisibility, boolean hubSpeed, boolean hubFlight, boolean reportNotifications, boolean hubInvisibility, boolean ignoreHubKnockback, boolean socialMediaNotifications, boolean staffLoginNotifications, boolean approvalNotifications, boolean approvalProcessedNotifications, boolean hubForcefield, boolean hideDisguiseName, boolean pingOnChatMention, Pronoun preferredPronouns) {
        this.player = player;
        this.friendRequests = friendRequests;
        this.partyRequests = partyRequests;
        this.muteInformMode = muteInformMode;
        this.chatVisibility = chatVisibility;
        this.privateMessageMode = privateMessageMode;
        this.pingOnPrivateMessage = pingOnPrivateMessage;
        this.pingOnChatMention = pingOnChatMention;
        this.pingOnPartyChat = pingOnPartyChat;
        this.hubVisibility = hubVisibility;
        this.hubSpeed = hubSpeed;


        this.hubFlight = hubFlight;
        this.reportNotifications = reportNotifications;
        this.hubInvisibility = hubInvisibility;
        this.ignoreHubKnockback = ignoreHubKnockback;
        this.socialMediaNotifications = socialMediaNotifications;
        this.staffLoginNotifications = staffLoginNotifications;
        this.approvalNotifications = approvalNotifications;
        this.approvalProcessedNotifications = approvalProcessedNotifications;
        this.hubForcefield = hubForcefield;
        this.hideDisguiseName = hideDisguiseName;
        this.preferredPronouns = preferredPronouns;
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public boolean isApprovalNotificationsEnabled() {
        return approvalNotifications;
    }

    public boolean isApprovalProcessedNotificationsEnabled() {
        return approvalProcessedNotifications;
    }

    public boolean isChatVisibilityEnabled() {
        return chatVisibility;
    }

    public boolean isFriendRequestsEnabled() {
        return friendRequests;
    }

    public boolean isHideDisguiseNameEnabled() {
        return hideDisguiseName;
    }

    public boolean isHubFlightEnabled() {
        return hubFlight;
    }

    public boolean isHubForcefieldEnabled() {
        return hubForcefield;
    }

    public boolean isHubInvisibilityEnabled() {
        return hubInvisibility;
    }

    public boolean isHubSpeedEnabled() {
        return hubSpeed;
    }

    public boolean isHubVisibilityEnabled() {
        return hubVisibility;
    }

    public boolean isIgnoreHubKnockbackEnabled() {
        return ignoreHubKnockback;
    }

    public boolean isPartyRequestsEnabled() {
        return partyRequests;
    }

    public boolean isPingOnPartyChatEnabled() {
        return pingOnPartyChat;
    }

    public boolean isPingOnPrivateMessageEnabled() {
        return pingOnPrivateMessage;
    }

    public boolean isReportNotificationsEnabled() {
        return reportNotifications;
    }

    public boolean isSocialMediaNotificationsEnabled() {
        return socialMediaNotifications;
    }

    public boolean isStaffLoginNotificationsEnabled() {
        return staffLoginNotifications;
    }

    public boolean isPingOnChatMentionEnabled() {
        return pingOnChatMention;
    }

    public MuteInformMode getMuteInformMode() {
        return muteInformMode;
    }

    public PrivateMessageMode getPrivateMessageMode() {
        return privateMessageMode;
    }

    public Pronoun getPreferredPronouns() {
        return preferredPronouns;
    }

    public void setPreferredPronouns(Pronoun preferredPronouns) {
        this.preferredPronouns = preferredPronouns;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PreferredPronounsChanged");
        out.writeUTF(player.getName());
        out.writeUTF(preferredPronouns.name());
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setApprovalNotifications(boolean approvalNotifications) {
        this.approvalNotifications = approvalNotifications;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ApprovalNotificationsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(approvalNotifications);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setApprovalProcessedNotifications(boolean approvalProcessedNotifications) {
        this.approvalProcessedNotifications = approvalProcessedNotifications;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ApprovalProcessedNotificationsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(approvalProcessedNotifications);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setChatVisibility(boolean chatVisibility) {
        this.chatVisibility = chatVisibility;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ChatVisibilityChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(chatVisibility);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setFriendRequests(boolean friendRequests) {
        this.friendRequests = friendRequests;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("FriendRequestsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(friendRequests);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setHideDisguiseName(boolean hideDisguiseName) {
        this.hideDisguiseName = hideDisguiseName;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HideDisguiseNameChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(hideDisguiseName);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setHubFlight(boolean hubFlight) {
        this.hubFlight = hubFlight;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HubFlightChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(hubFlight);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setHubForcefield(boolean hubForcefield) {
        this.hubForcefield = hubForcefield;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HubForcefieldChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(hubForcefield);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setHubInvisibility(boolean hubInvisibility) {
        this.hubInvisibility = hubInvisibility;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HubInvisibilityChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(hubInvisibility);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setHubSpeed(boolean hubSpeed) {
        this.hubSpeed = hubSpeed;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HubSpeedChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(hubSpeed);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setHubVisibility(boolean hubVisibility) {
        this.hubVisibility = hubVisibility;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HubVisibilityChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(hubVisibility);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setIgnoreHubKnockback(boolean ignoreHubKnockback) {
        this.ignoreHubKnockback = ignoreHubKnockback;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IgnoreHubKnockbackChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(ignoreHubKnockback);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setMuteInformMode(MuteInformMode muteInformMode) {
        this.muteInformMode = muteInformMode;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MuteInformModeChanged");
        out.writeUTF(player.getName());
        out.writeUTF(muteInformMode.name());
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setPartyRequests(boolean partyRequests) {
        this.partyRequests = partyRequests;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PartyRequestsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(partyRequests);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setPingOnPartyChat(boolean pingOnPartyChat) {
        this.pingOnPartyChat = pingOnPartyChat;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PingOnPartyChatChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(pingOnPartyChat);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setPingOnPrivateMessage(boolean pingOnPrivateMessage) {
        this.pingOnPrivateMessage = pingOnPrivateMessage;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PingOnPrivateMessageChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(pingOnPrivateMessage);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setPingOnChatMention(boolean pingOnChatMention) {
        this.pingOnChatMention = pingOnChatMention;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PingOnChatMentionChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(pingOnChatMention);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setPrivateMessageMode(PrivateMessageMode privateMessageMode) {
        this.privateMessageMode = privateMessageMode;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PrivateMessageModeChanged");
        out.writeUTF(player.getName());
        out.writeUTF(privateMessageMode.name());
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setReportNotifications(boolean reportNotifications) {
        this.reportNotifications = reportNotifications;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ReportNotificationsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(reportNotifications);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setSocialMediaNotifications(boolean socialMediaNotifications) {
        this.socialMediaNotifications = socialMediaNotifications;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SocialMediaNotificationsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(socialMediaNotifications);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public void setStaffLoginNotifications(boolean staffLoginNotifications) {
        this.staffLoginNotifications = staffLoginNotifications;

        PlayerPreferenceChangeEvent e = new PlayerPreferenceChangeEvent(player);
        Bukkit.getPluginManager().callEvent(e);


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("StaffLoginNotificationsChanged");
        out.writeUTF(player.getName());
        out.writeBoolean(staffLoginNotifications);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public enum MuteInformMode {DISABLED, MESSAGE_ONLY, MESSAGE_AND_MENTIONS}
    public enum PrivateMessageMode {DISABLED, FRIENDS_ONLY, ALL}
}
