/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.plussymbols.kitrewards;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Castle extends PlusSymbol {
    public Castle() {
        super(223, "Castle", "&6&l♜ Castle", "", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "Reach Crystal Quest Defender Kit Prestige 1 to unlock this Plus Symbol!", false, Material.OBSIDIAN, (short)0, '♜', Rarity.MYTHICAL);
    }
}
