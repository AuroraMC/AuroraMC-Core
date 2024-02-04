/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
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
