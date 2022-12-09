/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols.kitrewards;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Sword extends PlusSymbol {
    public Sword() {
        super(222, "Sword", "&6&l⚔ Sword", "", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "Reach Crystal Quest Fighter Kit Prestige 1 to unlock this Plus Symbol!", false, Material.DIAMOND_SWORD, (short)0, '⚔', Rarity.MYTHICAL);
    }
}
