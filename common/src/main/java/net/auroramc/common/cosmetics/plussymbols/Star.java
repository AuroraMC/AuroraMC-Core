/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Permission;


import java.util.Collections;

public class Star extends PlusSymbol {

    public Star() {
        super(200, "Star", "&3&l✦ Star", "Show your shooting star status with this awesome Plus Symbol!", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "&bYou must have an active Plus Subscription to use Plus Symbols.", true, "NETHER_STAR", (short)0, '✦', Rarity.UNCOMMON);
    }
}
