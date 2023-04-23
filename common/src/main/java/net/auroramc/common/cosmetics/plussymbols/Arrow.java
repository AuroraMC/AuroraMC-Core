/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Arrow extends PlusSymbol {

    public Arrow() {
        super(215, "Arrow", "&3&l➹ Arrow", "This way!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "ARROW", (short)0, '➹', Rarity.COMMON);
    }
}
