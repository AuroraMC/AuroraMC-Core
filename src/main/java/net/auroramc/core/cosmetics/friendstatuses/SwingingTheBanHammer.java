package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;

public class SwingingTheBanHammer extends FriendStatus {

    public SwingingTheBanHammer() {
        super(111, "&6&k0&r &9Swinging the Ban Hammer &6&k0", "&6&k0&r &9&lSwinging the Ban Hammer &6&k0", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MODERATOR), "", "&6&k0&r &9&lSwinging the Ban Hammer &6&k0", '9', false);
    }
}
