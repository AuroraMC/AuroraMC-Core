/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Smile extends PlusSymbol {

    public Smile() {
        super(218, "Smile", "&3&lツ Smile", ":)", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "BEACON", (short)0, 'ツ', Rarity.LEGENDARY);
    }
}
