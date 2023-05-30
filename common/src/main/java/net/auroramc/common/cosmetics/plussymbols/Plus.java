/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
