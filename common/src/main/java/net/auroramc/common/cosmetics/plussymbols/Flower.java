/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.plussymbols;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Flower extends PlusSymbol {

    public Flower() {
        super(212, "Flower", "&3&l✿ Flower", "Flower Power", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, "RED_ROSE", (short)0, '✿', Rarity.UNCOMMON);
    }
}