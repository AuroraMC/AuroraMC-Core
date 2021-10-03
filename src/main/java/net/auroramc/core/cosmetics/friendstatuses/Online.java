package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Online extends FriendStatus {

    public Online() {
        super(100, "Online", "Online", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Online", 'a', true);
    }
}
