/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.plussymbols.kitrewards;

import net.auroramc.api.cosmetics.PlusSymbol;


import java.util.Collections;

public class Sword extends PlusSymbol {
    public Sword() {
        super(222, "Sword", "&6&l⚔ Sword", "", UnlockMode.LEVEL, -1, Collections.emptyList(), Collections.emptyList(), "Reach Crystal Quest Fighter Kit Prestige 1 to unlock this Plus Symbol!", false, "DIAMOND_SWORD", (short)0, '⚔', Rarity.MYTHICAL);
    }
}
