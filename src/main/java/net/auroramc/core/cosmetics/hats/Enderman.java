/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.hats;

import net.auroramc.core.api.cosmetics.Hat;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;
import java.util.List;

public class Enderman extends Hat {
    public Enderman() {
        super(361, "Enderman", "&5Enderman", "Become an enderman.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the End Bundle at store.auroramc.net to unlock this Particle Effect!", "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951", true, Rarity.EPIC);
    }
}
