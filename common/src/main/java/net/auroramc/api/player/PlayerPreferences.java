/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.utils.Pronoun;

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

    public void setPreferredPronouns(Pronoun preferredPronouns, boolean send) {
        this.preferredPronouns = preferredPronouns;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setPreferredPronouns(player, preferredPronouns));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PreferredPronounsChanged");
            out.writeUTF(player.getName());
            out.writeUTF(preferredPronouns.name());
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void setApprovalNotifications(boolean approvalNotifications, boolean send) {
        this.approvalNotifications = approvalNotifications;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setApprovalNotifications(player, approvalNotifications));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ApprovalNotificationsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(approvalNotifications);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void setApprovalProcessedNotifications(boolean approvalProcessedNotifications, boolean send) {
        this.approvalProcessedNotifications = approvalProcessedNotifications;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setApprovalProcessedNotifications(player, approvalProcessedNotifications));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ApprovalProcessedNotificationsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(approvalProcessedNotifications);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void setChatVisibility(boolean chatVisibility, boolean send) {
        this.chatVisibility = chatVisibility;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setChatVisibility(player, chatVisibility));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ChatVisibilityChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(chatVisibility);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setFriendRequests(boolean friendRequests, boolean send) {
        this.friendRequests = friendRequests;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setFriendRequests(player, friendRequests));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendRequestsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(friendRequests);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setHideDisguiseName(boolean hideDisguiseName, boolean send) {
        this.hideDisguiseName = hideDisguiseName;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setHideDisguiseName(player, hideDisguiseName));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HideDisguiseNameChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(hideDisguiseName);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setHubFlight(boolean hubFlight, boolean send) {
        this.hubFlight = hubFlight;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setHubFlight(player, hubFlight));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HubFlightChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(hubFlight);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setHubForcefield(boolean hubForcefield, boolean send) {
        this.hubForcefield = hubForcefield;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setHubForcefield(player, hubForcefield));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HubForcefieldChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(hubForcefield);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setHubInvisibility(boolean hubInvisibility, boolean send) {
        this.hubInvisibility = hubInvisibility;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setHubInvisibility(player, hubInvisibility));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HubInvisibilityChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(hubInvisibility);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setHubSpeed(boolean hubSpeed, boolean send) {
        this.hubSpeed = hubSpeed;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setHubSpeed(player, hubSpeed));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HubSpeedChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(hubSpeed);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setHubVisibility(boolean hubVisibility, boolean send) {
        this.hubVisibility = hubVisibility;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setHubVisibility(player, hubVisibility));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HubVisibilityChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(hubVisibility);
            player.sendPluginMessage(out.toByteArray());
        }

    }

    public void setIgnoreHubKnockback(boolean ignoreHubKnockback, boolean send) {
        this.ignoreHubKnockback = ignoreHubKnockback;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setIgnoreHubKnockback(player, ignoreHubKnockback));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("IgnoreHubKnockbackChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(ignoreHubKnockback);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setMuteInformMode(MuteInformMode muteInformMode, boolean send) {
        this.muteInformMode = muteInformMode;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setMuteInformMode(player, muteInformMode));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("MuteInformModeChanged");
            out.writeUTF(player.getName());
            out.writeUTF(muteInformMode.name());
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setPartyRequests(boolean partyRequests, boolean send) {
        this.partyRequests = partyRequests;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setPartyRequests(player, partyRequests));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PartyRequestsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(partyRequests);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setPingOnPartyChat(boolean pingOnPartyChat, boolean send) {
        this.pingOnPartyChat = pingOnPartyChat;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setPingOnPartyChat(player, pingOnPartyChat));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PingOnPartyChatChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(pingOnPartyChat);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setPingOnPrivateMessage(boolean pingOnPrivateMessage, boolean send) {
        this.pingOnPrivateMessage = pingOnPrivateMessage;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setPingOnPrivateMessage(player, pingOnPrivateMessage));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PingOnPrivateMessageChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(pingOnPrivateMessage);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setPingOnChatMention(boolean pingOnChatMention, boolean send) {
        this.pingOnChatMention = pingOnChatMention;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setPingOnChatMention(player, pingOnChatMention));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PingOnChatMentionChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(pingOnChatMention);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setPrivateMessageMode(PrivateMessageMode privateMessageMode, boolean send) {
        this.privateMessageMode = privateMessageMode;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setPrivateMessageMode(player, privateMessageMode));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PrivateMessageModeChanged");
            out.writeUTF(player.getName());
            out.writeUTF(privateMessageMode.name());
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setReportNotifications(boolean reportNotifications, boolean send) {
        this.reportNotifications = reportNotifications;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setReportNotifications(player, reportNotifications));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ReportNotificationsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(reportNotifications);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setSocialMediaNotifications(boolean socialMediaNotifications, boolean send) {
        this.socialMediaNotifications = socialMediaNotifications;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setSocialMediaNotifications(player, socialMediaNotifications));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SocialMediaNotificationsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(socialMediaNotifications);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public void setStaffLoginNotifications(boolean staffLoginNotifications, boolean send) {
        this.staffLoginNotifications = staffLoginNotifications;

        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().setStaffLoginNotifications(player, staffLoginNotifications));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("StaffLoginNotificationsChanged");
            out.writeUTF(player.getName());
            out.writeBoolean(staffLoginNotifications);
            player.sendPluginMessage(out.toByteArray());
        }


    }

    public enum MuteInformMode {DISABLED, MESSAGE_ONLY, MESSAGE_AND_MENTIONS}
    public enum PrivateMessageMode {DISABLED, FRIENDS_ONLY, ALL}
}
