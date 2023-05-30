/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Permission;

import java.util.Collections;

public class ProcessingReports extends FriendStatus {

    public ProcessingReports() {
        super(110, "&6&k0&r &9Processing Reports &6&k0&r", "&6&k0&r &9&lProcessing Reports &6&k0&r", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.MODERATION), Collections.emptyList(), "", "&6&k0&r &9&lProcessing Reports &6&k0&r", '9', false, Rarity.MYTHICAL);
    }
}
