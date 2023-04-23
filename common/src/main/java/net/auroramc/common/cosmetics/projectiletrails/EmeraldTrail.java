/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.projectiletrails;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.ProjectileTrail;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;

import java.util.Collections;
import java.util.Random;

public class EmeraldTrail extends ProjectileTrail {
    public EmeraldTrail() {
        super(1001, "Emerald Trail", TextFormatter.convert("&a&lEmerald Trail"), "A trail of Emeralds. Kinda. Sort of.", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at store.auroramc.net to unlock this Projectile Trail!", true, "EMERALD", (short)0, Rarity.EPIC);
    }
}
