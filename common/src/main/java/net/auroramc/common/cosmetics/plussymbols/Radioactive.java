/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Radioactive extends PlusSymbol {

    public Radioactive() {
        super(208, "Radioactive", "&3&l☢ Radioactive", "DANGER! DANGER!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "LAVA_BUCKET", (short)0, '☢', Rarity.UNCOMMON);
    }
}
