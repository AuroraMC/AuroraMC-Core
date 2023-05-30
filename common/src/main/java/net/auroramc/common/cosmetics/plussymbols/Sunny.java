/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Sunny extends PlusSymbol {

    public Sunny() {
        super(219, "Sunny", "&3&l☀ Sunny", "Bring me sunshine!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "DOUBLE_PLANT", (short)0, '☀', Rarity.COMMON);
    }
}
