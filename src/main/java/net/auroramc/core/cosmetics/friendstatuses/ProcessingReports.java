package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Permission;

import java.util.Collections;

public class ProcessingReports extends FriendStatus {

    public ProcessingReports() {
        super(110, "&6&k0&r &9Processing Reports &6&k0", "&6&k0&r &9&lProcessing Reports &6&k0", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.MODERATION), Collections.emptyList(), "", "&6&k0&r &9&lProcessing Reports &6&k0", '9', false);
    }
}