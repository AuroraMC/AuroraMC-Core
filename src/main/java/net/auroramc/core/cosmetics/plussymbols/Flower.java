package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Flower extends PlusSymbol {

    public Flower() {
        super(212, "Flower", "&3&l✿ Flower", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.RED_ROSE, (short)0, '✿');
    }
}
