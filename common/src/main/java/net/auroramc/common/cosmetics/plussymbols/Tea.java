/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Rank;


import java.util.Collections;

public class Tea extends PlusSymbol {

    public Tea() {
        super(202, "Tea", "&3&l☕ Tea", "Show off how British you are with this Tea Plus Symbol!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ELITE), "&bPurchase Elite at store.auroramc.net to unlock this Plus Symbol!", true, "GLASS_BOTTLE", (short)0, '☕', Rarity.UNCOMMON);
    }
}
