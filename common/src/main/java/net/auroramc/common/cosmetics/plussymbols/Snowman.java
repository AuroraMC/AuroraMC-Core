/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Permission;


import java.util.Collections;

public class Snowman extends PlusSymbol {

    public Snowman() {
        super(201, "Snowman", "&3&l☃ Snowman", "Balls of snow!", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "&bYou must have an active Plus Subscription to use Plus Symbols.", true, "SNOW_BALL", (short)0, '☃', Rarity.UNCOMMON);
    }
}
