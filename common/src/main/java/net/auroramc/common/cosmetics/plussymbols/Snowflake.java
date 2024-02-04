/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Rank;


import java.util.Collections;

public class Snowflake extends PlusSymbol {

    public Snowflake() {
        super(205, "Snowflake", "&3&l❆ Snowflake", "Do you want to build a Snowman?", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MASTER), "&dPurchase Master at store.auroramc.net to unlock this Plus Symbol!", true, "SNOW_BLOCK", (short)0, '❆', Rarity.RARE);
    }
}
