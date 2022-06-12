/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class ShootingStar extends PlusSymbol {

    public ShootingStar() {
        super(210, "Shooting Star", "&3&l☄ Shooting Star", "Make a wish!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.NETHER_STAR, (short)0, '☄', Rarity.RARE);
    }
}
