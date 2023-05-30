/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
