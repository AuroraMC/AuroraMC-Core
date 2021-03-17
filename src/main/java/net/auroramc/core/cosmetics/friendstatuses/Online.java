package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;
import java.util.List;

public class Online extends FriendStatus {

    public Online() {
        super(100, "Online", "Online", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Online", 'a', true);
    }
}
