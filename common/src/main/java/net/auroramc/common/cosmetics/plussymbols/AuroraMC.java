/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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
