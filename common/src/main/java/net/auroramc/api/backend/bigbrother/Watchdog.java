/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.bigbrother;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.abstraction.ServerFactory;
import net.auroramc.api.backend.info.ProxyInfo;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.DiscordWebhook;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public final class Watchdog {

    public static UUID logException(Throwable t) {
        return logException(t, null);
    }

    private static UUID logException(Throwable t, AuroraMCPlayer executor) {
        return logException(t, executor, null);
    }

    public static UUID logException(Throwable t, AuroraMCPlayer executor, String commandSyntax) {
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

        List<WatchdogException> exceptions = AuroraMCAPI.getDbManager().getExceptions(t.getClass().getSimpleName(), proxy);

        for (WatchdogException e : exceptions) {
            if (similarity(e.getTrace(), trace) > 0.85d) {
                e.getOtherOccurrences().put(new JSONObject().put("trace", trace).put("server", server).put("timestamp", timestamp));
                AuroraMCAPI.getDbManager().updateException(e.getUuid(), e.getOtherOccurrences());
                return e.getUuid();
            }
        }

        ScheduleFactory.scheduleAsync(() -> {
            try {
                AuroraMCAPI.getDbManager().uploadException(timestamp, uuid, t.getClass().getSimpleName(), trace, commandSyntax, ((executor == null)?null:executor.getUuid()), proxy, server, serverState);
                DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1133782966213017660/PD0XP6UXxWnOTwpVE3WIteLqJFOGkFDHBBtQMxDSqyJZe748ViZiMybFzO2gF2nb4aA5");
                webhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("AuroraMC Watchdog").setDescription(String.format("A new %s has been logged. View at https://supersecretsettings.dev/watchdog/exceptions?uuid=%s", t.getClass().getSimpleName(), uuid)).setColor(new Color(170, 0, 0)));
                try {
                    webhook.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return uuid;
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - new LevenshteinDistance().apply(longer, shorter)) / (double) longerLength;
    }
}

