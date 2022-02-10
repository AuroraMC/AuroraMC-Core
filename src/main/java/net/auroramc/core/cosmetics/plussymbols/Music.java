/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Music extends PlusSymbol {

    public Music() {
        super(213, "Music", "&3&l♫ Music", "Do do do doooo", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.JUKEBOX, (short)0, '♫', Rarity.COMMON);
    }
}
