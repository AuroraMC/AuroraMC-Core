/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Hazard extends PlusSymbol {

    public Hazard() {
        super(220, "Hazard", "&3&l☣ Hazard", "WARNING! WARNING!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "BARRIER", (short)0, '☣', Rarity.UNCOMMON);
    }
}
