package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Hazard extends PlusSymbol {

    public Hazard() {
        super(220, "Hazard", "&3&l☣ Hazard", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.BARRIER, (short)0, '☣');
    }
}
