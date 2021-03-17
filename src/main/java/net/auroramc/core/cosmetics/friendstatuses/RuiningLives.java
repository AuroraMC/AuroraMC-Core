package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Permission;

import java.util.Collections;

public class RuiningLives extends FriendStatus {

    public RuiningLives() {
        super(113, "&d&k0&r &eR&bu&ei&bn&ei&bn&eg &bL&ei&bv&ee&bs &d&k0", "&d&l&k0&r &e&lR&b&lu&e&li&b&ln&e&li&b&ln&e&lg &b&lL&e&li&b&lv&e&le&b&ls &d&l&k0", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.ALL), Collections.emptyList(), "", "&d&l&k0&r &e&lR&b&lu&e&li&b&ln&e&li&b&ln&e&lg &b&lL&e&li&b&lv&e&le&b&ls &d&l&k0", 'a', false);
    }
}
