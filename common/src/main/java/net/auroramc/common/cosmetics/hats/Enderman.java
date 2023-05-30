/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.hats;

import net.auroramc.api.cosmetics.Hat;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;
import java.util.List;

public class Enderman extends Hat {
    public Enderman() {
        super(361, "Enderman", "&5Enderman", "Become an enderman.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Hat!", "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951", true, Rarity.EPIC);
    }
}
