/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;
import net.auroramc.api.permissions.Rank;


import java.util.Collections;

public class AuroraMC extends PlusSymbol {

    public AuroraMC() {
        super(203, "AuroraMC", "&3&lⒶ AuroraMC", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.JUNIOR_MODERATOR), "", false, "ANVIL", (short)0, 'Ⓐ', Rarity.MYTHICAL);
    }
}
