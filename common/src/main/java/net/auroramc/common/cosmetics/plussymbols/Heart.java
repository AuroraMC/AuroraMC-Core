/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;


import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Heart extends PlusSymbol {

    public Heart() {
        super(207, "Heart", "&3&l❤ Heart", "I <3 U", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "DIAMOND", (short)0, '❤', Rarity.LEGENDARY);
    }

}
