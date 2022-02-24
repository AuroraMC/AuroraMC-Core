/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.Collections;

public class Tea extends PlusSymbol {

    public Tea() {
        super(202, "Tea", "&3&l☕ Tea", "Show off how British you are with this Tea Plus Symbol!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ELITE), "&bPurchase Elite at store.auroramc.net to unlock this Plus Symbol!", true, Material.GLASS_BOTTLE, (short)0, '☕', Rarity.COMMON);
    }
}
