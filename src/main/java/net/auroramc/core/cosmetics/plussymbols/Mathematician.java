package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Mathematician extends PlusSymbol {

    public Mathematician() {
        super(211, "Mathematician", "&3&l½ Mathematician", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.BOOKSHELF, (short)0, '½');
    }
}
