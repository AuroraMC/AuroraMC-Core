/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.utils;


import net.auroramc.api.permissions.Permission;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MaintenanceMode {

    STAFF_ONLY("Staff Only", Arrays.asList(Permission.MODERATION, Permission.DEBUG_INFO, Permission.ADMIN, Permission.BUILD)),
    LEADERSHIP_ONLY("Leadership Only", Arrays.asList(Permission.DEBUG_ACTION, Permission.ADMIN)),
    LOCKDOWN("Essential Staff Only", Collections.singletonList(Permission.ALL)),
    NOT_OPEN("Not Open", Arrays.asList(Permission.MODERATION, Permission.BUILD));

    private final String title;
    private final List<Permission> allowance;

    MaintenanceMode(String title, List<Permission> allowance) {
        this.title = title;
        this.allowance = allowance;
    }

    public List<Permission> getAllowance() {
        return allowance;
    }

    public String getTitle() {
        return title;
    }
}
