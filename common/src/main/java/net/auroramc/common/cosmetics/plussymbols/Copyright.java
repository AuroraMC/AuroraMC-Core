/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Rank;


import java.util.Collections;

public class Copyright extends PlusSymbol {

    public Copyright() {
        super(204, "Copyright", "&3&l© Copyright", "Coming Soon™", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ADMIN), "", false, "DIAMOND_AXE", (short)0, '©', Rarity.MYTHICAL);
    }
}
