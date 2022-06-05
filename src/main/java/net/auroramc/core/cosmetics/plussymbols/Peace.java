/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Peace extends PlusSymbol {

    public Peace() {
        super(209, "Peace", "&3&l✌ Peace", "It's all cool brooo", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at;store.auroramc.net to unlock this plus symbol!", true, Material.SLIME_BALL, (short)0, '✌', Rarity.EPIC);
    }
}
