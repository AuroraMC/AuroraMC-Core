/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Permission;
import org.bukkit.Material;

import java.util.Collections;

public class Plus extends PlusSymbol {

    public Plus() {
        super(206, "Plus", "&3&l✚ Plus", "Show off your Plus Subscription with this Plus Symbol!", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "Subscribe to Plus at store.auroramc.net to unlock this symbol!", true, Material.COOKIE, (short)0, '✚');
    }
}
