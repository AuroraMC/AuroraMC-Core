/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class ShootingStar extends PlusSymbol {

    public ShootingStar() {
        super(210, "Shooting Star", "&3&l☄ Shooting Star", "Make a wish!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "NETHER_STAR", (short)0, '☄', Rarity.RARE);
    }
}
