/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
