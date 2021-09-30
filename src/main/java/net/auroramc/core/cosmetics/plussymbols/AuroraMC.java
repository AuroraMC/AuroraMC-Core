package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Material;

import java.util.Collections;

public class AuroraMC extends PlusSymbol {

    public AuroraMC() {
        super(203, "AuroraMC", "&3&lⒶ AuroraMC", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH!", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.JUNIOR_MODERATOR), "", false, Material.ANVIL, (short)0, 'Ⓐ');
    }
}
