/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Permission;

import java.util.Collections;

public class Recording extends FriendStatus {

    public Recording() {
        super(107, "Recording a video", "Recording a video", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.SOCIAL), Collections.emptyList(), "", "Recording a video", '6', false, Rarity.MYTHICAL);
    }
}
