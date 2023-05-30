/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Radioactive extends PlusSymbol {

    public Radioactive() {
        super(208, "Radioactive", "&3&l☢ Radioactive", "DANGER! DANGER!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "LAVA_BUCKET", (short)0, '☢', Rarity.UNCOMMON);
    }
}
