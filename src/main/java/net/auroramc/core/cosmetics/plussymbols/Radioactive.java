/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Radioactive extends PlusSymbol {

    public Radioactive() {
        super(208, "Radioactive", "&3&l☢ Radioactive", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.LAVA_BUCKET, (short)0, '☢');
    }
}
