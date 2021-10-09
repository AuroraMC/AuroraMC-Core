/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Permission;

import java.util.Collections;

public class Recording extends FriendStatus {

    public Recording() {
        super(107, "Recording a video", "Recording a video", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.SOCIAL), Collections.emptyList(), "", "Recording a video", '6', false);
    }
}
