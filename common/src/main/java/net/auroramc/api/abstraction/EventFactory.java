/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.AuroraMCPlayer;

public class EventFactory {


    public static void firePreferenceEvent(AuroraMCPlayer player) {
        AuroraMCAPI.getAbstractedMethods().firePreferenceEvent(player);
    }


}
