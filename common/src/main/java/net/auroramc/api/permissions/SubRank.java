/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.permissions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SubRank {

    QUALITY_ASSURANCE(2, "Quality Assurance", Collections.singletonList(Permission.DEBUG_INFO), 85, 255, 85, 'a'),
    QUALITY_ASSURANCE_MANAGEMENT(3, "Quality Assurance Management", Arrays.asList(Permission.DEBUG_INFO, Permission.DEBUG_ACTION, Permission.PANEL), 85, 255, 85, 'a'),
    STAFF_MANAGEMENT(4, "Staff Management", Arrays.asList(Permission.STAFF_MANAGEMENT, Permission.DISGUISE, Permission.CUSTOM_DISGUISE, Permission.PANEL), 255, 170, 0, '6'),
    SUPPORT(5, "Support", Collections.singletonList(Permission.SUPPORT), 85, 85, 255, '9'),
    RECRUITMENT(6, "Recruitment", Arrays.asList(Permission.RECRUITMENT, Permission.DISGUISE, Permission.CUSTOM_DISGUISE), 255, 170, 0, '6'),
    SOCIAL_MEDIA(7, "Social Media", Collections.singletonList(Permission.SOCIAL_MEDIA), 85, 85, 255, '9'),
    EVENT_MANAGEMENT(8, "Event Management", Collections.singletonList(Permission.EVENT_MANAGEMENT), 0, 170, 0, '2'),
    COMMUNITY_MANAGEMENT(9, "Community Management", Collections.emptyList(), 0, 170, 0, '2'),
    RULES_COMMITTEE(10, "Rules Committee", Collections.singletonList(Permission.PANEL), 255, 170, 0, '6'),
    APPEALS(11, "Appeals Team", Collections.singletonList(Permission.PANEL), 0, 170, 0, '2'),
    TESTER(12, "Alpha Tester", Collections.emptyList(), 0, 170, 0, '2');


    private final int id;
    private final String name;
    private final List<Permission> permissions;
    private final char menuColor;
    private final int r;
    private final int g;
    private final int b;

    SubRank(@NotNull int id, @NotNull String name, @NotNull List<Permission> permissions, int r, int g, int b, char menuColor) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.r = r;
        this.g = g;
        this.b = b;
        this.menuColor = menuColor;
    }

    public final String getName() {
        return name;
    }

    public final List<Permission> getPermissions() {
        return new ArrayList<>(permissions);
    }

    public final boolean hasPermission(String node) {
        for (Permission permission : getPermissions()) {
            if (permission.getNode().equals(node) || permission.getNode().equals("all")) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasPermission(int id) {
        for (Permission permission : getPermissions()) {
            if (permission.getId() == id || permission.getId() == -1) {
                return true;
            }
        }


        return false;
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public int getId() {
        return id;
    }

    public char getMenuColor() {
        return menuColor;
    }

    public static SubRank getByID(int id) {
        for (SubRank subRank : values()) {
            if (subRank.getId() == id) {
                return subRank;
            }
        }

        return null;
    }
}
