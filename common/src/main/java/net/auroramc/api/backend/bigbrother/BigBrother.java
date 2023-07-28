/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.backend.bigbrother;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.abstraction.ServerFactory;
import net.auroramc.api.backend.info.ProxyInfo;
import net.auroramc.api.command.Command;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.DiscordWebhook;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public final class BigBrother {

    public static void logException(Throwable t) {
        logException(t, null);
    }

    private static void logException(Throwable t, AuroraMCPlayer executor) {
        logException(t, executor, null);
    }

    public static void logException(Throwable t, AuroraMCPlayer executor, String commandSyntax) {
        long timestamp = System.currentTimeMillis();
        UUID uuid = UUID.randomUUID();
        String trace = ExceptionUtils.getStackTrace(t);
        boolean proxy = AuroraMCAPI.getInfo() instanceof ProxyInfo;
        String server = AuroraMCAPI.getInfo().getName();
        JSONObject serverState = new JSONObject();
        JSONArray plugins = ServerFactory.getPluginData();
        serverState.put("plugins", plugins);
        serverState.put("network", AuroraMCAPI.getInfo().getNetwork().name());
        serverState.put("test_mode", AuroraMCAPI.isTestServer());
        ScheduleFactory.scheduleAsync(() -> {
            AuroraMCAPI.getDbManager().uploadException(timestamp, uuid, trace, commandSyntax, executor.getUuid(), proxy, server, serverState);
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1133782966213017660/PD0XP6UXxWnOTwpVE3WIteLqJFOGkFDHBBtQMxDSqyJZe748ViZiMybFzO2gF2nb4aA5");
            webhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("AuroraMC Big Brother").setDescription(String.format("A new %s has been logged. View at https://supersecretsettings.dev/bigbrother/exception?uuid=%s", t.getClass().getSimpleName(), uuid)).setColor(new Color(170, 0, 0)));
            try {
                webhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
