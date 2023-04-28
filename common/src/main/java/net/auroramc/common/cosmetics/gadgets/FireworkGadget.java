/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.gadgets;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Gadget;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;

import java.util.Collections;
import java.util.Random;

public class FireworkGadget extends Gadget {
    public FireworkGadget() {
        super(800, TextFormatter.rainbow("Firework Gadget").toLegacyText(), TextFormatter.rainbow("Firework Gadget").toLegacyText(), "&oYou're actually giving us permission to do this? &7That is correct, Longbottom. &oTo blow it up? Boom? &7BOOM!", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(),"Purchase the Grand Celebration Bundle at store.auroramc.net to unlock this Gadget!", true, "FIREWORK", (short)0, Rarity.LEGENDARY, 2);
    }
}
