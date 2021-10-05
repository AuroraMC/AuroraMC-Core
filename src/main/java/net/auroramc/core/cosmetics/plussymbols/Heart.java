package net.auroramc.core.cosmetics.plussymbols;


import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Heart extends PlusSymbol {

    public Heart() {
        super(207, "Heart", "&3&l❤ Heart", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.DIAMOND, (short)0, '❤');
    }

}
