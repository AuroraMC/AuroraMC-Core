package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Smile extends PlusSymbol {

    public Smile() {
        super(218, "Smile", "&3&lツ Smile", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.BEACON, (short)0, 'ツ');
    }
}
