/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Hazard extends PlusSymbol {

    public Hazard() {
        super(220, "Hazard", "&3&l☣ Hazard", "WARNING! WARNING!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "BARRIER", (short)0, '☣', Rarity.UNCOMMON);
    }
}
