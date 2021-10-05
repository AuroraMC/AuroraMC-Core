package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Balanced extends PlusSymbol {

    public Balanced() {
        super(217, "Balanced", "&3&l☯ Balanced", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.CAKE, (short)0, '☯');
    }
}
