/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
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
