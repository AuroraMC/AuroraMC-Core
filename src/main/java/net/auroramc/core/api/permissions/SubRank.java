/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.permissions;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SubRank {

    JUNIOR_QUALITY_ASSURANCE(2, "Junior Quality Assurance", Collections.singletonList(Permission.DEBUG_INFO), Color.fromRGB(85, 255, 85), 'a'),
    SENIOR_QUALITY_ASSURANCE(3, "Senior Quality Assurance", Arrays.asList(Permission.DEBUG_INFO, Permission.DEBUG_ACTION, Permission.PANEL), Color.fromRGB(85, 255, 85), 'a'),
    STAFF_MANAGEMENT(4, "Staff Management", Arrays.asList(Permission.STAFF_MANAGEMENT, Permission.DISGUISE, Permission.CUSTOM_DISGUISE, Permission.PANEL), Color.fromRGB(255, 170, 0), '6'),
    SUPPORT(5, "Support", Collections.singletonList(Permission.SUPPORT), Color.fromRGB(85, 85, 255), '9'),
    RECRUITMENT(6, "Recruitment", Arrays.asList(Permission.RECRUITMENT, Permission.DISGUISE, Permission.CUSTOM_DISGUISE), Color.fromRGB(255, 170, 0), '6'),
    SOCIAL_MEDIA(7, "Social Media", Collections.singletonList(Permission.SOCIAL_MEDIA), Color.fromRGB(85, 85, 255), '9'),
    EVENT_MANAGEMENT(8, "Event Management", Collections.singletonList(Permission.EVENT_MANAGEMENT), Color.fromRGB(0, 170, 0), '2'),
    COMMUNITY_MANAGEMENT(9, "Community Management", Collections.emptyList(), Color.fromRGB(0, 170, 0), '2'),
    RULES_COMMITTEE(10, "Rules Committee", Collections.singletonList(Permission.PANEL), Color.fromRGB(255, 170, 0), '6'),
    APPEALS(11, "Appeals Team", Collections.singletonList(Permission.PANEL), Color.fromRGB(0, 170, 0), '2'),
    TESTER(12, "Alpha Tester", Collections.emptyList(), Color.fromRGB(0, 170, 0), '2');


    private final int id;
    private final String name;
    private final List<Permission> permissions;
    private final Color color;
    private final char menuColor;

    SubRank(@NotNull int id, @NotNull String name, @NotNull List<Permission> permissions, @NotNull Color color, @NotNull char menuColor) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.color = color;
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

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
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
