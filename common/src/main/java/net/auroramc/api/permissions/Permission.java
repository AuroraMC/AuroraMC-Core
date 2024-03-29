/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.permissions;

import org.jetbrains.annotations.NotNull;

public enum Permission {

    ALL(-1, "all"),
    PLAYER(0, "player"),
    ELITE(1, "elite"),
    MASTER(2, "master"),
    PLUS(3, "plus"),
    MODERATION(4, "moderation"),
    DISGUISE(5, "disguise"),
    STAFF_MANAGEMENT(6, "staffmanagement"),
    ADMIN(7, "admin"),
    DEBUG_ACTION(8, "debug.action"),
    SUPPORT(9, "support"),
    DEBUG_INFO(10, "debug.info"),
    SOCIAL(11, "social"),
    BUILD_TEAM_MANAGEMENT(12, "btm"),
    BUILD(13, "build"),
    BYPASS_APPROVAL(14, "approval.bypass"),
    RECRUITMENT(15, "recruitment"),
    SOCIAL_MEDIA(16, "socialmedia"),
    EVENT_MANAGEMENT(17, "events"),
    PANEL(18, "panel"),
    CUSTOM_DISGUISE(19, "disguise.custom"),
    EVENT_HOST(20, "event.host"),
    TESTER(21, "tester");

    private final int id;
    private final String node;

    Permission(@NotNull int id, @NotNull String node) {
        this.id = id;
        this.node = node;
    }

    public final int getId() {
        return id;
    }

    public final String getNode() {
        return node;
    }

}
