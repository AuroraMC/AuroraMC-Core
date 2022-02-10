/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.Collections;

public class Snowflake extends PlusSymbol {

    public Snowflake() {
        super(205, "Snowflake", "&3&l❆ Snowflake", "Do you want to build a Snowman?", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.MASTER), "&dPurchase Master at store.auroramc.net to unlock this Plus Symbol!", true, Material.SNOW_BLOCK, (short)0, '❆', Rarity.COMMON);
    }
}
