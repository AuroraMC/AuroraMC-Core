package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Permission;
import org.bukkit.Material;

import java.util.Collections;

public class Star extends PlusSymbol {

    public Star() {
        super(200, "Star", "&3&l✦ Star", "Show your shooting star status with this awesome Plus Symbol!", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "&bYou must have an active Plus Subscription to use Plus Symbols.", true, Material.NETHER_STAR, (short)0, '✦');
    }
}
