/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Sunny extends PlusSymbol {

    public Sunny() {
        super(219, "Sunny", "&3&l☀ Sunny", "Bring me sunshine!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.DOUBLE_PLANT, (short)0, '☀');
    }
}
