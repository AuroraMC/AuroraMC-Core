package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.Collections;

public class Trademark extends PlusSymbol {

    public Trademark() {
        super(204, "Trademark", "&3&l™ Trademark", "Coming Soon™!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ADMIN), "", false, Material.DIAMOND_AXE, (short)0, '™');
    }
}
