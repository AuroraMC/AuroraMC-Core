/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.hats;

import net.auroramc.core.api.cosmetics.Hat;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;
import java.util.List;

public class Astronaut extends Hat {


    public Astronaut() {
        super(315, "Astronaut", "&7&lAstronaut", "Out of this world!", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "This is a bug, please report this.", "e4f53f3d996eb6303abc98b42f4af0e7f9459c80a886d1c6aac25e8ab033f376", true, Rarity.UNCOMMON);
    }
}
