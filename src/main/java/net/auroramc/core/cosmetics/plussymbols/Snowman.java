/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import net.auroramc.core.api.permissions.Permission;
import org.bukkit.Material;

import java.util.Collections;

public class Snowman extends PlusSymbol {

    public Snowman() {
        super(201, "Snowman", "&3&l☃ Snowman", "Balls of snow!", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), Collections.emptyList(), "&bYou must have an active Plus Subscription to use Plus Symbols.", true, Material.SNOW_BALL, (short)0, '☃', Rarity.COMMON);
    }
}
