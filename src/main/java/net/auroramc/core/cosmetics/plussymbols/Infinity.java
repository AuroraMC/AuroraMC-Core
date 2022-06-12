/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Infinity extends PlusSymbol {

    public Infinity() {
        super(214, "Infinity", "&3&l∞ Infinity", "It goes on and on and on and on", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.BEDROCK, (short)0, '∞', Rarity.UNCOMMON);
    }
}
