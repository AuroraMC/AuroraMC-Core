/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.Collections;

public class Copyright extends PlusSymbol {

    public Copyright() {
        super(204, "Copyright", "&3&l© Copyright", "Coming Soon™", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ADMIN), "", false, Material.DIAMOND_AXE, (short)0, '©');
    }
}
