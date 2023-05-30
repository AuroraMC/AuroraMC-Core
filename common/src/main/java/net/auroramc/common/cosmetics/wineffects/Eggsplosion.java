/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.wineffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.WinEffect;


import java.util.Collections;

public class Eggsplosion extends WinEffect {

    public Eggsplosion() {
        super(602, "EGGsplosion", "&7&lEGGsplosion", "What an EGGcelent end to a game!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Win Effect!", true, "EGG", (short)0, Rarity.EPIC);
    }
}
