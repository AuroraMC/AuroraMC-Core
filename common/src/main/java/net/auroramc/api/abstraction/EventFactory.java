/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;

public class EventFactory {


    public static void firePreferenceEvent(AuroraMCPlayer player) {
        AuroraMCAPI.getAbstractedMethods().firePreferenceEvent(player);
    }


}
