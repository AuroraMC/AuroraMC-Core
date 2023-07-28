/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerFactory {

    public static JSONArray getPluginData() {
        return AuroraMCAPI.getAbstractedMethods().getPluginData();
    }

}
