package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class CountingSheep extends FriendStatus {

    public CountingSheep() {
        super(118, "&fCounting Sheep", "&fCounting Sheep", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", "&f&lCounting Sheep", 'f', true);
    }
}
