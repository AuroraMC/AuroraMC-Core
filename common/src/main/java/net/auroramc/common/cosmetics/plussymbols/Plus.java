/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Permission;


import java.util.Collections;

public class Plus extends PlusSymbol {

    public Plus() {
        super(206, "Plus", "&3&l✚ Plus", "Show off your Plus Subscription with this Plus Symbol!", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "Subscribe to Plus at store.auroramc.net to unlock this symbol!", true, "COOKIE", (short)0, '✚', Rarity.UNCOMMON);
    }
}
